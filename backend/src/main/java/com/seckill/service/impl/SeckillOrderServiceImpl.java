package com.seckill.service.impl;

import com.seckill.common.Result;
import com.seckill.config.RabbitMQConfig;
import com.seckill.mapper.SeckillActivityMapper;
import com.seckill.mapper.SeckillOrderMapper;
import com.seckill.pojo.SeckillActivity;
import com.seckill.pojo.SeckillOrder;
import com.seckill.service.SeckillOrderService;
import com.seckill.zookeeper.ZooKeeperDistributedLock;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    private static final Logger log = LoggerFactory.getLogger(SeckillOrderServiceImpl.class);

    private final SeckillOrderMapper orderMapper;
    private final SeckillActivityMapper activityMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;
    private final ZooKeeperDistributedLock distributedLock;

    private static final String STOCK_CACHE_PREFIX = "seckill:stock:";
    private static final String ORDER_USER_PREFIX = "seckill:order:user:";
    private static final String SOLD_OUT_PREFIX = "seckill:soldout:";

    public SeckillOrderServiceImpl(SeckillOrderMapper orderMapper,
                                    SeckillActivityMapper activityMapper,
                                    RedisTemplate<String, Object> redisTemplate,
                                    ZooKeeperDistributedLock distributedLock) {
        this.orderMapper = orderMapper;
        this.activityMapper = activityMapper;
        this.redisTemplate = redisTemplate;
        this.distributedLock = distributedLock;
    }

    /**
     * 秒杀核心逻辑：
     * 1. 内存标记（sold out标记，减少Redis访问）
     * 2. Redis预减库存
     * 3. Redis去重判断
     * 4. RabbitMQ异步排队
     * 5. 消费者中ZooKeeper分布式锁 + DB落单
     */
    @Override
    public Result<String> seckill(Long userId, Long activityId) {
        // 1. 内存标记 - 如果已售罄直接返回
        if (Boolean.TRUE.equals(redisTemplate.hasKey(SOLD_OUT_PREFIX + activityId))) {
            return Result.error(400, "商品已售罄");
        }

        // 2. Redis预减库存（原子操作）
        Long stock = redisTemplate.opsForValue().decrement(STOCK_CACHE_PREFIX + activityId);
        if (stock == null || stock < 0) {
            // 库存不足，回滚Redis库存
            redisTemplate.opsForValue().increment(STOCK_CACHE_PREFIX + activityId);
            // 标记售罄
            redisTemplate.opsForValue().set(SOLD_OUT_PREFIX + activityId, true, 1, TimeUnit.HOURS);
            return Result.error(400, "商品已售罄");
        }

        // 3. Redis去重判断
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(ORDER_USER_PREFIX + userId + ":" + activityId, "1", 1, TimeUnit.HOURS);
        if (Boolean.FALSE.equals(isNew)) {
            // 重复下单，回滚库存
            redisTemplate.opsForValue().increment(STOCK_CACHE_PREFIX + activityId);
            return Result.error(400, "您已参与过此活动，不能重复抢购");
        }

        // 4. 发送消息到RabbitMQ，异步处理
        if (rabbitTemplate != null) {
            JSONObject message = new JSONObject();
            message.put("userId", userId);
            message.put("activityId", activityId);
            message.put("timestamp", System.currentTimeMillis());

            try {
                rabbitTemplate.convertAndSend(RabbitMQConfig.SECKILL_EXCHANGE, RabbitMQConfig.SECKILL_ROUTING_KEY, message.toJSONString());
            } catch (Exception e) {
                // MQ发送失败，回滚Redis
                redisTemplate.opsForValue().increment(STOCK_CACHE_PREFIX + activityId);
                redisTemplate.delete(ORDER_USER_PREFIX + userId + ":" + activityId);
                log.error("秒杀消息发送失败: userId={}, activityId={}", userId, activityId, e);
                return Result.error(500, "系统繁忙，请稍后重试");
            }
        }

        return Result.success("排队中，请稍后查看订单");
    }

    /**
     * MQ消费者调用的实际下单逻辑
     * 使用ZooKeeper分布式锁保证数据一致性
     */
    public void processSeckillOrder(Long userId, Long activityId) {
        String lockPath = "/seckill/lock/activity/" + activityId;

        try {
            // 获取ZooKeeper分布式锁
            boolean locked = distributedLock.acquire(lockPath, 10, TimeUnit.SECONDS);
            if (!locked) {
                log.warn("获取分布式锁失败，回滚: userId={}, activityId={}", userId, activityId);
                rollbackRedis(userId, activityId);
                return;
            }

            try {
                // 双重校验：DB中是否已存在订单
                SeckillOrder existOrder = orderMapper.findByUserAndActivity(userId, activityId);
                if (existOrder != null) {
                    log.info("重复订单，跳过: userId={}, activityId={}", userId, activityId);
                    return;
                }

                // 查询活动信息
                SeckillActivity activity = activityMapper.findById(activityId);
                if (activity == null || activity.getStatus() != 1) {
                    log.warn("活动不存在或未开始: activityId={}", activityId);
                    rollbackRedis(userId, activityId);
                    return;
                }

                // DB减库存（使用乐观锁 where stock >= 1）
                int rows = activityMapper.reduceStock(activityId, 1);
                if (rows == 0) {
                    log.warn("DB库存不足: activityId={}", activityId);
                    rollbackRedis(userId, activityId);
                    return;
                }

                // 创建订单
                SeckillOrder order = new SeckillOrder();
                order.setUserId(userId);
                order.setGoodsId(activity.getGoodsId());
                order.setActivityId(activityId);
                order.setOrderNo(generateOrderNo());
                order.setGoodsPrice(activity.getSeckillPrice());
                order.setStatus(0);
                orderMapper.insert(order);

                log.info("秒杀成功: userId={}, activityId={}, orderNo={}", userId, activityId, order.getOrderNo());
            } finally {
                distributedLock.release(lockPath);
            }
        } catch (Exception e) {
            log.error("秒杀处理异常: userId={}, activityId={}", userId, activityId, e);
            rollbackRedis(userId, activityId);
        }
    }

    private void rollbackRedis(Long userId, Long activityId) {
        redisTemplate.opsForValue().increment(STOCK_CACHE_PREFIX + activityId);
        redisTemplate.delete(ORDER_USER_PREFIX + userId + ":" + activityId);
    }

    private String generateOrderNo() {
        return "SK" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    @Override
    public Result<SeckillOrder> getOrderDetail(Long id) {
        SeckillOrder order = orderMapper.findById(id);
        if (order == null) return Result.error(404, "订单不存在");
        return Result.success(order);
    }

    @Override
    public Result<List<SeckillOrder>> listOrders(Long userId, Integer status, String orderNo, int page, int size) {
        List<SeckillOrder> list = orderMapper.findByCondition(userId, status, orderNo, (page - 1) * size, size);
        return Result.success(list);
    }

    @Override
    public Result<Void> payOrder(Long id) {
        int rows = orderMapper.pay(id);
        if (rows == 0) return Result.error(400, "支付失败，订单状态异常");
        return Result.success();
    }

    @Override
    public Result<Void> cancelOrder(Long id) {
        orderMapper.updateStatus(id, 4);
        return Result.success();
    }

    @Override
    public Result<Void> shipOrder(Long id) {
        orderMapper.updateStatus(id, 2);
        return Result.success();
    }

    @Override
    public Result<Void> completeOrder(Long id, Long userId) {
        SeckillOrder order = orderMapper.findById(id);
        if (order == null) return Result.error(404, "订单不存在");
        if (!order.getUserId().equals(userId)) return Result.error(403, "无权操作");
        orderMapper.updateStatus(id, 3);
        return Result.success();
    }
}
