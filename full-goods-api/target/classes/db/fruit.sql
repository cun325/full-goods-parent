-- 创建水果表
CREATE TABLE IF NOT EXISTS `t_fruit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '水果ID',
  `name` varchar(100) NOT NULL COMMENT '水果名称',
  `description` varchar(500) DEFAULT NULL COMMENT '水果描述',
  `origin` varchar(100) DEFAULT NULL COMMENT '产地',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `unit` varchar(50) DEFAULT NULL COMMENT '单位（如：500g/份，1kg/箱）',
  `stock` int(11) DEFAULT '0' COMMENT '库存数量',
  `image_url` varchar(255) DEFAULT NULL COMMENT '图片URL',
  `category` varchar(50) DEFAULT NULL COMMENT '分类（如：热带水果、应季水果等）',
  `taste` varchar(100) DEFAULT NULL COMMENT '口感（如：酸甜、香甜等）',
  `nutrition` varchar(500) DEFAULT NULL COMMENT '营养成分（如：富含维生素C等）',
  `suitable_crowd` varchar(200) DEFAULT NULL COMMENT '适合人群（如：适合孕妇、儿童等）',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `recommended` tinyint(1) DEFAULT '0' COMMENT '推荐状态：0-不推荐，1-推荐',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水果表';

-- 创建推荐历史表
CREATE TABLE IF NOT EXISTS `t_recommend_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `condition` varchar(500) NOT NULL COMMENT '推荐条件',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推荐历史表';

-- 初始化水果数据
INSERT INTO `t_fruit` (`name`, `description`, `origin`, `price`, `unit`, `stock`, `image_url`, `category`, `taste`, `nutrition`, `suitable_crowd`, `status`, `create_time`) VALUES
('海南妃子笑荔枝', '新鲜采摘，果大核小，甜度高', '海南', 39.90, '500g/份', 100, 'https://img1.baidu.com/it/u=3462355338,123123123&fm=253&fmt=auto&app=138&f=JPEG', '热带水果', '香甜多汁', '富含维生素C，有美容养颜的功效', '适合大多数人食用，孕妇适量', 1, NOW()),
('山东大樱桃', '个大饱满，色泽鲜艳', '山东烟台', 59.90, '500g/盒', 80, 'https://img1.baidu.com/it/u=123456789,987654321&fm=253&fmt=auto&app=138&f=JPEG', '应季水果', '酸甜可口', '富含铁元素和维生素，有补血功效', '适合贫血人群，孕妇适量', 1, NOW()),
('泰国椰青', '椰肉饱满，椰汁清甜', '泰国', 15.90, '1个', 50, 'https://img1.baidu.com/it/u=111111111,222222222&fm=253&fmt=auto&app=138&f=JPEG', '热带水果', '清甜爽口', '富含多种电解质，有助于补充体力', '适合夏季食用，运动后补充能量', 1, NOW()),
('新疆哈密瓜', '果肉厚实，香甜可口', '新疆', 29.90, '1个', 30, 'https://img1.baidu.com/it/u=333333333,444444444&fm=253&fmt=auto&app=138&f=JPEG', '应季水果', '香甜多汁', '含有丰富的胡萝卜素，有助于保护视力', '适合大多数人食用，糖尿病人慎食', 1, NOW()),
('山东大黄桃', '肉质细腻，汁多味甜', '山东临沂', 25.80, '4个装', 60, 'https://img1.baidu.com/it/u=555555555,666666666&fm=253&fmt=auto&app=138&f=JPEG', '应季水果', '香甜多汁', '富含维生素A和C，有助于美容养颜', '适合大多数人食用，儿童尤佳', 1, NOW()),
('广西百香果', '酸甜可口，香气浓郁', '广西', 29.90, '500g/份', 40, 'https://img1.baidu.com/it/u=777777777,888888888&fm=253&fmt=auto&app=138&f=JPEG', '热带水果', '酸甜可口', '富含维生素C和膳食纤维，有助于消化', '适合便秘人群，孕妇适量', 1, NOW());