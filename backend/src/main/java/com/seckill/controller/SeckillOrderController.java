package com.seckill.controller;

import com.seckill.common.Result;
import com.seckill.common.UserContext;
import com.seckill.pojo.SeckillOrder;
import com.seckill.service.SeckillOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seckill/order")
public class SeckillOrderController {

    private final SeckillOrderService orderService;

    public SeckillOrderController(SeckillOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/execute")
    public Result<String> executeSeckill(@RequestParam Long activityId) {
        return orderService.seckill(UserContext.getUserId(), activityId);
    }

    @GetMapping("/list")
    public Result<List<SeckillOrder>> list(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String orderNo,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return orderService.listOrders(userId, status, orderNo, page, size);
    }

    @GetMapping("/my")
    public Result<List<SeckillOrder>> myOrders(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return orderService.listOrders(UserContext.getUserId(), status, null, page, size);
    }

    @GetMapping("/{id}")
    public Result<SeckillOrder> getDetail(@PathVariable Long id) {
        return orderService.getOrderDetail(id);
    }

    @PutMapping("/{id}/pay")
    public Result<Void> pay(@PathVariable Long id) {
        return orderService.payOrder(id);
    }

    @PutMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    @PutMapping("/{id}/ship")
    public Result<Void> ship(@PathVariable Long id) {
        return orderService.shipOrder(id);
    }

    @PutMapping("/{id}/complete")
    public Result<Void> complete(@PathVariable Long id) {
        return orderService.completeOrder(id, UserContext.getUserId());
    }
}
