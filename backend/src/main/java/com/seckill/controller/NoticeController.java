package com.seckill.controller;

import com.seckill.common.Result;
import com.seckill.common.UserContext;
import com.seckill.pojo.Notice;
import com.seckill.service.NoticeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/list")
    public Result<List<Notice>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return noticeService.listNotices(status, keyword, page, size);
    }

    @GetMapping("/published")
    public Result<List<Notice>> listPublished() {
        return noticeService.listPublished();
    }

    @GetMapping("/{id}")
    public Result<Notice> getDetail(@PathVariable Long id) {
        return noticeService.getNoticeDetail(id);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Notice notice) {
        notice.setAdminId(UserContext.getUserId());
        return noticeService.addNotice(notice);
    }

    @PutMapping
    public Result<Void> update(@RequestBody Notice notice) {
        return noticeService.updateNotice(notice);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return noticeService.deleteNotice(id);
    }
}
