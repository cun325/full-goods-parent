-- 默认消息模板数据
INSERT INTO t_message_template (code, name, message_type, title, content, icon_url, link_template, status, description, params_desc, create_time, update_time, create_by, update_by, remark) VALUES

-- 订单相关模板
('ORDER_CREATED', '订单创建通知', 1, '订单创建成功', '您的订单{{orderNo}}已创建成功，订单金额：{{amount}}元。感谢您的购买！', '/icons/order.svg', '/order/detail/{{orderNo}}', 1, '用户下单成功后的通知模板', 'orderNo:订单号, amount:订单金额', NOW(), NOW(), 'system', 'system', '订单创建通知模板'),

('ORDER_PAID', '订单支付成功', 1, '支付成功', '您的订单{{orderNo}}支付成功，支付金额：{{amount}}元。我们将尽快为您发货！', '/icons/payment.svg', '/order/detail/{{orderNo}}', 1, '订单支付成功后的通知模板', 'orderNo:订单号, amount:支付金额', NOW(), NOW(), 'system', 'system', '订单支付成功模板'),

('ORDER_SHIPPED', '订单发货通知', 1, '订单已发货', '您的订单{{orderNo}}已发货，快递单号：{{trackingNo}}，预计{{estimatedTime}}送达。', '/icons/shipping.svg', '/order/tracking/{{trackingNo}}', 1, '订单发货后的通知模板', 'orderNo:订单号, trackingNo:快递单号, estimatedTime:预计送达时间', NOW(), NOW(), 'system', 'system', '订单发货通知模板'),

('ORDER_DELIVERED', '订单送达通知', 1, '订单已送达', '您的订单{{orderNo}}已成功送达，感谢您的购买！如有问题请联系客服。', '/icons/delivered.svg', '/order/detail/{{orderNo}}', 1, '订单送达后的通知模板', 'orderNo:订单号', NOW(), NOW(), 'system', 'system', '订单送达通知模板'),

('ORDER_CANCELLED', '订单取消通知', 1, '订单已取消', '您的订单{{orderNo}}已取消，如有疑问请联系客服。', '/icons/cancel.svg', '/order/detail/{{orderNo}}', 1, '订单取消后的通知模板', 'orderNo:订单号', NOW(), NOW(), 'system', 'system', '订单取消通知模板'),

-- 物流相关模板
('LOGISTICS_UPDATE', '物流状态更新', 2, '物流信息更新', '您的包裹{{trackingNo}}状态已更新：{{status}}，当前位置：{{location}}', '/icons/logistics.svg', '/logistics/tracking/{{trackingNo}}', 1, '物流状态更新通知模板', 'trackingNo:快递单号, status:物流状态, location:当前位置', NOW(), NOW(), 'system', 'system', '物流状态更新模板'),

('LOGISTICS_EXCEPTION', '物流异常通知', 2, '物流异常', '您的包裹{{trackingNo}}出现异常：{{reason}}，我们正在处理中，请耐心等待。', '/icons/warning.svg', '/logistics/tracking/{{trackingNo}}', 1, '物流异常通知模板', 'trackingNo:快递单号, reason:异常原因', NOW(), NOW(), 'system', 'system', '物流异常通知模板'),

-- 系统通知模板
('SYSTEM_MAINTENANCE', '系统维护通知', 3, '系统维护通知', '系统将于{{startTime}}至{{endTime}}进行维护，期间可能影响正常使用，敬请谅解。', '/icons/maintenance.svg', '', 1, '系统维护通知模板', 'startTime:开始时间, endTime:结束时间', NOW(), NOW(), 'system', 'system', '系统维护通知模板'),

('SYSTEM_UPDATE', '系统更新通知', 3, '系统更新', '系统已更新至{{version}}版本，新增功能：{{features}}', '/icons/update.svg', '/help/changelog', 1, '系统更新通知模板', 'version:版本号, features:新功能描述', NOW(), NOW(), 'system', 'system', '系统更新通知模板'),

-- 客服消息模板
('CUSTOMER_SERVICE_REPLY', '客服回复', 4, '客服回复', '客服{{serviceName}}回复：{{message}}', '/icons/service.svg', '/chat/{{chatId}}', 1, '客服回复消息模板', 'serviceName:客服姓名, message:回复内容, chatId:聊天ID', NOW(), NOW(), 'system', 'system', '客服回复模板'),

('CUSTOMER_SERVICE_TRANSFER', '客服转接', 4, '客服转接', '您的咨询已转接给{{serviceName}}，请稍等。', '/icons/transfer.svg', '/chat/{{chatId}}', 1, '客服转接通知模板', 'serviceName:客服姓名, chatId:聊天ID', NOW(), NOW(), 'system', 'system', '客服转接模板'),

