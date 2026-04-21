package com.seckill.pojo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String salt;
    private String nickname;
    private String phone;
    private String email;
    private Integer role;   // 0-消费者 1-商家 2-管理员
    private Integer status; // 0-禁用 1-正常
    private String avatar;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
