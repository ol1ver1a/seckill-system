package com.seckill.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Notice {
    private Long id;
    private String title;
    private String content;
    private Long adminId;
    private Integer status; // 0-草稿 1-已发布
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
