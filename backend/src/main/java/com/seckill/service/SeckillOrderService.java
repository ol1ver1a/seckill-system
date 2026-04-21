package com.seckill.service;

import com.seckill.common.Result;
import com.seckill.pojo.SeckillOrder;

import java.util.List;

public interface SeckillOrderService {
    Result<String> seckill(Long userId, Long activityId);
    Result<SeckillOrder> getOrderDetail(Long id);
    Result<List<SeckillOrder>> listOrders(Long userId, Integer status, String orderNo, int page, int size);
    Result<Void> payOrder(Long id);
    Result<Void> cancelOrder(Long id);
    Result<Void> shipOrder(Long id);
    Result<Void> completeOrder(Long id, Long userId);
}
