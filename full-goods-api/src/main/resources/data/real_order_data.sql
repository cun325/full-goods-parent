-- 为用户ID为2创建真实订单数据和对应的物流通知

-- 开始事务
START TRANSACTION;

-- 插入真实订单数据
INSERT INTO `t_order` (
    `order_no`, `user_id`, `total_amount`, `pay_amount`, `freight_amount`, `discount_amount`, 
    `pay_type`, `status`, `pay_time`, `delivery_time`, `receiver_name`, `receiver_phone`, 
    `receiver_province`, `receiver_city`, `receiver_district`, `receiver_address`, 
    `note`, `tracking_number`, `courier`, `create_time`, `update_time`
) VALUES 
-- 订单1：已发货状态
('ORD202412251001', 2, 99.80, 99.80, 10.00, 0.00, 1, 2, 
 DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 
 '张三', '13800138001', '广东省', '深圳市', '南山区', '科技园南区深南大道1001号', 
 '请尽快发货', 'SF1234567890', '顺丰速运', 
 DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),

-- 订单2：配送中状态
('ORD202412251002', 2, 119.70, 119.70, 10.00, 0.00, 2, 2, 
 DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 6 HOUR), 
 '张三', '13800138001', '广东省', '深圳市', '南山区', '科技园南区深南大道1001号', 
 '水果要新鲜', 'YTO9876543210', '圆通速递', 
 DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),

-- 订单3：已完成状态
('ORD202412251003', 2, 75.60, 75.60, 10.00, 0.00, 1, 3, 
 DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 
 '张三', '13800138001', '广东省', '深圳市', '南山区', '科技园南区深南大道1001号', 
 '包装要仔细', 'ZTO5555666677', '中通快递', 
 DATE_SUB(NOW(), INTERVAL 6 DAY), NOW());

-- 插入订单项数据
INSERT INTO `t_order_item` (
    `order_id`, `order_no`, `fruit_id`, `fruit_name`, `fruit_image`, `price`, `quantity`, `total_price`, 
    `create_time`, `update_time`
) VALUES 
-- 订单1的商品
((SELECT id FROM t_order WHERE order_no = 'ORD202412251001'), 'ORD202412251001', 1, '海南妃子笑荔枝', 'https://img1.baidu.com/it/u=3462355338,123123123&fm=253&fmt=auto&app=138&f=JPEG', 39.90, 1, 39.90, NOW(), NOW()),
((SELECT id FROM t_order WHERE order_no = 'ORD202412251001'), 'ORD202412251001', 4, '新疆哈密瓜', 'https://img1.baidu.com/it/u=333333333,444444444&fm=253&fmt=auto&app=138&f=JPEG', 29.90, 1, 29.90, NOW(), NOW()),
((SELECT id FROM t_order WHERE order_no = 'ORD202412251001'), 'ORD202412251001', 3, '泰国椰青', 'https://img1.baidu.com/it/u=111111111,222222222&fm=253&fmt=auto&app=138&f=JPEG', 15.90, 1, 15.90, NOW(), NOW()),

-- 订单2的商品
((SELECT id FROM t_order WHERE order_no = 'ORD202412251002'), 'ORD202412251002', 2, '山东大樱桃', 'https://img1.baidu.com/it/u=123456789,987654321&fm=253&fmt=auto&app=138&f=JPEG', 59.90, 1, 59.90, NOW(), NOW()),
((SELECT id FROM t_order WHERE order_no = 'ORD202412251002'), 'ORD202412251002', 6, '广西百香果', 'https://img1.baidu.com/it/u=777777777,888888888&fm=253&fmt=auto&app=138&f=JPEG', 29.90, 1, 29.90, NOW(), NOW()),
((SELECT id FROM t_order WHERE order_no = 'ORD202412251002'), 'ORD202412251002', 3, '泰国椰青', 'https://img1.baidu.com/it/u=111111111,222222222&fm=253&fmt=auto&app=138&f=JPEG', 15.90, 1, 15.90, NOW(), NOW()),

-- 订单3的商品
((SELECT id FROM t_order WHERE order_no = 'ORD202412251003'), 'ORD202412251003', 5, '山东大黄桃', 'https://img1.baidu.com/it/u=555555555,666666666&fm=253&fmt=auto&app=138&f=JPEG', 25.80, 1, 25.80, NOW(), NOW()),
((SELECT id FROM t_order WHERE order_no = 'ORD202412251003'), 'ORD202412251003', 1, '海南妃子笑荔枝', 'https://img1.baidu.com/it/u=3462355338,123123123&fm=253&fmt=auto&app=138&f=JPEG', 39.90, 1, 39.90, NOW(), NOW());

-- 插入对应的物流通知消息
INSERT INTO `t_message` (
    `user_id`, `title`, `content`, `message_type`, `status`, `order_no`, 
    `create_time`, `update_time`, `create_by`, `update_by`
) VALUES 
-- 订单1的物流通知
(2, '订单已发货', '您的订单ORD202412251001已发货，快递单号：SF1234567890，预计明日送达。包含：海南妃子笑荔枝、新疆哈密瓜、泰国椰青', 1, 0, 'ORD202412251001', DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(), 'system', 'system'),

-- 订单2的物流通知
(2, '订单正在配送中', '您的订单ORD202412251002正在配送中，快递单号：YTO9876543210，预计今日送达，请保持手机畅通。包含：山东大樱桃、广西百香果、泰国椰青', 1, 0, 'ORD202412251002', DATE_SUB(NOW(), INTERVAL 6 HOUR), NOW(), 'system', 'system'),

-- 订单3的物流通知
(2, '订单已送达', '您的订单ORD202412251003已成功送达，感谢您的购买！如有问题请联系客服。包含：山东大黄桃、海南妃子笑荔枝', 1, 1, 'ORD202412251003', DATE_SUB(NOW(), INTERVAL 3 DAY), NOW(), 'system', 'system'),

-- 额外的物流状态更新通知
(2, '包裹到达配送站', '您的订单ORD202412251002包裹已到达深圳南山配送站，正在安排配送，预计2小时内送达', 1, 0, 'ORD202412251002', DATE_SUB(NOW(), INTERVAL 2 HOUR), NOW(), 'system', 'system');

-- 提交事务
COMMIT;

-- 显示创建的订单信息
SELECT '创建的订单信息：' as message;
SELECT order_no, user_id, total_amount, status, tracking_number, courier, create_time 
FROM t_order 
WHERE user_id = 2 AND order_no LIKE 'ORD20241225%'
ORDER BY create_time DESC;

-- 显示创建的物流通知
SELECT '创建的物流通知：' as message;
SELECT id, title, content, order_no, status, create_time 
FROM t_message 
WHERE user_id = 2 AND message_type = 1 AND order_no LIKE 'ORD20241225%'
ORDER BY create_time DESC;