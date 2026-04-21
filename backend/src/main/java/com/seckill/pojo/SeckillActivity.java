package com.seckill.pojo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SeckillActivity {
    private Long id;
    private Long goodsId;
    private Long merchantId;
    private String activityName;
    private BigDecimal seckillPrice;
    private Integer stockCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status; // 0-未开始 1-进行中 2-已结束 3-已审核 4-审核拒绝
    private Integer limitCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 关联查询字段
    private String goodsName;
    private String goodsImg;
    private BigDecimal goodsPrice;
}
