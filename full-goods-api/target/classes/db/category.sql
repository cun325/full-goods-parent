-- 创建水果分类表
CREATE TABLE IF NOT EXISTS `t_fruit_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `icon_name` varchar(100) DEFAULT NULL COMMENT '图标名称（Element Plus图标）',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '图标URL',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水果分类表';

-- 插入前端分类数据
INSERT INTO `t_fruit_category` (`name`, `icon_name`, `icon_url`, `sort_order`, `status`, `create_by`) VALUES
('进口水果', 'Apple', '/images/icons/apple.jpg', 1, 1, 'system'),
('当季水果', 'Cherry', '/images/icons/cherry.jpg', 2, 1, 'system'),
('热带水果', 'Watermelon', '/images/icons/watermelon.jpg', 3, 1, 'system'),
('柑橘类', 'Orange', '/images/icons/orange.jpg', 4, 1, 'system'),
('礼盒', 'Present', '/images/icons/present.jpg', 5, 1, 'system'),
('应季水果', 'Cherry', '/images/icons/seasonal.jpg', 6, 1, 'system');