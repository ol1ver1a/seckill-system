package com.seckill.service;

import com.seckill.common.Result;
import com.seckill.pojo.SeckillActivity;

import java.util.List;

public interface SeckillActivityService {
    Result<List<SeckillActivity>> listActivities(Long merchantId, Integer status, String keyword, int page, int size);
    Result<List<SeckillActivity>> listActive();
    Result<SeckillActivity> getActivityDetail(Long id);
    Result<Void> createActivity(SeckillActivity activity);
    Result<Void> updateActivity(SeckillActivity activity);
    Result<Void> updateActivityStatus(Long id, Integer status);
    Result<Void> auditActivity(Long id, Integer status);
}
