package com.seckill.mq;

import com.seckill.service.impl.SeckillOrderServiceImpl;
import com.alibaba.fastjson2.JSONObject;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 秒杀消息消费者
 * 从RabbitMQ中消费秒杀请求，执行实际的下单逻辑
 *
 * 流程：
 * 1. 接收MQ消息
 * 2. 调用 SeckillOrderServiceImpl.processSeckillOrder() 执行下单
 * 3. 下单过程中使用ZooKeeper分布式锁保证数据一致性
 * 4. 手动ACK确认消息
 */
@Component
public class SeckillMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(SeckillMessageConsumer.class);

    private final SeckillOrderServiceImpl seckillOrderService;

    public SeckillMessageConsumer(SeckillOrderServiceImpl seckillOrderService) {
        this.seckillOrderService = seckillOrderService;
    }

    @RabbitListener(queues = "seckill.queue")
    public void handleSeckillMessage(Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String body = new String(message.getBody());
            JSONObject json = JSONObject.parseObject(body);
            Long userId = json.getLong("userId");
            Long activityId = json.getLong("activityId");

            log.info("收到秒杀消息: userId={}, activityId={}", userId, activityId);

            // 执行秒杀下单（含分布式锁）
            seckillOrderService.processSeckillOrder(userId, activityId);

            // 手动确认消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("秒杀消息处理失败", e);
            try {
                // 消息处理失败，拒绝并重新入队
                channel.basicNack(deliveryTag, false, true);
            } catch (Exception ex) {
                log.error("消息NACK失败", ex);
            }
        }
    }
}
