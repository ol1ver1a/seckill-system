package com.seckill.pojo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Goods {
    private Long id;
    private Long merchantId;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private BigDecimal goodsPrice;
    private BigDecimal seckillPrice;
    private Integer stockCount;
    private Integer status; // 0-下架 1-上架
    private String category;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
