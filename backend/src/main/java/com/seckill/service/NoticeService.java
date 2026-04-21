package com.seckill.service;

import com.seckill.common.Result;
import com.seckill.pojo.Notice;

import java.util.List;

public interface NoticeService {
    Result<List<Notice>> listNotices(Integer status, String keyword, int page, int size);
    Result<List<Notice>> listPublished();
    Result<Notice> getNoticeDetail(Long id);
    Result<Void> addNotice(Notice notice);
    Result<Void> updateNotice(Notice notice);
    Result<Void> deleteNotice(Long id);
}
