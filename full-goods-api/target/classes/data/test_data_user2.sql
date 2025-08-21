-- 为用户ID为2创建测试数据

-- 首先创建用户ID为2的用户
INSERT INTO `t_user` (`id`, `mobile`, `password`, `username`, `nickname`, `status`, `create_time`, `update_time`) 
VALUES (2, '13900139001', 'e10adc3949ba59abbe56e057f20f883e', 'testuser2', '测试用户2', 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
`mobile` = VALUES(`mobile`),
`username` = VALUES(`username`),
`nickname` = VALUES(`nickname`),
`update_time` = NOW();

-- 为用户ID为2创建物流通知消息（message_type = 1）
INSERT INTO `t_message` (`user_id`, `title`, `content`, `message_type`, `status`, `order_no`, `create_time`, `update_time`) VALUES
(2, '您的订单已发货', '亲爱的用户，您的订单 ORD202412251001 已发货，快递单号：SF1234567890，预计2-3个工作日送达。请保持手机畅通，注意查收。', 1, 0, 'ORD202412251001', DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(2, '包裹正在派送中', '您的包裹（快递单号：SF1234567890）正在派送中，快递员将在今日下午送达，请保持手机畅通。如有疑问请联系快递员：13800138888。', 1, 0, 'ORD202412251001', DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(2, '订单已签收', '您的订单 ORD202412251002 已成功签收，感谢您的购买！如对商品有任何问题，请及时联系客服。期待您的再次光临！', 1, 1, 'ORD202412251002', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 为用户ID为2创建客服消息（message_type = 2）
INSERT INTO `t_message` (`user_id`, `title`, `content`, `message_type`, `status`, `create_time`, `update_time`) VALUES
(2, '客服回复：关于退换货问题', '您好！关于您咨询的退换货问题，我们支持7天无理由退换货。请您在收货后7天内，保持商品原包装完好，联系客服办理退换货手续。退货邮费由我们承担。', 2, 0, DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(2, '客服回复：商品咨询', '您好！您咨询的海南妃子笑荔枝，我们采用冷链运输，保证新鲜度。建议收货后尽快食用，常温下可保存2-3天，冷藏可保存5-7天。', 2, 0, DATE_SUB(NOW(), INTERVAL 5 HOUR), DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(2, '客服回复：订单问题已解决', '您好！您反馈的订单问题我们已经处理完毕。相关费用已退回到您的原支付账户，请注意查收。如还有其他问题，随时联系我们。', 2, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 为用户ID为2创建系统通知（message_type = 3）
INSERT INTO `t_message` (`user_id`, `title`, `content`, `message_type`, `status`, `link_url`, `create_time`, `update_time`) VALUES
(2, '账户安全提醒', '您的账户在新设备上登录，登录时间：2024-12-25 14:30，登录地点：北京市朝阳区。如非本人操作，请及时修改密码并联系客服。', 3, 0, '/profile', DATE_SUB(NOW(), INTERVAL 30 MINUTE), DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(2, '系统维护通知', '尊敬的用户，我们将于今晚23:00-次日01:00进行系统维护升级，期间可能影响部分功能使用。维护完成后系统将更加稳定，感谢您的理解与支持！', 3, 0, '/settings', DATE_SUB(NOW(), INTERVAL 4 HOUR), DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(2, '新功能上线通知', '好消息！我们的积分商城功能已正式上线，您可以使用积分兑换精美礼品。快去积分商城看看吧！首次使用还有额外积分奖励哦~', 3, 1, '/home', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, '优惠活动通知', '限时特惠！精选水果5折起，满99元免邮费！活动时间：12月25日-12月31日，数量有限，先到先得。点击查看活动详情。', 3, 0, '/home', DATE_SUB(NOW(), INTERVAL 6 HOUR), DATE_SUB(NOW(), INTERVAL 6 HOUR));

-- 为用户ID为2创建收货地址
INSERT INTO `t_address` (`user_id`, `receiver_name`, `receiver_phone`, `province`, `city`, `district`, `detail_address`, `is_default`, `create_time`) VALUES
(2, '李明', '13900139001', '上海市', '上海市', '浦东新区', '张江高科技园区科苑路399号', 1, NOW())
ON DUPLICATE KEY UPDATE 
`receiver_name` = VALUES(`receiver_name`),
`receiver_phone` = VALUES(`receiver_phone`),
`update_time` = NOW();

-- 提交事务
COMMIT;