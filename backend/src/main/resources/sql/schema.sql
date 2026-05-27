-- 青循环数据库初始化脚本
CREATE DATABASE IF NOT EXISTS qingxunhuan DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE qingxunhuan;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `openid` VARCHAR(64) NOT NULL COMMENT '微信openid',
  `nickname` VARCHAR(64) DEFAULT '' COMMENT '昵称',
  `avatar` VARCHAR(512) DEFAULT '' COMMENT '头像URL',
  `phone` VARCHAR(20) DEFAULT '' COMMENT '手机号',
  `school` VARCHAR(64) DEFAULT '' COMMENT '学校',
  `campus` VARCHAR(64) DEFAULT '' COMMENT '校区',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 1正常 0禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 1已删除 0未删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 管理员表
CREATE TABLE IF NOT EXISTS `admin` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` VARCHAR(32) NOT NULL COMMENT '用户名',
  `password` VARCHAR(128) NOT NULL COMMENT '密码(BCrypt加密)',
  `nickname` VARCHAR(32) DEFAULT '' COMMENT '昵称',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 1正常 0禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 默认管理员 admin / 123456
INSERT INTO `admin` (`username`, `password`, `nickname`) VALUES
('admin', '$2b$10$Z89c9XjyFkEKHcplBiUr5uCvnnM3ZSqTJm.mY8g08bupCacYvUWC6', '系统管理员')
ON DUPLICATE KEY UPDATE `username` = `username`;

-- 商品分类表
CREATE TABLE IF NOT EXISTS `category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NOT NULL COMMENT '分类名称',
  `icon` VARCHAR(256) DEFAULT '' COMMENT '图标',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父分类ID, 0为一级分类',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 初始化分类数据
INSERT INTO `category` (`id`, `name`, `icon`, `sort`, `parent_id`) VALUES
(1, '数码', '', 1, 0),
(2, '书籍', '', 2, 0),
(3, '生活用品', '', 3, 0),
(4, '服装', '', 4, 0),
(5, '运动', '', 5, 0),
(6, '其他', '', 6, 0)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 商品表
CREATE TABLE IF NOT EXISTS `goods` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(128) NOT NULL COMMENT '标题',
  `description` TEXT COMMENT '描述',
  `price` DECIMAL(10,2) NOT NULL COMMENT '售价',
  `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
  `images` VARCHAR(2048) DEFAULT '' COMMENT '图片JSON数组',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `user_id` BIGINT NOT NULL COMMENT '发布者ID',
  `school` VARCHAR(64) DEFAULT '' COMMENT '学校',
  `campus` VARCHAR(64) DEFAULT '' COMMENT '校区',
  `condition` VARCHAR(32) DEFAULT '' COMMENT '成色',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 1在售 0已下架',
  `view_count` INT DEFAULT 0 COMMENT '浏览量',
  `collect_count` INT DEFAULT 0 COMMENT '收藏数',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_school` (`school`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 收藏表
CREATE TABLE IF NOT EXISTS `collect` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `goods_id` BIGINT NOT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_goods` (`user_id`, `goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
  `goods_id` BIGINT NOT NULL,
  `buyer_id` BIGINT NOT NULL,
  `seller_id` BIGINT NOT NULL,
  `amount` DECIMAL(10,2) NOT NULL,
  `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING/PAID/SHIPPED/COMPLETED/CANCELLED',
  `remark` VARCHAR(256) DEFAULT '',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `pay_time` DATETIME DEFAULT NULL,
  `complete_time` DATETIME DEFAULT NULL,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_buyer_id` (`buyer_id`),
  KEY `idx_seller_id` (`seller_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 消息表
CREATE TABLE IF NOT EXISTS `message` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `from_user_id` BIGINT NOT NULL,
  `to_user_id` BIGINT NOT NULL,
  `goods_id` BIGINT DEFAULT NULL,
  `content` TEXT NOT NULL,
  `is_read` TINYINT DEFAULT 0,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_from_to` (`from_user_id`, `to_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';
