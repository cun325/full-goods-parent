-- 创建用户表
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `mobile` varchar(11) NOT NULL COMMENT '手机号',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `gender` varchar(10) DEFAULT NULL COMMENT '性别',
  `birthday` varchar(20) DEFAULT NULL COMMENT '生日',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `bio` varchar(500) DEFAULT NULL COMMENT '个人简介',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 如果表已存在，添加新字段
ALTER TABLE `t_user` 
ADD COLUMN IF NOT EXISTS `gender` varchar(10) DEFAULT NULL COMMENT '性别' AFTER `last_login_ip`,
ADD COLUMN IF NOT EXISTS `birthday` varchar(20) DEFAULT NULL COMMENT '生日' AFTER `gender`,
ADD COLUMN IF NOT EXISTS `email` varchar(100) DEFAULT NULL COMMENT '邮箱' AFTER `birthday`,
ADD COLUMN IF NOT EXISTS `address` varchar(255) DEFAULT NULL COMMENT '地址' AFTER `email`,
ADD COLUMN IF NOT EXISTS `bio` varchar(500) DEFAULT NULL COMMENT '个人简介' AFTER `address`;

-- 初始化测试用户数据
INSERT INTO `t_user` (`mobile`, `password`, `username`, `nickname`, `status`, `create_time`) 
VALUES ('13800138000', 'e10adc3949ba59abbe56e057f20f883e', 'test', '测试用户', 1, NOW());