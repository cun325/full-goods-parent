-- 消息系统相关表结构

-- 客服会话表
CREATE TABLE IF NOT EXISTS `t_customer_service_session` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会话ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `service_id` bigint(20) DEFAULT NULL COMMENT '客服ID',
  `session_type` int(11) NOT NULL DEFAULT '1' COMMENT '会话类型：1-智能客服，2-人工客服',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '会话状态：1-进行中，2-已结束，3-等待中',
  `title` varchar(255) DEFAULT NULL COMMENT '会话标题',
  `last_message` text COMMENT '最后一条消息内容',
  `last_message_time` datetime DEFAULT NULL COMMENT '最后消息时间',
  `unread_count` int(11) DEFAULT '0' COMMENT '未读消息数',
  `start_time` datetime DEFAULT NULL COMMENT '会话开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '会话结束时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_service_id` (`service_id`),
  KEY `idx_session_type` (`session_type`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客服会话表';

-- 消息表（如果不存在）
CREATE TABLE IF NOT EXISTS `t_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `title` varchar(255) NOT NULL COMMENT '消息标题',
  `content` text NOT NULL COMMENT '消息内容',
  `message_type` int(11) NOT NULL DEFAULT '1' COMMENT '消息类型：1-物流，2-客服，3-系统，4-优惠',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '消息状态：0-未读，1-已读',
  `order_no` varchar(64) DEFAULT NULL COMMENT '关联订单号',
  `template_id` bigint(20) DEFAULT NULL COMMENT '消息模板ID',
  `extra_data` json DEFAULT NULL COMMENT '扩展数据',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_message_type` (`message_type`),
  KEY `idx_status` (`status`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

-- 消息模板表
CREATE TABLE IF NOT EXISTS `t_message_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `code` varchar(64) NOT NULL COMMENT '模板编码',
  `name` varchar(255) NOT NULL COMMENT '模板名称',
  `message_type` int(11) NOT NULL COMMENT '消息类型：1-订单通知，2-物流通知，3-系统通知，4-客服消息，5-优惠活动，6-支付通知，7-库存预警，8-用户相关',
  `title` varchar(255) NOT NULL COMMENT '消息标题模板',
  `content` text NOT NULL COMMENT '消息内容模板',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '图标URL',
  `link_template` varchar(500) DEFAULT NULL COMMENT '跳转链接模板',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `description` varchar(500) DEFAULT NULL COMMENT '模板描述',
  `params_desc` varchar(1000) DEFAULT NULL COMMENT '参数说明',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_message_type` (`message_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息模板表';

-- 插入一些基础的消息模板数据
INSERT IGNORE INTO `t_message_template` (`code`, `name`, `message_type`, `title`, `content`, `icon_url`, `link_template`, `status`, `description`, `params_desc`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`) VALUES
('ORDER_SHIPPED', '订单发货通知', 2, '您的订单已发货', '亲爱的用户，您的订单{{orderNo}}已发货，物流单号：{{trackingNo}}，请注意查收。', '/icons/shipping.svg', '/order/tracking/{{trackingNo}}', 1, '订单发货后的通知模板', 'orderNo:订单号, trackingNo:快递单号', NOW(), NOW(), 'system', 'system', '订单发货通知模板'),
('ORDER_DELIVERING', '订单配送中通知', 2, '您的订单正在配送中', '您的订单{{orderNo}}正在配送中，预计今日送达，请保持手机畅通。', '/icons/delivery.svg', '/order/detail/{{orderNo}}', 1, '订单配送中的通知模板', 'orderNo:订单号', NOW(), NOW(), 'system', 'system', '订单配送中通知模板'),
('ORDER_DELIVERED', '订单已送达通知', 2, '您的订单已送达', '您的订单{{orderNo}}已成功送达，感谢您的购买！', '/icons/delivered.svg', '/order/detail/{{orderNo}}', 1, '订单送达后的通知模板', 'orderNo:订单号', NOW(), NOW(), 'system', 'system', '订单送达通知模板'),
('SYSTEM_MAINTENANCE', '系统维护通知', 3, '系统维护通知', '系统将于{{startTime}}至{{endTime}}进行维护，期间可能影响正常使用，敬请谅解。', '/icons/maintenance.svg', '', 1, '系统维护通知模板', 'startTime:开始时间, endTime:结束时间', NOW(), NOW(), 'system', 'system', '系统维护通知模板'),
('PROMOTION_ACTIVITY', '限时优惠活动', 5, '限时优惠开始', '{{activityName}}活动开始啦！{{discount}}优惠等你来抢，活动截止{{endTime}}', '/icons/promotion.svg', '/promotion/{{activityId}}', 1, '优惠活动开始通知模板', 'activityName:活动名称, discount:优惠信息, endTime:结束时间, activityId:活动ID', NOW(), NOW(), 'system', 'system', '优惠活动通知模板'),
('NEW_USER_COUPON', '新人优惠券', 5, '新人优惠券已到账', '欢迎使用鲜果云商！新人专享优惠券已发放到您的账户，快去使用吧！', '/icons/coupon.svg', '/user/coupons', 1, '新用户优惠券到账通知模板', '', NOW(), NOW(), 'system', 'system', '新人优惠券模板');