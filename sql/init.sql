-- =============================================
-- 分布式秒杀系统 数据库初始化脚本
-- =============================================

CREATE DATABASE IF NOT EXISTS seckill DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE seckill;

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(64) NOT NULL COMMENT '用户名',
  `password` VARCHAR(128) NOT NULL COMMENT '密码(MD5)',
  `salt` VARCHAR(32) DEFAULT NULL COMMENT '盐',
  `nickname` VARCHAR(64) DEFAULT NULL COMMENT '昵称',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
  `role` TINYINT NOT NULL DEFAULT 0 COMMENT '角色: 0-消费者 1-商家 2-管理员',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
  `avatar` VARCHAR(256) DEFAULT NULL COMMENT '头像URL',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- 商品表
-- ----------------------------
DROP TABLE IF EXISTS `t_goods`;
CREATE TABLE `t_goods` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `merchant_id` BIGINT DEFAULT NULL COMMENT '商家ID',
  `goods_name` VARCHAR(256) NOT NULL COMMENT '商品名称',
  `goods_title` VARCHAR(512) DEFAULT NULL COMMENT '商品标题',
  `goods_img` VARCHAR(512) DEFAULT NULL COMMENT '商品图片URL',
  `goods_detail` TEXT DEFAULT NULL COMMENT '商品详情',
  `goods_price` DECIMAL(10,2) NOT NULL COMMENT '商品原价',
  `seckill_price` DECIMAL(10,2) DEFAULT NULL COMMENT '秒杀价',
  `stock_count` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-下架 1-上架',
  `category` VARCHAR(64) DEFAULT NULL COMMENT '分类',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_merchant` (`merchant_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- ----------------------------
-- 秒杀活动表
-- ----------------------------
DROP TABLE IF EXISTS `t_seckill_activity`;
CREATE TABLE `t_seckill_activity` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `goods_id` BIGINT NOT NULL COMMENT '商品ID',
  `merchant_id` BIGINT DEFAULT NULL COMMENT '商家ID',
  `activity_name` VARCHAR(256) NOT NULL COMMENT '活动名称',
  `seckill_price` DECIMAL(10,2) NOT NULL COMMENT '秒杀价格',
  `stock_count` INT NOT NULL DEFAULT 0 COMMENT '秒杀库存',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME NOT NULL COMMENT '结束时间',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-未开始 1-进行中 2-已结束 3-已审核 4-审核拒绝',
  `limit_count` INT NOT NULL DEFAULT 1 COMMENT '每人限购数量',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_goods` (`goods_id`),
  KEY `idx_time` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀活动表';

-- ----------------------------
-- 秒杀订单表
-- ----------------------------
DROP TABLE IF EXISTS `t_seckill_order`;
CREATE TABLE `t_seckill_order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `goods_id` BIGINT NOT NULL COMMENT '商品ID',
  `activity_id` BIGINT NOT NULL COMMENT '活动ID',
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
  `goods_price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态: 0-待支付 1-已支付 2-已发货 3-已完成 4-已取消 5-已退款',
  `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  UNIQUE KEY `uk_user_goods` (`user_id`, `activity_id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_goods` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀订单表';

-- ----------------------------
-- 公告表
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` VARCHAR(256) NOT NULL COMMENT '标题',
  `content` TEXT NOT NULL COMMENT '内容',
  `admin_id` BIGINT DEFAULT NULL COMMENT '发布管理员ID',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-草稿 1-已发布',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- ----------------------------
-- 初始数据
-- ----------------------------
-- 管理员 (密码: 123456, salt: 1a2b3c4d -> MD5("1234561a2b3c4d"))
INSERT INTO `t_user` (`username`, `password`, `salt`, `nickname`, `role`, `status`) VALUES
('admin', '8d1c9457e01d9803f1caaeab87c8c07a', '1a2b3c4d', 'Admin', 2, 1);

-- 商家 (密码: 123456, salt: 5e6f7a8b -> MD5("1234565e6f7a8b"))
INSERT INTO `t_user` (`username`, `password`, `salt`, `nickname`, `role`, `status`) VALUES
('merchant1', '569fb21ff871047cbb592fdd310e0a4d', '5e6f7a8b', 'MerchantA', 1, 1);

-- 消费者 (密码: 123456, salt: 9c0d1e2f -> MD5("1234569c0d1e2f"))
INSERT INTO `t_user` (`username`, `password`, `salt`, `nickname`, `role`, `status`) VALUES
('user1', '56ada219f4af2fcc9a7afa8e3a99ca63', '9c0d1e2f', 'UserA', 0, 1);

-- 商品
INSERT INTO `t_goods` (`merchant_id`, `goods_name`, `goods_title`, `goods_img`, `goods_detail`, `goods_price`, `seckill_price`, `stock_count`, `status`, `category`) VALUES
(2, 'iPhone 16 Pro', 'iPhone 16 Pro Latest Flagship', '/images/iphone16.jpg', 'A18 chip, Super Retina XDR display', 8999.00, 6999.00, 100, 1, 'Phone'),
(2, 'MacBook Air M4', 'Lightweight High Performance Laptop', '/images/macbook.jpg', 'M4 chip, 18 hour battery life', 9999.00, 7999.00, 50, 1, 'Computer'),
(2, 'AirPods Pro 3', 'Apple Wireless Noise-Canceling Earbuds', '/images/airpods.jpg', 'Adaptive ANC, Spatial Audio', 1899.00, 1299.00, 200, 1, 'Earbuds');

-- 秒杀活动
INSERT INTO `t_seckill_activity` (`goods_id`, `merchant_id`, `activity_name`, `seckill_price`, `stock_count`, `start_time`, `end_time`, `status`, `limit_count`) VALUES
(1, 2, 'iPhone 16 Pro Flash Sale', 6999.00, 100, NOW(), DATE_ADD(NOW(), INTERVAL 24 HOUR), 1, 1),
(2, 2, 'MacBook Air M4 Limited Purchase', 7999.00, 50, NOW(), DATE_ADD(NOW(), INTERVAL 24 HOUR), 1, 1),
(3, 2, 'AirPods Pro 3 Special Sale', 1299.00, 200, NOW(), DATE_ADD(NOW(), INTERVAL 24 HOUR), 1, 1);

-- 公告
INSERT INTO `t_notice` (`title`, `content`, `admin_id`, `status`) VALUES
('Flash Sale System Launch', 'Distributed seckill system is officially online!', 1, 1),
('Activity Rules', 'Limit 1 item per person per event. No malicious scalping.', 1, 1);
