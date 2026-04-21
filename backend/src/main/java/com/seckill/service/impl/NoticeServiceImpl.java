package com.seckill.service.impl;

import com.seckill.common.Result;
import com.seckill.mapper.NoticeMapper;
import com.seckill.pojo.Notice;
import com.seckill.service.NoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    public NoticeServiceImpl(NoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }

    @Override
    public Result<List<Notice>> listNotices(Integer status, String keyword, int page, int size) {
        List<Notice> list = noticeMapper.findByCondition(status, keyword, (page - 1) * size, size);
        return Result.success(list);
    }

    @Override
    public Result<List<Notice>> listPublished() {
        return Result.success(noticeMapper.findPublished());
    }

    @Override
    public Result<Notice> getNoticeDetail(Long id) {
        Notice notice = noticeMapper.findById(id);
        if (notice == null) return Result.error(404, "公告不存在");
        return Result.success(notice);
    }

    @Override
    public Result<Void> addNotice(Notice notice) {
        noticeMapper.insert(notice);
        return Result.success();
    }

    @Override
    public Result<Void> updateNotice(Notice notice) {
        noticeMapper.update(notice);
        return Result.success();
    }

    @Override
    public Result<Void> deleteNotice(Long id) {
        noticeMapper.deleteById(id);
        return Result.success();
    }
}
