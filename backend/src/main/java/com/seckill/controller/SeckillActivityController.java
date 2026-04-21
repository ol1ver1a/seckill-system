package com.seckill.controller;

import com.seckill.common.Result;
import com.seckill.common.UserContext;
import com.seckill.pojo.SeckillActivity;
import com.seckill.service.SeckillActivityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seckill/activity")
public class SeckillActivityController {

    private final SeckillActivityService activityService;

    public SeckillActivityController(SeckillActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/list")
    public Result<List<SeckillActivity>> list(
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return activityService.listActivities(merchantId, status, keyword, page, size);
    }

    @GetMapping("/active")
    public Result<List<SeckillActivity>> listActive() {
        return activityService.listActive();
    }

    @GetMapping("/{id}")
    public Result<SeckillActivity> getDetail(@PathVariable Long id) {
        return activityService.getActivityDetail(id);
    }

    @PostMapping
    public Result<Void> create(@RequestBody SeckillActivity activity) {
        activity.setMerchantId(UserContext.getUserId());
        return activityService.createActivity(activity);
    }

    @PutMapping
    public Result<Void> update(@RequestBody SeckillActivity activity) {
        return activityService.updateActivity(activity);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        return activityService.updateActivityStatus(id, status);
    }

    @PutMapping("/{id}/audit")
    public Result<Void> audit(@PathVariable Long id, @RequestParam Integer status) {
        return activityService.auditActivity(id, status);
    }
}
