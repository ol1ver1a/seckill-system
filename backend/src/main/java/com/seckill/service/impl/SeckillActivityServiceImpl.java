package com.seckill.service.impl;

import com.seckill.common.Result;
import com.seckill.mapper.SeckillActivityMapper;
import com.seckill.pojo.SeckillActivity;
import com.seckill.service.SeckillActivityService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillActivityServiceImpl implements SeckillActivityService {

    private final SeckillActivityMapper activityMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ACTIVITY_CACHE_PREFIX = "seckill:activity:";
    private static final String STOCK_CACHE_PREFIX = "seckill:stock:";

    public SeckillActivityServiceImpl(SeckillActivityMapper activityMapper, RedisTemplate<String, Object> redisTemplate) {
        this.activityMapper = activityMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Result<List<SeckillActivity>> listActivities(Long merchantId, Integer status, String keyword, int page, int size) {
        List<SeckillActivity> list = activityMapper.findByCondition(merchantId, status, keyword, (page - 1) * size, size);
        return Result.success(list);
    }

    @Override
    public Result<List<SeckillActivity>> listActive() {
        // 先查缓存
        String key = "seckill:active:list";
        @SuppressWarnings("unchecked")
        List<SeckillActivity> cached = (List<SeckillActivity>) redisTemplate.opsForValue().get(key);
        if (cached != null) return Result.success(cached);

        List<SeckillActivity> list = activityMapper.findActive();
        // 同步库存到Redis
        for (SeckillActivity act : list) {
            redisTemplate.opsForValue().set(STOCK_CACHE_PREFIX + act.getId(), act.getStockCount(), 1, TimeUnit.HOURS);
        }
        redisTemplate.opsForValue().set(key, list, 5, TimeUnit.MINUTES);
        return Result.success(list);
    }

    @Override
    public Result<SeckillActivity> getActivityDetail(Long id) {
        String key = ACTIVITY_CACHE_PREFIX + id;
        @SuppressWarnings("unchecked")
        SeckillActivity cached = (SeckillActivity) redisTemplate.opsForValue().get(key);
        if (cached != null) return Result.success(cached);

        SeckillActivity activity = activityMapper.findById(id);
        if (activity == null) return Result.error(404, "活动不存在");
        redisTemplate.opsForValue().set(key, activity, 10, TimeUnit.MINUTES);
        return Result.success(activity);
    }

    @Override
    public Result<Void> createActivity(SeckillActivity activity) {
        if (activity.getStatus() == null) activity.setStatus(0);
        activityMapper.insert(activity);
        return Result.success();
    }

    @Override
    public Result<Void> updateActivity(SeckillActivity activity) {
        activityMapper.update(activity);
        // 清除缓存
        redisTemplate.delete(ACTIVITY_CACHE_PREFIX + activity.getId());
        redisTemplate.delete("seckill:active:list");
        return Result.success();
    }

    @Override
    public Result<Void> updateActivityStatus(Long id, Integer status) {
        activityMapper.updateStatus(id, status);
        redisTemplate.delete(ACTIVITY_CACHE_PREFIX + id);
        redisTemplate.delete("seckill:active:list");
        // 如果活动开始，预加载库存到Redis
        if (status == 1) {
            SeckillActivity activity = activityMapper.findById(id);
            if (activity != null) {
                redisTemplate.opsForValue().set(STOCK_CACHE_PREFIX + id, activity.getStockCount(), 1, TimeUnit.DAYS);
            }
        }
        return Result.success();
    }

    @Override
    public Result<Void> auditActivity(Long id, Integer status) {
        activityMapper.updateStatus(id, status);
        redisTemplate.delete(ACTIVITY_CACHE_PREFIX + id);
        redisTemplate.delete("seckill:active:list");
        return Result.success();
    }
}
