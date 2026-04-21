package com.seckill.service.impl;

import com.seckill.common.Result;
import com.seckill.mapper.GoodsMapper;
import com.seckill.pojo.Goods;
import com.seckill.service.GoodsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    private final GoodsMapper goodsMapper;

    public GoodsServiceImpl(GoodsMapper goodsMapper) {
        this.goodsMapper = goodsMapper;
    }

    @Override
    public Result<List<Goods>> listGoods(Long merchantId, Integer status, String keyword, int page, int size) {
        List<Goods> list = goodsMapper.findByCondition(merchantId, status, keyword, (page - 1) * size, size);
        return Result.success(list);
    }

    @Override
    public Result<List<Goods>> listOnSale() {
        return Result.success(goodsMapper.findAllOnSale());
    }

    @Override
    public Result<Goods> getGoodsDetail(Long id) {
        Goods goods = goodsMapper.findById(id);
        if (goods == null) return Result.error(404, "商品不存在");
        return Result.success(goods);
    }

    @Override
    public Result<Void> addGoods(Goods goods) {
        if (goods.getStatus() == null) goods.setStatus(1);
        goodsMapper.insert(goods);
        return Result.success();
    }

    @Override
    public Result<Void> updateGoods(Goods goods) {
        goodsMapper.update(goods);
        return Result.success();
    }

    @Override
    public Result<Void> updateGoodsStatus(Long id, Integer status) {
        goodsMapper.updateStatus(id, status);
        return Result.success();
    }

    @Override
    public Result<Void> deleteGoods(Long id) {
        goodsMapper.deleteById(id);
        return Result.success();
    }
}
