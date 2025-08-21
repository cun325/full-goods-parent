-- 创建轮播图表
CREATE TABLE IF NOT EXISTS `t_banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
  `image_url` TEXT NOT NULL COMMENT '图片URL',
  `link_url` varchar(255) DEFAULT NULL COMMENT '链接URL',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';

-- 初始化轮播图数据
INSERT INTO `t_banner` (`image_url`, `link_url`, `sort`, `status`, `create_time`) VALUES
('/images/banners/banner1.jpg', '/fruit/detail/1', 1, 1, NOW()),
('/images/banners/banner2.jpg', '/fruit/detail/2', 2, 1, NOW()),
('/images/banners/banner3.jpg', '/fruit/detail/3', 3, 1, NOW());