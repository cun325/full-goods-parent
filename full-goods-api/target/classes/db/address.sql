-- 创建收货地址表（如果不存在）
CREATE TABLE IF NOT EXISTS `t_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `receiver_name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收货人电话',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `district` varchar(50) DEFAULT NULL COMMENT '区/县',
  `detail_address` varchar(200) NOT NULL COMMENT '详细地址',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认地址：0-否，1-是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- 为测试用户（手机号：13800138000）初始化地址数据
INSERT INTO `t_address` (`user_id`, `receiver_name`, `receiver_phone`, `province`, `city`, `district`, `detail_address`, `is_default`, `create_time`) VALUES
(1, '张三', '13800138000', '北京市', '北京市', '朝阳区', '三里屯街道工体北路8号院', 1, NOW()),
(1, '李四', '13900139000', '上海市', '上海市', '浦东新区', '陆家嘴金融贸易区世纪大道100号', 0, NOW()),
(1, '王五', '13700137000', '广东省', '深圳市', '南山区', '科技园南区深南大道9988号', 0, NOW());