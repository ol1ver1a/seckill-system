package com.seckill.controller;

import com.seckill.common.Result;
import com.seckill.common.UserContext;
import com.seckill.pojo.Goods;
import com.seckill.service.GoodsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping("/list")
    public Result<List<Goods>> list(
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return goodsService.listGoods(merchantId, status, keyword, page, size);
    }

    @GetMapping("/onsale")
    public Result<List<Goods>> listOnSale() {
        return goodsService.listOnSale();
    }

    @GetMapping("/{id}")
    public Result<Goods> getDetail(@PathVariable Long id) {
        return goodsService.getGoodsDetail(id);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Goods goods) {
        goods.setMerchantId(UserContext.getUserId());
        return goodsService.addGoods(goods);
    }

    @PutMapping
    public Result<Void> update(@RequestBody Goods goods) {
        return goodsService.updateGoods(goods);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        return goodsService.updateGoodsStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return goodsService.deleteGoods(id);
    }
}
