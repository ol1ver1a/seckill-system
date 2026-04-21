package com.seckill.service;

import com.seckill.common.Result;
import com.seckill.pojo.Goods;

import java.util.List;

public interface GoodsService {
    Result<List<Goods>> listGoods(Long merchantId, Integer status, String keyword, int page, int size);
    Result<List<Goods>> listOnSale();
    Result<Goods> getGoodsDetail(Long id);
    Result<Void> addGoods(Goods goods);
    Result<Void> updateGoods(Goods goods);
    Result<Void> updateGoodsStatus(Long id, Integer status);
    Result<Void> deleteGoods(Long id);
}
