-- 青芽数据库初始化脚本
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
