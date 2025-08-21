-- 创建限时特惠表
CREATE TABLE IF NOT EXISTS `t_flash_sale` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '限时特惠ID',
  `fruit_id` bigint(20) NOT NULL COMMENT '水果ID',
  `original_price` decimal(10,2) NOT NULL COMMENT '原价',
  `sale_price` decimal(10,2) NOT NULL COMMENT '特惠价',
  `discount_rate` decimal(5,2) DEFAULT NULL COMMENT '折扣率（如：0.8表示8折）',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `stock` int(11) DEFAULT '0' COMMENT '特惠库存',
  `sold_count` int(11) DEFAULT '0' COMMENT '已售数量',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_fruit_id` (`fruit_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='限时特惠表';

-- 初始化限时特惠数据
INSERT INTO `t_flash_sale` (`fruit_id`, `original_price`, `sale_price`, `discount_rate`, `start_time`, `end_time`, `stock`, `sold_count`, `status`, `create_time`) VALUES
(1, 39.90, 29.90, 0.75, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 50, 0, 1, NOW()),
(2, 59.90, 45.90, 0.77, NOW(), DATE_ADD(NOW(), INTERVAL 5 DAY), 30, 0, 1, NOW()),
(3, 15.90, 12.90, 0.81, NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY), 20, 0, 1, NOW()),
(4, 29.90, 22.90, 0.77, NOW(), DATE_ADD(NOW(), INTERVAL 6 DAY), 25, 0, 1, NOW()),
(5, 25.80, 19.80, 0.77, NOW(), DATE_ADD(NOW(), INTERVAL 4 DAY), 35, 0, 1, NOW()),
(6, 29.90, 24.90, 0.83, NOW(), DATE_ADD(NOW(), INTERVAL 8 DAY), 40, 0, 1, NOW());