-- 优惠活动模板
('PROMOTION_START', '优惠活动开始', 5, '限时优惠开始', '{{activityName}}活动开始啦！{{discount}}优惠等你来抢，活动截止{{endTime}}', '/icons/promotion.svg', '/promotion/{{activityId}}', 1, '优惠活动开始通知模板', 'activityName:活动名称, discount:优惠信息, endTime:结束时间, activityId:活动ID', NOW(), NOW(), 'system', 'system', '优惠活动开始模板'),

('PROMOTION_END', '优惠活动结束', 5, '优惠活动即将结束', '{{activityName}}活动即将结束，仅剩{{remainingTime}}，抓紧时间参与！', '/icons/countdown.svg', '/promotion/{{activityId}}', 1, '优惠活动结束提醒模板', 'activityName:活动名称, remainingTime:剩余时间, activityId:活动ID', NOW(), NOW(), 'system', 'system', '优惠活动结束模板'),

('COUPON_RECEIVED', '优惠券到账', 5, '优惠券到账', '恭喜您获得{{couponName}}，面值{{amount}}元，有效期至{{expireTime}}', '/icons/coupon.svg', '/user/coupons', 1, '优惠券到账通知模板', 'couponName:优惠券名称, amount:面值, expireTime:过期时间', NOW(), NOW(), 'system', 'system', '优惠券到账模板'),

('COUPON_EXPIRE', '优惠券即将过期', 5, '优惠券即将过期', '您的{{couponName}}将于{{expireTime}}过期，请尽快使用！', '/icons/expire.svg', '/user/coupons', 1, '优惠券过期提醒模板', 'couponName:优惠券名称, expireTime:过期时间', NOW(), NOW(), 'system', 'system', '优惠券过期提醒模板'),

-- 支付相关模板
('PAYMENT_SUCCESS', '支付成功通知', 6, '支付成功', '支付成功！订单号：{{orderNo}}，支付金额：{{amount}}元', '/icons/payment-success.svg', '/order/detail/{{orderNo}}', 1, '支付成功通知模板', 'orderNo:订单号, amount:支付金额', NOW(), NOW(), 'system', 'system', '支付成功通知模板'),

('PAYMENT_FAILED', '支付失败通知', 6, '支付失败', '支付失败，订单号：{{orderNo}}，失败原因：{{reason}}', '/icons/payment-failed.svg', '/order/detail/{{orderNo}}', 1, '支付失败通知模板', 'orderNo:订单号, reason:失败原因', NOW(), NOW(), 'system', 'system', '支付失败通知模板'),

('REFUND_SUCCESS', '退款成功通知', 6, '退款成功', '退款成功！订单号：{{orderNo}}，退款金额：{{amount}}元，预计{{arrivalTime}}到账', '/icons/refund.svg', '/order/detail/{{orderNo}}', 1, '退款成功通知模板', 'orderNo:订单号, amount:退款金额, arrivalTime:到账时间', NOW(), NOW(), 'system', 'system', '退款成功通知模板'),

-- 库存预警模板
('STOCK_LOW', '库存不足预警', 7, '库存不足', '商品{{productName}}库存不足，当前库存：{{currentStock}}，请及时补货', '/icons/stock-warning.svg', '/admin/products/{{productId}}', 1, '库存不足预警模板', 'productName:商品名称, currentStock:当前库存, productId:商品ID', NOW(), NOW(), 'system', 'system', '库存不足预警模板'),

('STOCK_OUT', '库存售罄通知', 7, '库存售罄', '商品{{productName}}已售罄，请及时补货或下架商品', '/icons/stock-out.svg', '/admin/products/{{productId}}', 1, '库存售罄通知模板', 'productName:商品名称, productId:商品ID', NOW(), NOW(), 'system', 'system', '库存售罄通知模板'),

-- 用户相关模板
('USER_REGISTER', '用户注册欢迎', 8, '欢迎注册', '欢迎{{username}}加入我们！您已成功注册，快去探索更多精彩内容吧！', '/icons/welcome.svg', '/user/profile', 1, '用户注册欢迎模板', 'username:用户名', NOW(), NOW(), 'system', 'system', '用户注册欢迎模板'),

('USER_LOGIN', '登录通知', 8, '登录提醒', '您的账户于{{loginTime}}在{{location}}登录，如非本人操作请及时修改密码', '/icons/login.svg', '/user/security', 1, '用户登录通知模板', 'loginTime:登录时间, location:登录地点', NOW(), NOW(), 'system', 'system', '用户登录通知模板'),

('PASSWORD_CHANGED', '密码修改通知', 8, '密码已修改', '您的账户密码已于{{changeTime}}修改，如非本人操作请联系客服', '/icons/security.svg', '/user/security', 1, '密码修改通知模板', 'changeTime:修改时间', NOW(), NOW(), 'system', 'system', '密码修改通知模板');