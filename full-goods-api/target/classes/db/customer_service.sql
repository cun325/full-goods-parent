/*
-- 创建客服消息表
CREATE TABLE IF NOT EXISTS `t_customer_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `user_avatar` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `content` text NOT NULL COMMENT '消息内容',
  `message_type` varchar(20) DEFAULT 'text' COMMENT '消息类型：text-文本，image-图片，file-文件',
  `status` varchar(20) DEFAULT 'pending' COMMENT '处理状态：pending-未处理，processing-处理中，done-已处理，ai-AI回复',
  `priority` tinyint(4) DEFAULT '1' COMMENT '优先级：1-低，2-中，3-高',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '是否已读：0-未读，1-已读',
  `admin_id` bigint(20) DEFAULT NULL COMMENT '处理的管理员ID',
  `admin_name` varchar(50) DEFAULT NULL COMMENT '处理的管理员名称',
  `reply_count` int(11) DEFAULT '0' COMMENT '回复数量',
  `last_reply_time` datetime DEFAULT NULL COMMENT '最后回复时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_admin_id` (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客服消息表';

-- 创建客服对话表
CREATE TABLE IF NOT EXISTS `t_customer_dialog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '对话ID',
  `message_id` bigint(20) NOT NULL COMMENT '消息ID',
  `sender_type` varchar(20) NOT NULL COMMENT '发送者类型：user-用户，admin-管理员，ai-AI',
  `sender_id` bigint(20) DEFAULT NULL COMMENT '发送者ID',
  `sender_name` varchar(50) DEFAULT NULL COMMENT '发送者名称',
  `sender_avatar` varchar(255) DEFAULT NULL COMMENT '发送者头像',
  `content` text NOT NULL COMMENT '对话内容',
  `content_type` varchar(20) DEFAULT 'text' COMMENT '内容类型：text-文本，image-图片，file-文件',
  `file_url` varchar(500) DEFAULT NULL COMMENT '文件URL（图片、文件等）',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名称',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小（字节）',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '是否已读：0-未读，1-已读',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_message_id` (`message_id`),
  KEY `idx_sender_type` (`sender_type`),
  KEY `idx_sender_id` (`sender_id`),
  KEY `idx_create_time` (`create_time`),
  FOREIGN KEY (`message_id`) REFERENCES `t_customer_message` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客服对话表';

-- 创建客服统计表
CREATE TABLE IF NOT EXISTS `t_customer_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `total_messages` int(11) DEFAULT '0' COMMENT '总消息数',
  `pending_messages` int(11) DEFAULT '0' COMMENT '待处理消息数',
  `processed_messages` int(11) DEFAULT '0' COMMENT '已处理消息数',
  `ai_replied_messages` int(11) DEFAULT '0' COMMENT 'AI回复消息数',
  `avg_response_time` int(11) DEFAULT '0' COMMENT '平均响应时间（分钟）',
  `satisfaction_rate` decimal(5,2) DEFAULT '0.00' COMMENT '满意度（百分比）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_stat_date` (`stat_date`),
  KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客服统计表';

-- 初始化测试数据
INSERT INTO `t_customer_message` (`user_id`, `user_name`, `user_avatar`, `content`, `message_type`, `status`, `priority`, `is_read`, `reply_count`, `create_time`) VALUES
(1, '张三', '/images/avatars/user1.jpg', '你好，我想咨询一下海南妃子笑荔枝的保质期是多久？', 'text', 'pending', 2, 0, 0, NOW()),
(1, '李四', '/images/avatars/user2.jpg', '我昨天下的订单什么时候能发货？订单号是：ORD202412251234', 'text', 'processing', 3, 1, 1, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(2, '王五', '/images/avatars/user3.jpg', '收到的樱桃有些坏了，可以退换吗？', 'text', 'done', 2, 1, 3, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, '赵六', '/images/avatars/user4.jpg', '请问你们有没有无糖的水果？我是糖尿病患者', 'text', 'ai', 1, 1, 1, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(4, '孙七', '/images/avatars/user5.jpg', '想要批量采购，有优惠吗？', 'text', 'pending', 1, 0, 0, DATE_SUB(NOW(), INTERVAL 30 MINUTE));

-- 初始化对话数据
INSERT INTO `t_customer_dialog` (`message_id`, `sender_type`, `sender_id`, `sender_name`, `content`, `content_type`, `create_time`) VALUES
-- 消息1的对话
(1, 'user', 1, '张三', '你好，我想咨询一下海南妃子笑荔枝的保质期是多久？', 'text', NOW()),

-- 消息2的对话
(2, 'user', 1, '李四', '我昨天下的订单什么时候能发货？订单号是：ORD202412251234', 'text', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(2, 'admin', 1001, '客服小王', '您好，我来帮您查询一下订单状态', 'text', DATE_SUB(NOW(), INTERVAL 1 HOUR, 50 MINUTE)),

-- 消息3的对话
(3, 'user', 2, '王五', '收到的樱桃有些坏了，可以退换吗？', 'text', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, 'admin', 1002, '客服小李', '非常抱歉给您带来不便，我们支持7天无理由退换货', 'text', DATE_SUB(NOW(), INTERVAL 23 HOUR)),
(3, 'admin', 1002, '客服小李', '请您提供一下订单号和商品照片，我们会尽快为您处理', 'text', DATE_SUB(NOW(), INTERVAL 22 HOUR, 30 MINUTE)),
(3, 'user', 2, '王五', '好的，订单号是ORD202412241234，照片我稍后发给您', 'text', DATE_SUB(NOW(), INTERVAL 22 HOUR)),

-- 消息4的对话
(4, 'user', 3, '赵六', '请问你们有没有无糖的水果？我是糖尿病患者', 'text', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(4, 'ai', NULL, 'AI助手', '您好！对于糖尿病患者，我们推荐一些低糖水果，如：柚子、猕猴桃、草莓等。这些水果的糖分相对较低，但建议您适量食用并咨询医生意见。', 'text', DATE_SUB(NOW(), INTERVAL 2 HOUR, 45 MINUTE)),

-- 消息5的对话
(5, 'user', 4, '孙七', '想要批量采购，有优惠吗？', 'text', DATE_SUB(NOW(), INTERVAL 30 MINUTE));

-- 更新消息表的回复数量和最后回复时间
UPDATE `t_customer_message` SET `reply_count` = 1, `last_reply_time` = DATE_SUB(NOW(), INTERVAL 1 HOUR, 50 MINUTE) WHERE `id` = 2;
UPDATE `t_customer_message` SET `reply_count` = 3, `last_reply_time` = DATE_SUB(NOW(), INTERVAL 22 HOUR) WHERE `id` = 3;
UPDATE `t_customer_message` SET `reply_count` = 1, `last_reply_time` = DATE_SUB(NOW(), INTERVAL 2 HOUR, 45 MINUTE) WHERE `id` = 4;

-- 初始化统计数据
INSERT INTO `t_customer_statistics` (`stat_date`, `total_messages`, `pending_messages`, `processed_messages`, `ai_replied_messages`, `avg_response_time`, `satisfaction_rate`) VALUES
(CURDATE(), 5, 2, 2, 1, 25, 85.50),
(DATE_SUB(CURDATE(), INTERVAL 1 DAY), 8, 1, 6, 1, 18, 92.30),
(DATE_SUB(CURDATE(), INTERVAL 2 DAY), 6, 0, 5, 1, 22, 88.70);*/
