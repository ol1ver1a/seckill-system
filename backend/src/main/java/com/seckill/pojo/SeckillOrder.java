package com.seckill.pojo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SeckillOrder {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long activityId;
    private String orderNo;
    private BigDecimal goodsPrice;
    private LocalDateTime createTime;
    private Integer status; // 0-待支付 1-已支付 2-已发货 3-已完成 4-已取消 5-已退款
    private LocalDateTime payTime;

    // 关联查询字段
    private String goodsName;
    private String goodsImg;
    private String username;
}
