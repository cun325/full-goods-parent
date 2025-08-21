/*
 Navicat Premium Data Transfer

 Source Server         : dev
 Source Server Type    : MySQL
 Source Server Version : 80030 (8.0.30)
 Source Host           : localhost:3306
 Source Schema         : goods_shop

 Target Server Type    : MySQL
 Target Server Version : 80030 (8.0.30)
 File Encoding         : 65001

 Date: 23/08/2025 00:10:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `cover` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `image1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `image2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `price` float(10, 2) NULL DEFAULT 0.00,
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `stock` int NULL DEFAULT 0,
  `type_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (1, '牛仔裤', '/images/9.jpg', '/image/cake1_img1.jpg', '/image/cake1_img2.jpg', 10.00, '白色高腰直筒牛仔裤女显瘦2023年秋新款宽松明线窄版阔腿拖地裤子', 10, 1);
INSERT INTO `goods` VALUES (2, '高腰裤', '/images/8.jpg', '/image/cake1_img1.jpg', '/image/cake1_img2.jpg', 12.00, '高腰定制款垂坠感折叠两片金属拉链宽松阔腿裤', 10, 1);
INSERT INTO `goods` VALUES (3, '开衩长裙', '/images/7.jpg', '/image/cake1_img1.jpg', '/image/cake1_img2.jpg', 12.00, '夏季新款女装，别致纯欲设计感褶皱圆领宝藏连衣裙', 10, 1);
INSERT INTO `goods` VALUES (8, '小香风开衫', '/images/1.jpg', '/image/cake2_img1.jpg', '/image/cake2_img2.jpg', 18.00, '小香风大翻领短款开衫羊绒针织衫洋气设计感山茶花上衣', 10, 1);
INSERT INTO `goods` VALUES (9, '方领针织衫', '/images/2.jpg', '/image/qunzi1.jpg', '/image/cake3_img2.jpg', 20.00, '秋冬新款拼色毛衣套头修身型小个子休闲显瘦打底针织衫女', 20, 1);
INSERT INTO `goods` VALUES (10, '卫衣外套', '/images/3.jpg', '/image/cake3_img1.jpg', '/image/cake3_img2.jpg', 20.00, '韩版高级感连帽双拉链外套女2023早秋新款洋气时尚短款上衣', 20, 1);
INSERT INTO `goods` VALUES (11, '毛衣', '/images/4.jpg', '/image/cake3_img1.jpg', '/image/cake3_img2.jpg', 20.00, '2023毛衣冬季加厚高级感针织衫桐乡慵懒风外套扣号特卖专场福利', 20, 1);
INSERT INTO `goods` VALUES (12, '气质小白裙', '/images/5.jpg', '/image/cake3_img1.jpg', '/image/cake3_img2.jpg', 20.00, '小个子法式气质圆领全棉连衣裙女2023夏季新款收腰显瘦中长裙', 20, 1);
INSERT INTO `goods` VALUES (13, '运动套装', '/images/6.jpg', '/image/cake3_img1.jpg', '/image/cake3_img2.jpg', 20.00, '白色针织套装女冬装搭配一整套慵懒休闲连帽毛衣两件套', 20, 1);

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `total` float NULL DEFAULT 0,
  `amount` int NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 0,
  `paytype` tinyint NULL DEFAULT NULL,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `datetime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (1, 10, 2, 0, NULL, 'zs', NULL, NULL, '2023-12-21 10:53:28', NULL);
INSERT INTO `order` VALUES (2, 24, 2, 2, 1, '林与', '13312345678', '南宁', '2024-01-10 21:42:33', 2);
INSERT INTO `order` VALUES (15, 10, 1, 2, 1, '????', '13312345678', NULL, '2024-01-10 17:57:56', 6);
INSERT INTO `order` VALUES (16, 22, 2, 2, 1, '????', '13312345678', NULL, '2024-01-10 18:09:27', 6);
INSERT INTO `order` VALUES (17, 22, 2, 2, 1, '????', '13312345678', NULL, '2024-01-10 21:47:19', 6);
INSERT INTO `order` VALUES (18, 22, 2, 2, 1, '????', '13312345678', NULL, '2024-01-10 21:52:38', 6);
INSERT INTO `order` VALUES (19, 10, 1, 2, 1, '??', '13312345678', NULL, '2024-01-10 21:54:57', 2);
INSERT INTO `order` VALUES (20, 22, 2, 2, 2, '??', '13312345678', NULL, '2024-01-11 19:56:46', 2);
INSERT INTO `order` VALUES (21, 10, 1, 2, 2, '??', '13312345678', NULL, '2024-01-11 20:31:52', 2);

-- ----------------------------
-- Table structure for orderitem
-- ----------------------------
DROP TABLE IF EXISTS `orderitem`;
CREATE TABLE `orderitem`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `price` float(10, 2) NULL DEFAULT NULL,
  `amount` int NULL DEFAULT NULL,
  `goods_id` int NULL DEFAULT NULL,
  `order_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of orderitem
-- ----------------------------
INSERT INTO `orderitem` VALUES (1, 10.00, 1, 1, 15);
INSERT INTO `orderitem` VALUES (2, 10.00, 1, 1, 16);
INSERT INTO `orderitem` VALUES (3, 12.00, 1, 3, 16);
INSERT INTO `orderitem` VALUES (4, 10.00, 1, 1, 17);
INSERT INTO `orderitem` VALUES (5, 12.00, 1, 3, 17);
INSERT INTO `orderitem` VALUES (6, 10.00, 1, 1, 18);
INSERT INTO `orderitem` VALUES (7, 12.00, 1, 3, 18);
INSERT INTO `orderitem` VALUES (8, 10.00, 1, 1, 19);
INSERT INTO `orderitem` VALUES (9, 10.00, 1, 1, 20);
INSERT INTO `orderitem` VALUES (10, 12.00, 1, 3, 20);
INSERT INTO `orderitem` VALUES (11, 10.00, 1, 1, 21);

-- ----------------------------
-- Table structure for recommend
-- ----------------------------
DROP TABLE IF EXISTS `recommend`;
CREATE TABLE `recommend`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` tinyint NULL DEFAULT 0 COMMENT '1条幅推荐2热销推荐3新品推荐',
  `goods_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of recommend
-- ----------------------------
INSERT INTO `recommend` VALUES (1, 2, 1);
INSERT INTO `recommend` VALUES (2, 2, 3);
INSERT INTO `recommend` VALUES (3, 2, 3);
INSERT INTO `recommend` VALUES (4, 2, 9);
INSERT INTO `recommend` VALUES (5, 3, 8);
INSERT INTO `recommend` VALUES (6, 3, 9);
INSERT INTO `recommend` VALUES (7, 3, 10);
INSERT INTO `recommend` VALUES (8, 3, 11);
INSERT INTO `recommend` VALUES (9, 3, 12);

-- ----------------------------
-- Table structure for t_address
-- ----------------------------
DROP TABLE IF EXISTS `t_address`;
CREATE TABLE `t_address`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区/县',
  `detail_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `is_default` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认地址：0-否，1-是',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收货地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_address
-- ----------------------------
INSERT INTO `t_address` VALUES (1, 1, '张三', '13800138000', '北京市', '北京市', '朝阳区', '三里屯街道工体北路8号院', 1, '2025-08-15 23:41:21', NULL);
INSERT INTO `t_address` VALUES (2, 1, '李四', '13900139000', '上海市', '上海市', '浦东新区', '陆家嘴金融贸易区世纪大道100号', 0, '2025-08-15 23:41:21', NULL);
INSERT INTO `t_address` VALUES (3, 1, '王五', '13700137000', '广东省', '深圳市', '南山区', '科技园南区深南大道9988号', 0, '2025-08-15 23:41:21', NULL);
INSERT INTO `t_address` VALUES (4, 13, '黄春红', '111111111111', '重庆市', '重庆市', '江北区', '11111', 1, '2025-08-16 00:11:14', '2025-08-16 00:11:14');
INSERT INTO `t_address` VALUES (5, 2, '1111112', '111', '天津市', '天津市', '河东区', '1111111', 1, '2025-08-16 01:01:25', '2025-08-17 13:49:31');
INSERT INTO `t_address` VALUES (6, 2, '测试', '11111', '黑龙江省', '齐齐哈尔市', '市辖区', '1111', 0, '2025-08-17 13:49:15', '2025-08-17 13:49:25');
INSERT INTO `t_address` VALUES (7, 2, '李明', '13900139001', '上海市', '上海市', '浦东新区', '张江高科技园区科苑路399号', 1, '2025-08-19 04:17:42', NULL);
INSERT INTO `t_address` VALUES (8, 2, '李明', '13900139001', '上海市', '上海市', '浦东新区', '张江高科技园区科苑路399号', 1, '2025-08-20 00:03:56', NULL);
INSERT INTO `t_address` VALUES (9, 3, 'huang', '12876544567', '广西壮族自治区', '南宁市', '县级市', '1234565', 1, '2025-08-22 17:39:09', '2025-08-22 17:39:09');

-- ----------------------------
-- Table structure for t_banner
-- ----------------------------
DROP TABLE IF EXISTS `t_banner`;
CREATE TABLE `t_banner`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
  `image_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '图片URL',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链接URL',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '轮播图表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_banner
-- ----------------------------
INSERT INTO `t_banner` VALUES (1, '/api/images/52efeaba-9d3a-4138-8135-7ed5856eaebc.jpg', '/fruit/detail/1', 1, 1, '2025-08-12 23:15:33', '2025-08-22 18:04:55', NULL, 'admin', NULL);
INSERT INTO `t_banner` VALUES (3, '/api/images/cfaf00ec-8861-4ee2-a4ef-0375870937a5.jpg', '/fruit/detail/3', 3, 0, '2025-08-12 23:15:33', '2025-08-22 18:05:27', NULL, 'admin', NULL);
INSERT INTO `t_banner` VALUES (5, '/api/images/c503f728-8918-4c31-8c3e-6f1265352d59.jpg', '/fruit/detail/2', 2, 1, '2025-08-15 21:10:56', '2025-08-22 18:05:04', NULL, 'admin', NULL);
INSERT INTO `t_banner` VALUES (6, '/api/images/5805b101-5139-4890-b642-204be5573372.jpg', '/fruit/detail/3', 3, 1, '2025-08-15 21:10:56', '2025-08-22 18:05:15', NULL, 'admin', NULL);
INSERT INTO `t_banner` VALUES (9, '/api/images/df502968-1f59-4d59-a059-6b8b5335e575.jpg', '/fruit/detail/4', 4, 1, '2025-08-17 02:42:01', '2025-08-22 18:05:39', 'admin', 'admin', NULL);

-- ----------------------------
-- Table structure for t_cart
-- ----------------------------
DROP TABLE IF EXISTS `t_cart`;
CREATE TABLE `t_cart`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `fruit_id` bigint NOT NULL COMMENT '水果ID',
  `quantity` int NULL DEFAULT 1 COMMENT '数量',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_fruit_id`(`fruit_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_cart
-- ----------------------------
INSERT INTO `t_cart` VALUES (38, 3, 54, 1, '2025-08-23 23:18:28', '2025-08-23 23:18:28', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_customer_service_session
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_service_session`;
CREATE TABLE `t_customer_service_session`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会话ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `service_id` bigint NULL DEFAULT NULL COMMENT '客服ID',
  `session_type` int NOT NULL DEFAULT 1 COMMENT '会话类型：1-智能客服，2-人工客服',
  `status` int NOT NULL DEFAULT 1 COMMENT '会话状态：1-进行中，2-已结束，3-等待中',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会话标题',
  `last_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '最后一条消息内容',
  `last_message_time` datetime NULL DEFAULT NULL COMMENT '最后消息时间',
  `unread_count` int NULL DEFAULT 0 COMMENT '未读消息数',
  `start_time` datetime NULL DEFAULT NULL COMMENT '会话开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '会话结束时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_service_id`(`service_id` ASC) USING BTREE,
  INDEX `idx_session_type`(`session_type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客服会话表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_customer_service_session
-- ----------------------------

-- ----------------------------
-- Table structure for t_flash_sale
-- ----------------------------
DROP TABLE IF EXISTS `t_flash_sale`;
CREATE TABLE `t_flash_sale`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '限时特惠ID',
  `fruit_id` bigint NOT NULL COMMENT '水果ID',
  `original_price` decimal(10, 2) NOT NULL COMMENT '原价',
  `sale_price` decimal(10, 2) NOT NULL COMMENT '特惠价',
  `discount_rate` decimal(5, 2) NULL DEFAULT NULL COMMENT '折扣率（如：0.8表示8折）',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `stock` int NULL DEFAULT 0 COMMENT '特惠库存',
  `sold_count` int NULL DEFAULT 0 COMMENT '已售数量',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_fruit_id`(`fruit_id` ASC) USING BTREE,
  INDEX `idx_start_time`(`start_time` ASC) USING BTREE,
  INDEX `idx_end_time`(`end_time` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '限时特惠表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_flash_sale
-- ----------------------------
INSERT INTO `t_flash_sale` VALUES (1, 1, 39.90, 29.90, 0.75, '2025-08-15 18:58:06', '2025-08-22 18:58:06', 50, 0, 1, '2025-08-15 18:58:06', NULL, NULL, NULL, NULL);
INSERT INTO `t_flash_sale` VALUES (2, 2, 59.90, 45.90, 0.77, '2025-08-15 18:58:06', '2025-08-20 18:58:06', 30, 0, 1, '2025-08-15 18:58:06', NULL, NULL, NULL, NULL);
INSERT INTO `t_flash_sale` VALUES (4, 4, 29.90, 22.90, 0.77, '2025-08-15 18:58:06', '2025-08-21 18:58:06', 25, 0, 1, '2025-08-15 18:58:06', NULL, NULL, NULL, NULL);
INSERT INTO `t_flash_sale` VALUES (6, 6, 29.90, 24.90, 0.83, '2025-08-15 18:58:06', '2025-08-23 18:58:06', 40, 0, 1, '2025-08-15 18:58:06', NULL, NULL, NULL, NULL);
INSERT INTO `t_flash_sale` VALUES (7, 1, 39.90, 29.90, 0.75, '2025-08-15 21:10:56', '2025-08-22 21:10:56', 50, 0, 1, '2025-08-15 21:10:56', NULL, NULL, NULL, NULL);
INSERT INTO `t_flash_sale` VALUES (8, 2, 59.90, 45.90, 0.77, '2025-08-15 21:10:56', '2025-08-20 21:10:56', 30, 0, 1, '2025-08-15 21:10:56', NULL, NULL, NULL, NULL);
INSERT INTO `t_flash_sale` VALUES (10, 4, 29.90, 22.90, 0.77, '2025-08-15 21:10:56', '2025-08-21 21:10:56', 25, 0, 1, '2025-08-15 21:10:56', NULL, NULL, NULL, NULL);
INSERT INTO `t_flash_sale` VALUES (12, 6, 29.90, 24.90, 0.83, '2025-08-15 21:10:56', '2025-08-23 21:10:56', 40, 0, 1, '2025-08-15 21:10:56', NULL, NULL, NULL, NULL);
INSERT INTO `t_flash_sale` VALUES (15, 5, 25.80, 20.00, NULL, '2025-08-22 21:59:07', '2025-08-23 00:00:00', 54, 0, 1, '2025-08-22 21:59:21', NULL, NULL, NULL, NULL);
INSERT INTO `t_flash_sale` VALUES (17, 40, 48.00, 20.00, NULL, '2025-08-23 18:35:26', '2025-08-24 00:00:00', 65, 0, 1, '2025-08-23 18:35:41', NULL, NULL, NULL, NULL);
INSERT INTO `t_flash_sale` VALUES (19, 35, 32.00, 5.00, NULL, '2025-08-23 23:47:52', '2025-08-29 00:00:00', 25, 0, 1, '2025-08-23 23:48:18', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_fruit
-- ----------------------------
DROP TABLE IF EXISTS `t_fruit`;
CREATE TABLE `t_fruit`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '水果ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '水果名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '水果描述',
  `origin` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产地',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位（如：500g/份，1kg/箱）',
  `stock` int NULL DEFAULT 0 COMMENT '库存数量',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片URL',
  `category_id` int NULL DEFAULT NULL COMMENT '分类id（如：热带水果、应季水果等）',
  `taste` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '口感（如：酸甜、香甜等）',
  `nutrition` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '营养成分（如：富含维生素C等）',
  `suitable_crowd` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '适合人群（如：适合孕妇、儿童等）',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `recommended` tinyint(1) NULL DEFAULT 0 COMMENT '推荐状态：0-不推荐，1-推荐',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`name` ASC) USING BTREE,
  INDEX `idx_category`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水果表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_fruit
-- ----------------------------
INSERT INTO `t_fruit` VALUES (1, '新西兰加纳纯苹果', '更新', '新西兰', 99.99, '4个装', 99, '/api/images/648114f6-2807-4ad9-b296-d7011ae4e979.jpg', 4, NULL, NULL, NULL, 1, '2025-08-16 22:33:25', '2025-08-23 21:18:51', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (2, '大泽山玫瑰香葡萄33', '个大饱满，色泽鲜', '大泽山', 59.90, '500g/盒', 80, '/api/images/85b77d95-9c91-4303-863a-8d09512b8661.jpg', 5, '酸甜可口', '富含铁元素和维生素，有补血功效', '适合贫血人群，孕妇适量', 1, '2025-08-10 16:54:57', '2025-08-23 14:42:14', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (3, '大荔冰糖冬枣', '椰肉饱满，椰汁清甜', '新疆', 15.90, '1个', 53, '/api/images/e775665c-c1fe-4c37-979d-b9aea37643a4.jpg', 3, '清甜爽口', '富含多种电解质，有助于补充体力', '适合夏季食用，运动后补充能量', 1, '2025-08-10 16:54:57', '2025-08-23 14:42:17', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (4, '新疆哈密瓜', '果肉厚实，香甜可口', '新疆', 29.90, '1个', 30, '/api/images/a5eb23bb-c1ca-4923-b5c5-45d88714f813.jpg', 6, '香甜多汁', '含有丰富的胡萝卜素，有助于保护视力', '适合大多数人食用，糖尿病人慎食', 1, '2025-08-10 16:54:57', '2025-08-23 14:42:19', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (5, '山东大黄桃', '肉质细腻，汁多味甜', '山东临沂', 25.80, '4个装', 61, '/api/images/21621f13-9ef5-4445-a63e-413bff0aca8a.jpg', 6, '香甜多汁', '富含维生素A和C，有助于美容养颜', '适合大多数人食用，儿童尤佳', 1, '2025-08-10 16:54:57', '2025-08-23 18:32:13', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (6, '广西百香果', '酸甜可口，香气浓郁', '广西', 29.90, '500g/份', 33, '/api/images/f9c385c3-20dc-45cd-914f-31a354fc7e77.jpg', 1, '酸甜可口', '富含维生素C和膳食纤维，有助于消化', '适合便秘人群，孕妇适量', 1, '2025-08-10 16:54:57', '2025-08-23 21:44:15', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (7, '红心芭乐', '新鲜采摘，果大核小，甜度高', '海南', 39.90, '10个', 101, '/api/images/d96c7efe-7106-4058-ba57-722732b9194f.png', 1, '香甜多汁', '富含维生素C，有美容养颜的功效', '适合大多数人食用，孕妇适量', 1, '2025-08-15 21:10:56', '2025-08-23 18:09:39', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (8, '正宗砀山梨', '个大饱满，色泽鲜艳', '山东烟台', 59.90, '500g/盒', 78, '/api/images/531aa582-816a-4048-85d8-159633e422db.jpg', 6, '酸甜可口', '富含铁元素和维生素，有补血功效', '适合贫血人群，孕妇适量', 1, '2025-08-15 21:10:56', '2025-08-23 14:42:38', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (9, '红宝石莲雾', '椰肉饱满，椰汁清甜', '泰国', 15.90, '1个', 49, '/api/images/a3f7b5bf-a419-4403-a87e-e8d152d8111d.jpg', 3, '清甜爽口', '富含多种电解质，有助于补充体力', '适合夏季食用，运动后补充能量', 1, '2025-08-15 21:10:56', '2025-08-23 21:44:15', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (10, '丹东软枣猕猴桃', '果肉厚实，香甜可口', '丹东', 29.90, '1个', 30, '/api/images/81095ca9-36f9-498a-b6b6-32da3f1820a1.jpg', 6, '香甜多汁', '含有丰富的胡萝卜素，有助于保护视力', '适合大多数人食用，糖尿病人慎食', 1, '2025-08-15 21:10:56', '2025-08-22 10:37:21', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (11, '山东大黄桃', '肉质细腻，汁多味甜', '山东临沂', 25.80, '4个装', 60, '/api/images/0fa45c44-fab7-46fd-9e4d-6740e6dc420b.jpg', 6, '香甜多汁', '富含维生素A和C，有助于美容养颜', '适合大多数人食用，儿童尤佳', 1, '2025-08-15 21:10:56', '2025-08-22 10:13:49', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (12, '广西百香果', '酸甜可口，香气浓郁', '广西', 29.90, '500g/份', 40, '/api/images/b217d525-1a24-4617-a6bc-cafa253313ed.jpg', 3, '酸甜可口', '富含维生素C和膳食纤维，有助于消化', '适合便秘人群，孕妇适量', 1, '2025-08-15 21:10:56', '2025-08-22 10:13:25', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (14, '木瓜', NULL, '海南', 99.99, '1个', 100, '/api/images/a9537104-8711-4e53-8e3c-5cfa03463887.jpg', 3, NULL, NULL, NULL, 0, '2025-08-16 22:33:41', '2025-08-22 10:14:27', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (15, '榴莲', NULL, '泰国', 156.00, '1个', 99, '/api/images/76ecf952-b93c-4366-8f96-27690ce7768b.jpg', 3, NULL, NULL, NULL, 1, '2025-08-16 22:33:52', '2025-08-23 18:10:59', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (17, '福建红心蜜柚', NULL, '福建', 13.30, '4个装', 93, '/api/images/214f037d-2caa-403f-b67c-baa8ca779639.jpg', 3, NULL, NULL, NULL, 1, '2025-08-17 17:21:40', '2025-08-23 14:42:34', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (18, '阳光玫瑰葡萄', NULL, '湖北', 100.00, '500g/盒', 3, '/api/images/6fa96b78-4331-4abb-ab6d-bc7f183f62b7.jpg', 6, NULL, NULL, NULL, 1, '2025-08-22 10:12:55', '2025-08-22 10:12:55', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (19, '苹果', NULL, '北方', 19.68, '4个装', 0, '/api/images/da99440e-d18e-4ece-8e13-add6704d187d.jpg', 3, NULL, NULL, NULL, 1, '2025-08-22 10:47:38', '2025-08-22 21:49:24', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (21, '智利进口车厘子', '果大皮厚，色泽红润，甜度高', '智利', 128.00, '500g/盒', 68, '/api/images/b8b78cc2-d94b-4c3f-af03-b923bab00274.jpg', 3, '香甜多汁', '富含花青素和维生素C，抗氧化', '适合上班族、女性美容', 1, '2025-08-20 09:15:22', '2025-08-23 19:07:40', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (22, '海南贵妃芒', '果肉金黄，细腻无渣，香气浓郁', '海南', 36.80, '6个装', 85, '/api/images/9880fded-9064-47b9-8724-5f8c1f1867d8.jpg', 5, '香甜滑嫩', '富含维生素A和胡萝卜素，护眼润肤', '适合儿童、孕妇食用', 1, '2025-08-20 10:20:33', '2025-08-23 21:18:37', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (23, '云南蓝莓', '颗颗饱满，酸甜适中，新鲜采摘', '云南', 45.00, '125g/盒', 70, '/api/images/2afd2cc7-e2bd-4e2c-bb0a-9d77c519a0e4.jpg', 1, '酸甜可口', '富含花青素，增强记忆力', '适合学生、中老年人', 1, '2025-08-20 11:10:44', '2025-08-23 18:37:06', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (24, '泰国金枕榴莲', '果肉金黄，绵密香甜，核小肉厚', '泰国', 168.00, '1个', 25, '/api/images/a8c8320c-e12f-4b9c-bf06-6e8aca9cc622.jpg', 4, '香甜绵密', '富含蛋白质和脂肪，高能量补给', '适合体力消耗大者', 1, '2025-08-20 12:05:55', '2025-08-23 21:18:40', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (25, '四川丑苹果', '外表粗糙，内心清甜，汁水充沛', '四川', 28.80, '5斤装', 90, '/api/images/8798d1e2-50df-4e69-a328-f162f3449afc.jpg', 3, '清甜爽口', '富含果胶，有助于肠道健康', '适合久坐人群、老人', 1, '2025-08-20 13:15:10', '2025-08-23 21:03:45', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (26, '福建琯溪蜜柚', '皮薄肉厚，粒大汁多，酸甜平衡', '福建', 32.00, '4个装', 88, '/api/images/72b3ca34-845f-4e34-8a23-36b2e42fd9a5.jpg', 3, '酸甜可口', '富含天然果酸和维生素P', '适合减脂人群、三高者适量', 1, '2025-08-20 14:22:25', '2025-08-23 21:18:42', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (27, '陕西红富士苹果', '色泽鲜艳，脆甜多汁，耐储存', '陕西', 22.50, '6个装', 120, '/api/images/7g8h9i10-j11k12-l13m14-n15o16-p17q1819s20.jpg', 3, '脆甜多汁', '富含膳食纤维和多种维生素', '适合全家人日常食用', 1, '2025-08-20 15:30:35', '2025-08-20 15:30:35', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (28, '广东荔枝', '晶莹剔透，汁水爆棚，甜而不腻', '广东', 48.00, '500g/盒', 55, '/api/images/c1400d02-3033-4989-bb72-c8a64804bdb0.jpg', 3, '清甜多汁', '富含葡萄糖和维生素C', '适合夏季补能，体寒者少食', 1, '2025-08-20 16:40:40', '2025-08-23 20:03:48', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (29, '云南石林人参果', '外形独特，口感清甜，略带梨香', '云南', 18.00, '1斤', 95, '/api/images/65b1a8b6-dbbc-47c2-9831-498ca67f924f.jpg', 1, '清甜微酸', '富含硒元素和维生素B1', '适合中老年人保健食用', 1, '2025-08-20 17:50:50', '2025-08-23 20:06:44', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (30, '海南椰青', '椰汁清甜，清凉解暑，现摘现发', '海南', 16.80, '1个', 110, '/api/images/31f2f619-74da-496d-bc02-4207c8406b0f.jpg', 3, '清甜解渴', '富含天然电解质，补水佳品', '适合运动后、高温作业者', 1, '2025-08-20 18:00:00', '2025-08-23 20:04:10', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (31, '山东秋月梨', '果肉细腻，汁多无渣，甜润如蜜', '山东', 39.90, '4个装', 75, '/api/images/11k12l13m14-n15o16-p17q18-r19s20-t21u22v23w24.jpg', 6, '香甜多汁', '富含水分和果糖，润肺止咳', '适合干燥季节、吸烟人群', 1, '2025-08-21 09:05:15', '2025-08-21 09:05:15', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (32, '广西武鸣沃柑', '皮薄易剥，酸甜适口，香气扑鼻', '广西', 29.90, '5斤装', 80, '/api/images/ba83d1a5-b517-4efb-b4e8-4fdb0f3e17d2.jpg', 3, '酸甜可口', '富含维生素C和柠檬酸', '适合免疫力低下者', 1, '2025-08-21 10:10:20', '2025-08-23 21:06:16', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (33, '四川攀枝花凯特芒果', '果大核薄，肉质细腻，甜度高', '四川', 56.00, '4个装', 60, '/api/images/13m14n15o16-p17q18-r19s20-t21u22-v23w24x25y26.jpg', 3, '香甜滑润', '富含类胡萝卜素和维生素E', '适合皮肤干燥者', 1, '2025-08-21 11:15:30', '2025-08-21 11:15:30', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (34, '辽宁丹东草莓', '色泽红艳，香气浓郁，入口即化', '辽宁', 68.00, '500g/盒', 40, '/api/images/7efafd71-1045-4f8e-8add-655152fadd60.jpg', 5, '香甜多汁', '富含叶酸和抗氧化物质', '适合孕妇、儿童食用', 1, '2025-08-21 12:20:40', '2025-08-23 21:04:32', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (35, '台湾凤梨', '去皮即食，纤维少，酸甜开胃', '台湾', 32.00, '1个', 50, '/api/images/84f468de-7313-4af0-a62e-f73269d85f4c.jpg', 3, '酸甜可口', '富含菠萝蛋白酶，助消化', '适合饭后食用，胃弱者慎食', 1, '2025-08-21 13:25:50', '2025-08-23 23:46:00', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (36, '江西赣南脐橙', '果大皮薄，汁多味甜，无籽易剥', '江西', 35.00, '5斤装', 85, '/api/images/16p17q18r19-s20t21-u22v23-w24x25-y26z27a28b29.jpg', 3, '香甜多汁', '富含维生素C和橙皮苷', '适合感冒预防、美容养颜', 1, '2025-08-21 14:30:00', '2025-08-21 14:30:00', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (37, '云南昭通苹果', '高原种植，昼夜温差大，甜度高', '云南', 26.80, '5斤装', 90, '/api/images/17q18r19s20-t21u22-v23w24-x25y26-z27a28b29c30.jpg', 3, '脆甜爽口', '富含果糖和矿物质', '适合全家人日常食用', 1, '2025-08-21 15:35:10', '2025-08-21 15:35:10', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (38, '海南红心火龙果', '果肉深红，甜度高，富含花青素', '海南', 42.00, '2个装', 70, '/api/images/18r19s20t21-u22v23-w24x25-y26z27-a28b29c30d31.jpg', 3, '清甜多汁', '富含膳食纤维和植物性蛋白', '适合便秘人群、健身人士', 1, '2025-08-21 16:40:20', '2025-08-21 16:40:20', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (39, '四川凉山盐源苹果', '高原阳光充足，果香浓郁，脆甜可口', '四川', 29.90, '6个装', 88, '/api/images/19s20t21u22-v23w24-x25y26-z27a28-b29c30d31e32.jpg', 3, '脆甜多汁', '富含维生素和矿物质', '适合学生、上班族', 1, '2025-08-21 17:45:30', '2025-08-21 17:45:30', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (40, '广西桂林沙田柚', '果形美观，皮薄肉嫩，甜中带香', '广西', 48.00, '3个装', 64, '/api/images/0545abb9-b373-4e09-878b-4f3c693456d5.jpg', 3, '清甜微香', '富含维生素P和天然果胶', '适合三高人群适量食用', 1, '2025-08-21 18:50:40', '2025-08-23 21:44:15', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (41, '海南香蕉', '自然成熟，甜软适口，易消化', '海南', 12.80, '1把', 150, '/api/images/21u22v23w24-x25y26-z27a28-b29c30-d31e32f33g34.jpg', 3, '香甜软糯', '富含钾元素和维生素B6', '适合运动后补充电解质', 1, '2025-08-22 09:00:00', '2025-08-22 09:00:00', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (42, '宁夏硒砂瓜', '沙漠种植，昼夜温差大，甜度爆表', '宁夏', 38.00, '1个', 45, '/api/images/22v23w24x25-y26z27-a28b29-c30d31-e32f33g34h35.jpg', 6, '沙甜多汁', '富含硒元素，抗氧化', '适合夏季解暑，糖尿病人慎食', 1, '2025-08-22 10:05:10', '2025-08-22 10:05:10', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (43, '福建漳州芦柑', '果香浓郁，酸甜适口，易剥皮', '福建', 24.50, '5斤装', 78, '/api/images/23w24x25y26-z27a28-b29c30-d31e32-f33g34h35i36.jpg', 3, '酸甜开胃', '富含维生素C和有机酸', '适合食欲不振者', 1, '2025-08-22 11:10:20', '2025-08-22 11:10:20', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (44, '新疆库尔勒香梨', '小巧玲珑，皮薄多汁，甜润清香', '新疆', 36.00, '4斤装', 72, '/api/images/24x25y26z27-a28b29-c30d31-e32f33-g34h35i36j37.jpg', 6, '香甜细腻', '富含水分和果胶', '适合干燥季节润燥', 1, '2025-08-22 12:15:30', '2025-08-22 12:15:30', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (45, '广东增城挂绿荔枝', '稀有品种，果肉晶莹，甜中带微香', '广东', 588.00, '500g/礼盒', 5, '/api/images/daa5968e-1db0-4f93-8a15-afb21144b6ea.jpg', 2, '清甜微香', '富含多种氨基酸和矿物质', '适合高端礼品、收藏品尝', 1, '2025-08-22 13:20:40', '2025-08-23 19:47:20', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (46, '四川会理石榴', '籽大汁多，粒粒爆汁，甜而不腻', '四川', 45.00, '4个装', 60, '/api/images/26z27a28b29-c30d31-e32f33-g34h35-i36j37k38l39.jpg', 3, '清甜多汁', '富含鞣花酸，抗氧化', '适合女性美容养颜', 1, '2025-08-22 14:25:50', '2025-08-22 14:25:50', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (47, '陕西周至猕猴桃', '自然成熟，酸甜适口，富含维C', '陕西', 32.00, '10个装', 80, '/api/images/27a28b29c30-d31e32-f33g34-h35i36-j37k38l39m40.jpg', 6, '酸甜可口', '富含维生素C和猕猴桃碱', '适合免疫力低下者', 1, '2025-08-22 15:30:00', '2025-08-22 15:30:00', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (48, '云南大理冰糖心苹果', '高原种植，糖心明显，甜脆可口', '云南', 38.80, '5斤装', 75, '/api/images/28b29c30d31-e32f33-g34h35-i36j37-k38l39m40n41.jpg', 3, '脆甜多汁', '富含果糖和矿物质', '适合全家人食用', 1, '2025-08-22 16:35:10', '2025-08-22 16:35:10', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (49, '海南金钻凤梨', '无需泡盐水，直接食用，香甜不涩', '海南', 39.90, '2个装', 68, '/api/images/e9dc7875-83d7-48f1-912b-d4b3b906236b.jpg', 3, '香甜多汁', '富含菠萝蛋白酶，助消化', '适合饭后食用', 1, '2025-08-22 17:40:20', '2025-08-23 21:00:46', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (50, '甘肃静宁苹果', '黄土高原种植，日照充足，甜脆爽口', '甘肃', 30.00, '6个装', 82, '/api/images/b91ce26e-a747-47a2-b001-e6e73428b186.jpg', 3, '脆甜清香', '富含维生素和膳食纤维', '适合减肥人群、老人', 1, '2025-08-22 18:45:30', '2025-08-23 21:04:03', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (51, '马来西亚猫山王榴莲', '果王之王，核小肉厚，浓郁绵密', '马来西亚', 298.00, '1个', 12, '/api/images/bb6c357f-b017-4f31-9aa0-15e2fd6ba72d.jpg', 3, '香甜浓郁', '富含优质脂肪和矿物质', '适合榴莲爱好者、能量补充', 1, '2025-08-23 09:00:00', '2025-08-23 20:34:04', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (52, '四川汉源甜樱桃', '高原樱桃，果硬汁多，甜度高', '四川', 88.00, '500g/盒', 35, '/api/images/21718221-9559-4b1d-b200-f3cd961d3f41.png', 5, '脆甜多汁', '富含铁和维生素C', '适合贫血人群、女性食用', 1, '2025-08-23 10:05:10', '2025-08-23 22:11:36', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (53, '湖南麻阳冰糖橙', '皮薄肉厚，甜如冰糖，无籽多汁', '湖南', 28.00, '5斤装', 80, '/api/images/ae6d3621-e126-4a5f-a136-727c356333ff.jpg', 3, '香甜如蜜', '富含维生素C和果糖', '适合儿童、老人食用', 1, '2025-08-23 11:10:20', '2025-08-23 21:04:16', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (54, '贵州刺梨', '野果珍品，酸中带甜，维C之王', '贵州', 58.00, '500g/份', 40, '/api/images/2bbece22-5313-4010-9bbf-9ba973e19146.jpg', 3, '酸甜独特', '富含维生素C（是柠檬10倍）', '适合免疫力低下者', 1, '2025-08-23 12:15:30', '2025-08-23 20:58:44', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (55, '浙江慈溪杨梅', '紫黑发亮，汁水丰盈，酸甜开胃', '浙江', 68.00, '500g/盒', 50, '/api/images/fa203aa0-7505-4d6e-8401-de25cb4dadd9.jpg', 3, '酸甜可口', '富含有机酸和花青素', '适合夏季消暑、助消化', 1, '2025-08-23 13:20:40', '2025-08-23 20:01:12', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (56, '山东烟台大樱桃', '果大如珠，色泽红润，脆甜多汁', '山东', 98.00, '500g/盒', 45, '/api/images/57a9f45e-e033-4654-b77e-d70d6a8626b8.jpg', 5, '脆甜爽口', '富含铁和维生素E', '适合女性补血养颜', 1, '2025-08-23 14:00:00', '2025-08-23 21:38:23', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (57, '11', '测试', '老表', 0.03, '盒/5', 99991, '/api/images/d9378be4-6d6d-4629-9378-07cdc6530ab8.jpg', 1, NULL, NULL, NULL, 1, '2025-08-23 18:46:23', '2025-08-23 21:44:15', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (58, 'ceshi', '西奥和', '广西老表', 0.01, '500g/盒', 9996, '/api/images/2754559c-b169-4f83-8e1b-145a2b15b846.jpg', 5, NULL, NULL, NULL, 1, '2025-08-23 19:09:39', '2025-08-23 21:18:20', NULL, NULL, NULL, 1);
INSERT INTO `t_fruit` VALUES (59, '苹果2', '口味酸酸甜甜的，可以作为送礼的礼品 ', '广西', 10.70, '4个装', 1, '/api/images/e4fa5f8c-d966-40fc-ba7e-97ac8ad4ddfe.jpg', 2, NULL, NULL, NULL, 1, '2025-08-23 21:11:28', '2025-08-23 21:44:15', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (60, '苹果3', '脆脆的，颜色鲜艳', '广东', 100.00, '盒/5', 2, '/api/images/93169e09-af29-4310-a8d2-4402b6fbb028.jpg', 5, NULL, NULL, NULL, 1, '2025-08-23 21:35:00', '2025-08-23 21:35:00', NULL, NULL, NULL, 0);
INSERT INTO `t_fruit` VALUES (61, '苹果4', '南方特色苹果', '广东', 0.60, '10个', 1, '/api/images/a8fcc693-51d4-4d4b-826d-8235ba3aa2b1.jpg', 5, NULL, NULL, NULL, 1, '2025-08-23 21:42:36', '2025-08-23 22:10:56', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_fruit_category
-- ----------------------------
DROP TABLE IF EXISTS `t_fruit_category`;
CREATE TABLE `t_fruit_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `icon_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标名称（Element Plus图标）',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标URL',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序顺序',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_sort_order`(`sort_order` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水果分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_fruit_category
-- ----------------------------
INSERT INTO `t_fruit_category` VALUES (1, '进口水果', 'Apple', '/api/images/f49dfa14-699a-4e4a-92f8-533a38638781.jpg', 1, 1, '2025-08-15 19:08:32', '2025-08-22 11:07:56', 'system', NULL, NULL);
INSERT INTO `t_fruit_category` VALUES (2, '当季水果', 'Cherry', '/api/images/1f6e1f96-f0bb-4b4c-9bdc-7f23ca5bed37.jpg', 2, 1, '2025-08-15 19:08:32', '2025-08-22 11:08:10', 'system', NULL, NULL);
INSERT INTO `t_fruit_category` VALUES (3, '热带水果', 'Watermelon', '/api/images/360527ee-289e-4ac8-9ca9-40eec899ae87.png', 3, 1, '2025-08-15 19:08:32', '2025-08-22 11:08:23', 'system', NULL, NULL);
INSERT INTO `t_fruit_category` VALUES (4, '柑橘类', 'Orange', '/api/images/b774024c-6547-4f17-a02c-c3e6e025be3d.jpg', 4, 1, '2025-08-15 19:08:32', '2025-08-22 11:09:03', 'system', NULL, NULL);
INSERT INTO `t_fruit_category` VALUES (5, '礼盒09', 'Present', '/api/images/128906ca-09ba-454c-b5f2-0c3b81af4103.jpg', 5, 1, '2025-08-15 19:08:32', '2025-08-23 00:32:39', 'system', NULL, NULL);
INSERT INTO `t_fruit_category` VALUES (6, '应季水果', 'Cherry', '/api/images/89e6f2e0-ae38-4091-b9dd-b68eb041dc17.jpg', 6, 0, '2025-08-15 19:08:32', '2025-08-22 11:10:21', 'system', NULL, NULL);

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容',
  `message_type` int NOT NULL DEFAULT 1 COMMENT '消息类型：1-物流，2-客服，3-系统，4-优惠',
  `status` int NOT NULL DEFAULT 0 COMMENT '消息状态：0-未读，1-已读',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联订单号',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标URL',
  `link_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跳转链接',
  `sender_id` bigint NULL DEFAULT NULL COMMENT '发送者ID',
  `sender_type` int NULL DEFAULT NULL COMMENT '发送者类型：1-系统，2-客服，3-用户',
  `template_id` bigint NULL DEFAULT NULL COMMENT '消息模板ID',
  `extra_data` json NULL COMMENT '扩展数据',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_message_type`(`message_type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_message
-- ----------------------------
INSERT INTO `t_message` VALUES (1, 3, '您的订单已发货', '亲爱的用户，您的订单 175527729450262e9cb 已发货，快递单号：SF1234567890，预计2-3个工作日送达。请保持手机畅通，注意查收。', 1, 0, 'ORD202412251001', '/icons/shipping.svg', '/order/detail/ORD202412251001', 1, 1, NULL, NULL, '2025-08-19 02:17:42', '2025-08-22 17:37:34', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (2, 3, '包裹正在派送中', '您的包裹（快递单号：SF1234567890）正在派送中，快递员将在今日下午送达，请保持手机畅通。如有疑问请联系快递员：13800138888。', 1, 1, 'ORD202412251001', '/icons/shipping.svg', '/order/detail/ORD202412251001', 1, 1, NULL, NULL, '2025-08-19 03:17:42', '2025-08-22 17:37:18', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (3, 3, '订单已签收', '您的订单 ORD202412251002 已成功签收，感谢您的购买！如对商品有任何问题，请及时联系客服。期待您的再次光临！', 1, 0, 'ORD202412251002', '/icons/shipping.svg', '/order/detail/ORD202412251002', 1, 1, NULL, NULL, '2025-08-18 04:17:42', '2025-08-23 23:50:04', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (4, 2, '客服回复：关于退换货问题', '您好！关于您咨询的退换货问题，我们支持7天无理由退换货。请您在收货后7天内，保持商品原包装完好，联系客服办理退换货手续。退货邮费由我们承担。', 2, 0, NULL, '/icons/customer-service.svg', '/customer-service', 1, 1, NULL, NULL, '2025-08-19 01:17:42', '2025-08-22 17:37:42', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (5, 3, '客服回复：商品咨询', '您好！您咨询的海南妃子笑荔枝，我们采用冷链运输，保证新鲜度。建议收货后尽快食用，常温下可保存2-3天，冷藏可保存5-7天。', 2, 0, NULL, '/icons/customer-service.svg', '/customer-service', 1, 1, NULL, NULL, '2025-08-18 23:17:42', '2025-08-22 17:37:27', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (6, 2, '客服回复：订单问题已解决', '您好！您反馈的订单问题我们已经处理完毕。相关费用已退回到您的原支付账户，请注意查收。如还有其他问题，随时联系我们。', 2, 1, NULL, '/icons/customer-service.svg', '/customer-service', 1, 1, NULL, NULL, '2025-08-17 04:17:42', '2025-08-19 04:23:09', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (7, 2, '账户安全提醒', '您的账户在新设备上登录，登录时间：2024-12-25 14:30，登录地点：北京市朝阳区。如非本人操作，请及时修改密码并联系客服。', 3, 0, NULL, '/icons/system.svg', '/system/notices', 1, 1, NULL, NULL, '2025-08-19 03:47:42', '2025-08-19 04:23:09', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (8, 2, '系统维护通知', '尊敬的用户，我们将于今晚23:00-次日01:00进行系统维护升级，期间可能影响部分功能使用。维护完成后系统将更加稳定，感谢您的理解与支持！', 3, 0, NULL, '/icons/system.svg', '/system/notices', 1, 1, NULL, NULL, '2025-08-19 00:17:42', '2025-08-19 04:23:09', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (9, 2, '新功能上线通知', '好消息！我们的积分商城功能已正式上线，您可以使用积分兑换精美礼品。快去积分商城看看吧！首次使用还有额外积分奖励哦~', 3, 1, NULL, '/icons/system.svg', '/system/notices', 1, 1, NULL, NULL, '2025-08-18 04:17:42', '2025-08-19 04:23:09', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (10, 3, '优惠活动通知', '限时特惠！精选水果5折起，满99元免邮费！活动时间：12月25日-12月31日，数量有限，先到先得。点击查看活动详情。', 3, 0, NULL, '/icons/system.svg', '/system/notices', 1, 1, NULL, NULL, '2025-08-18 22:17:42', '2025-08-22 17:37:30', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (11, 2, '您的订单已发货', '您的订单 ORD20240101001 已从仓库发出，预计2-3个工作日内送达。', 1, 0, 'ORD20240101001', 'https://example.com/icons/logistics.png', '/order/detail/ORD20240101001', 1, 1, NULL, NULL, '2025-08-19 04:27:54', '2025-08-23 23:50:06', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (12, 2, '订单配送中', '您的订单 ORD20240101002 正在配送途中，快递员将在今日下午联系您。', 1, 1, 'ORD20240101002', 'https://example.com/icons/delivery.png', '/order/detail/ORD20240101002', 1, 1, NULL, NULL, '2025-08-19 04:27:54', '2025-08-19 04:53:13', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (13, 2, '订单已签收', '您的订单 ORD20240101003 已成功签收，感谢您的购买！', 1, 1, 'ORD20240101003', 'https://example.com/icons/received.png', '/order/detail/ORD20240101003', 1, 1, NULL, NULL, '2025-08-19 04:27:54', '2025-08-19 04:27:54', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (14, 3, '客服回复', '您好，关于您咨询的商品问题，我们已为您详细解答。', 2, 1, NULL, 'https://example.com/icons/customer-service.png', '/chat/123', 101, 3, NULL, NULL, '2025-08-19 04:27:54', '2025-08-23 23:49:47', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (15, 2, '在线客服', '欢迎使用我们的在线客服系统，有任何问题请随时咨询。', 2, 1, NULL, 'https://example.com/icons/chat.png', '/chat', 100, 2, NULL, NULL, '2025-08-19 04:27:54', '2025-08-19 04:27:54', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (16, 2, '系统维护通知', '系统将于今晚23:00-01:00进行维护升级，期间可能影响部分功能使用。', 3, 1, NULL, 'https://example.com/icons/maintenance.png', '/notice/maintenance', 1, 1, NULL, NULL, '2025-08-19 04:27:54', '2025-08-19 04:54:50', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (17, 2, '新功能上线', '我们上线了全新的积分商城功能，快来体验吧！', 3, 1, NULL, 'https://example.com/icons/new-feature.png', '/points-mall', 1, 1, NULL, NULL, '2025-08-19 04:27:54', '2025-08-19 05:08:45', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (18, 2, '账户安全提醒', '检测到您的账户在新设备上登录，如非本人操作请及时修改密码。', 3, 1, NULL, 'https://example.com/icons/security.png', '/account/security', 1, 1, NULL, NULL, '2025-08-19 04:27:54', '2025-08-19 04:27:54', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (19, 2, '客服咨询', '优惠活动', 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 05:06:58', '2025-08-19 05:06:58', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (20, 2, '客服咨询', '配送问题', 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 05:07:11', '2025-08-19 05:07:11', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (21, 2, '客服咨询', '订单查询', 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 05:07:21', '2025-08-19 05:07:21', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (22, 2, '客服咨询', '退换货', 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 05:07:28', '2025-08-19 05:07:28', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (23, 2, '客服咨询', '优惠活动', 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 05:07:37', '2025-08-19 05:07:37', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (24, 2, '客服咨询', '你是傻子', 2, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 05:09:43', '2025-08-19 05:10:57', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (25, 2, '客服咨询', '黄春红是傻子', 2, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 05:09:54', '2025-08-19 05:10:45', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (26, 2, '客服咨询', '邬鹏也是', 2, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 05:10:14', '2025-08-19 19:44:05', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (27, 2, '订单已发货', '您的订单ORD202412251001已发货，快递单号：SF1234567890，预计明日送达。包含：海南妃子笑荔枝、新疆哈密瓜、泰国椰青', 1, 1, 'ORD202412251001', NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-18 05:29:35', '2025-08-19 05:32:50', 'system', 'system', NULL);
INSERT INTO `t_message` VALUES (28, 2, '订单正在配送中', '您的订单ORD202412251002正在配送中，快递单号：YTO9876543210，预计今日送达，请保持手机畅通。包含：山东大樱桃、广西百香果、泰国椰青', 1, 0, 'ORD202412251002', NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-18 23:29:35', '2025-08-19 05:29:35', 'system', 'system', NULL);
INSERT INTO `t_message` VALUES (29, 2, '订单已送达', '您的订单ORD202412251003已成功送达，感谢您的购买！如有问题请联系客服。包含：山东大黄桃、海南妃子笑荔枝', 1, 1, 'ORD202412251003', NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-16 05:29:35', '2025-08-19 05:29:35', 'system', 'system', NULL);
INSERT INTO `t_message` VALUES (30, 2, '包裹到达配送站', '您的订单ORD202412251002包裹已到达深圳南山配送站，正在安排配送，预计2小时内送达', 1, 1, 'ORD202412251002', NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 03:29:35', '2025-08-19 05:33:06', 'system', 'system', NULL);
INSERT INTO `t_message` VALUES (31, 1, '测试消息', '这是一条测试消息', 3, 0, NULL, '/test.png', '/test', NULL, NULL, NULL, NULL, '2025-08-19 19:49:26', '2025-08-19 19:49:26', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (32, 1, '修复后测试消息', '这是修复空指针异常后的测试消息', 3, 0, NULL, 'https://example.com/icon.png', 'https://example.com', NULL, NULL, NULL, NULL, '2025-08-19 19:54:07', '2025-08-19 19:54:07', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (33, 1, '测试系统消息', '这是一条测试系统消息', 3, 0, NULL, 'https://example.com/icon.png', 'https://example.com', NULL, NULL, NULL, NULL, '2025-08-19 19:56:56', '2025-08-19 19:56:56', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (34, 1, '测试优化后的系统消息', '这是优化后的系统消息测试', 3, 0, NULL, 'https://example.com/icon.png', 'https://example.com', NULL, NULL, NULL, NULL, '2025-08-19 20:24:08', '2025-08-19 20:24:08', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (35, 2, '客服回复：客服咨询', '测试API修复', 4, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 22:55:26', '2025-08-19 22:55:26', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (36, 2, '客服回复：客服咨询', '最终测试API', 4, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 22:57:09', '2025-08-19 22:57:09', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (37, 2, '客服回复：客服咨询', '测试重启后的功能', 4, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 22:59:03', '2025-08-19 22:59:03', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (38, 2, '您的订单已发货', '亲爱的用户，您的订单 ORD202412251001 已发货，快递单号：SF1234567890，预计2-3个工作日送达。请保持手机畅通，注意查收。', 1, 0, 'ORD202412251001', NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 22:03:56', '2025-08-19 22:03:56', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (39, 2, '包裹正在派送中', '您的包裹（快递单号：SF1234567890）正在派送中，快递员将在今日下午送达，请保持手机畅通。如有疑问请联系快递员：13800138888。', 1, 0, 'ORD202412251001', NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 23:03:56', '2025-08-19 23:03:56', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (40, 2, '订单已签收', '您的订单 ORD202412251002 已成功签收，感谢您的购买！如对商品有任何问题，请及时联系客服。期待您的再次光临！', 1, 1, 'ORD202412251002', NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 00:03:56', '2025-08-19 00:03:56', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (41, 2, '客服回复：关于退换货问题', '您好！关于您咨询的退换货问题，我们支持7天无理由退换货。请您在收货后7天内，保持商品原包装完好，联系客服办理退换货手续。退货邮费由我们承担。', 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 21:03:56', '2025-08-19 21:03:56', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (42, 2, '客服回复：商品咨询', '您好！您咨询的海南妃子笑荔枝，我们采用冷链运输，保证新鲜度。建议收货后尽快食用，常温下可保存2-3天，冷藏可保存5-7天。', 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-19 19:03:56', '2025-08-19 19:03:56', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (43, 2, '客服回复：订单问题已解决', '您好！您反馈的订单问题我们已经处理完毕。相关费用已退回到您的原支付账户，请注意查收。如还有其他问题，随时联系我们。', 2, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-18 00:03:56', '2025-08-18 00:03:56', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (44, 2, '账户安全提醒', '您的账户在新设备上登录，登录时间：2024-12-25 14:30，登录地点：北京市朝阳区。如非本人操作，请及时修改密码并联系客服。', 3, 0, NULL, NULL, '/profile', NULL, NULL, NULL, NULL, '2025-08-19 23:33:56', '2025-08-19 23:33:56', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (45, 2, '系统维护通知', '尊敬的用户，我们将于今晚23:00-次日01:00进行系统维护升级，期间可能影响部分功能使用。维护完成后系统将更加稳定，感谢您的理解与支持！', 3, 0, NULL, NULL, '/settings', NULL, NULL, NULL, NULL, '2025-08-19 20:03:56', '2025-08-19 20:03:56', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (46, 2, '新功能上线通知', '好消息！我们的积分商城功能已正式上线，您可以使用积分兑换精美礼品。快去积分商城看看吧！首次使用还有额外积分奖励哦~', 3, 1, NULL, NULL, '/home', NULL, NULL, NULL, NULL, '2025-08-19 00:03:56', '2025-08-19 00:03:56', NULL, NULL, NULL);
INSERT INTO `t_message` VALUES (47, 2, '优惠活动通知', '限时特惠！精选水果5折起，满99元免邮费！活动时间：12月25日-12月31日，数量有限，先到先得。点击查看活动详情。', 3, 0, NULL, NULL, '/home', NULL, NULL, NULL, NULL, '2025-08-19 18:03:56', '2025-08-19 18:03:56', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_message_template
-- ----------------------------
DROP TABLE IF EXISTS `t_message_template`;
CREATE TABLE `t_message_template`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板名称',
  `message_type` int NOT NULL COMMENT '消息类型：1-订单通知，2-物流通知，3-系统通知，4-客服消息，5-优惠活动，6-支付通知，7-库存预警，8-用户相关',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息标题模板',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容模板',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标URL',
  `link_template` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跳转链接模板',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模板描述',
  `params_desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数说明',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE,
  INDEX `idx_message_type`(`message_type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_message_template
-- ----------------------------
INSERT INTO `t_message_template` VALUES (1, 'ORDER_SHIPPED_FIXED', '订单发货通知_测试更新', 2, '您的订单已发货_测试更新', '测试数据库更新操作的内容', '/icons/shipping.svg', '/order/tracking/{{trackingNo}}', 1, '测试数据库更新操作', 'orderNo:订单号, trackingNo:快递单号', '2025-08-19 02:39:41', '2025-08-19 21:19:33', 'system', 'system', '订单发货通知模板');
INSERT INTO `t_message_template` VALUES (5, 'PROMOTION_ACTIVITY', '限时优惠活动', 5, '限时优惠开始', '{{activityName}}活动开始啦！{{discount}}优惠等你来抢，活动截止{{endTime}}', '/icons/promotion.svg', '/promotion/{{activityId}}', 1, '优惠活动开始通知模板', 'activityName:活动名称, discount:优惠信息, endTime:结束时间, activityId:活动ID', '2025-08-19 02:39:41', '2025-08-19 02:39:41', 'system', 'system', '优惠活动通知模板');
INSERT INTO `t_message_template` VALUES (6, 'NEW_USER_COUPON', '新人优惠券', 5, '新人优惠券已到账', '欢迎使用鲜果云商！新人专享优惠券已发放到您的账户，快去使用吧！', '/icons/coupon.svg', '/user/coupons', 1, '新用户优惠券到账通知模板', '', '2025-08-19 02:39:41', '2025-08-19 02:39:41', 'system', 'system', '新人优惠券模板');
INSERT INTO `t_message_template` VALUES (54, 'ORDER_SHIPPED', '订单发货通知', 1, '{{title}}', '{{content}}', '/images/notification.png', '/orders/{{orderId}}', 1, '模板描述', '{\"title\":\"标题\",\"content\":\"内容\",\"orderId\":\"订单ID\"}', '2025-08-19 18:01:17', '2025-08-19 18:01:37', NULL, NULL, '系统模板');
INSERT INTO `t_message_template` VALUES (55, 'TEST_DB_CONNECTION', '数据库连接测试', 1, '测试标题', '这是一个测试消息内容', NULL, NULL, 1, '用于测试数据库连接的模板', NULL, '2025-08-19 21:19:08', '2025-08-19 21:19:08', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10, 2) NOT NULL COMMENT '实付金额',
  `freight_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '运费',
  `discount_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '优惠金额',
  `pay_type` tinyint NULL DEFAULT NULL COMMENT '支付方式：1-微信，2-支付宝',
  `status` tinyint NULL DEFAULT 0 COMMENT '订单状态：0-待付款，1-待发货，2-待收货，3-已完成，4-已取消',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '收货时间',
  `comment_time` datetime NULL DEFAULT NULL COMMENT '评价时间',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `receiver_province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `receiver_city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `receiver_district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区/县',
  `receiver_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `tracking_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '快递单号',
  `courier` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '快递公司',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES (1, '17552748650331d3bf7', 13, 59.90, 59.90, 0.00, 0.00, NULL, 2, '2025-08-17 02:11:57', NULL, NULL, NULL, '黄春红', '111111111111', '重庆市', '重庆市', '江北区', '11111', NULL, '2025-08-16 00:21:05', '2025-08-16 00:21:05', NULL, NULL, NULL, 'SF123456789', '顺丰快递');
INSERT INTO `t_order` VALUES (2, '17552749505268a4d87', 13, 59.90, 59.90, 0.00, 0.00, NULL, 1, NULL, NULL, NULL, NULL, '黄春红', '111111111111', '重庆市', '重庆市', '江北区', '11111', NULL, '2025-08-16 00:22:31', '2025-08-16 00:22:31', NULL, NULL, NULL, 'YTO987654321', '圆通快递');
INSERT INTO `t_order` VALUES (3, '1755275135960051f47', 13, 59.90, 59.90, 0.00, 0.00, NULL, 2, NULL, NULL, NULL, NULL, '黄春红', '111111111111', '重庆市', '重庆市', '江北区', '11111', NULL, '2025-08-16 00:25:36', '2025-08-16 00:25:36', NULL, NULL, NULL, '11111', '圆通速递');
INSERT INTO `t_order` VALUES (4, '17552752070192169aa', 13, 59.90, 59.90, 0.00, 0.00, NULL, 4, NULL, NULL, NULL, NULL, '黄春红', '111111111111', '重庆市', '重庆市', '江北区', '11111', NULL, '2025-08-16 00:26:47', '2025-08-22 10:09:09', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_order` VALUES (5, '17552757816599120d8', 13, 85.70, 85.70, 0.00, 0.00, NULL, 4, NULL, NULL, NULL, NULL, '黄春红', '111111111111', '重庆市', '重庆市', '江北区', '11111', NULL, '2025-08-16 00:36:22', '2025-08-22 10:09:09', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_order` VALUES (6, '175527729450262e9cb', 2, 59.90, 59.90, 0.00, 0.00, NULL, 3, NULL, NULL, '2025-08-16 02:32:43', NULL, '111111', '111', '上海市', '上海市', '普陀区', '1111111', NULL, '2025-08-16 01:01:35', '2025-08-16 02:32:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_order` VALUES (7, '1755281169788994413', 2, 59.90, 59.90, 0.00, 0.00, NULL, 4, NULL, NULL, NULL, NULL, '111111', '111', '上海市', '上海市', '普陀区', '1111111', NULL, '2025-08-16 02:06:10', '2025-08-16 02:33:00', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_order` VALUES (8, '1755282107411183054', 2, 139.70, 139.70, 0.00, 0.00, NULL, 1, NULL, NULL, NULL, NULL, '111111', '111', '上海市', '上海市', '普陀区', '1111111', NULL, '2025-08-16 02:21:47', '2025-08-16 02:21:47', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_order` VALUES (9, '17555523739897bbe28', 2, 87.60, 87.60, 0.00, 0.00, NULL, 4, NULL, NULL, NULL, NULL, '1111112', '111', '天津市', '天津市', '河东区', '1111111', NULL, '2025-08-19 05:26:14', '2025-08-22 10:09:09', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_order` VALUES (13, 'ORD202412251001', 2, 99.80, 99.80, 10.00, 0.00, 1, 2, '2025-08-17 05:29:35', '2025-08-18 05:29:35', NULL, NULL, '张三', '13800138001', '广东省', '深圳市', '南山区', '科技园南区深南大道1001号', '请尽快发货', '2025-08-16 05:29:35', '2025-08-19 05:29:35', NULL, NULL, NULL, 'SF1234567890', '顺丰速运');
INSERT INTO `t_order` VALUES (14, 'ORD202412251002', 2, 119.70, 119.70, 10.00, 0.00, 2, 2, '2025-08-18 05:29:35', '2025-08-18 23:29:35', NULL, NULL, '张三', '13800138001', '广东省', '深圳市', '南山区', '科技园南区深南大道1001号', '水果要新鲜', '2025-08-17 05:29:35', '2025-08-19 05:29:35', NULL, NULL, NULL, 'YTO9876543210', '圆通速递');
INSERT INTO `t_order` VALUES (15, 'ORD202412251003', 2, 75.60, 75.60, 10.00, 0.00, 1, 3, '2025-08-14 05:29:35', '2025-08-16 05:29:35', NULL, NULL, '张三', '13800138001', '广东省', '深圳市', '南山区', '科技园南区深南大道1001号', '包装要仔细', '2025-08-13 05:29:35', '2025-08-19 05:29:35', NULL, NULL, NULL, 'ZTO5555666677', '中通快递');
INSERT INTO `t_order` VALUES (16, '1755855554098c3d2dd', 3, 99.99, 99.99, 0.00, 0.00, NULL, 4, NULL, NULL, NULL, NULL, 'huang', '12876544567', '广西壮族自治区', '南宁市', '县级市', '1234565', NULL, '2025-08-22 17:39:14', '2025-08-22 17:54:47', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_order` VALUES (17, '1755864260116a7db1e', 3, 99.99, 99.99, 0.00, 0.00, NULL, 4, NULL, NULL, NULL, NULL, 'huang', '12876544567', '广西壮族自治区', '南宁市', '县级市', '1234565', NULL, '2025-08-22 20:04:20', '2025-08-22 20:20:13', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_order` VALUES (18, '17558705645881b686c', 3, 624.65, 624.65, 0.00, 0.00, 1, 2, '2025-08-22 21:49:29', NULL, NULL, NULL, 'huang', '12876544567', '广西壮族自治区', '南宁市', '县级市', '1234565', NULL, '2025-08-22 21:49:25', '2025-08-22 21:49:29', NULL, NULL, NULL, '1234567', '顺丰速运');
INSERT INTO `t_order` VALUES (19, '175588259531100b01b', 3, 59.80, 59.80, 0.00, 0.00, 1, 1, '2025-08-23 01:10:05', NULL, NULL, NULL, 'huang', '12876544567', '广西壮族自治区', '南宁市', '县级市', '1234565', NULL, '2025-08-23 01:09:55', '2025-08-23 01:10:05', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_order` VALUES (20, '1755883535333df0173', 3, 29.90, 29.90, 0.00, 0.00, 1, 1, '2025-08-23 01:25:40', NULL, NULL, NULL, 'huang', '12876544567', '广西壮族自治区', '南宁市', '县级市', '1234565', NULL, '2025-08-23 01:25:35', '2025-08-23 01:25:40', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_order` VALUES (21, '17559474476280a55c9', 3, 0.03, 0.03, 0.00, 0.00, 1, 2, '2025-08-23 19:10:53', NULL, NULL, NULL, 'huang', '12876544567', '广西壮族自治区', '南宁市', '县级市', '1234565', NULL, '2025-08-23 19:10:48', '2025-08-23 19:10:53', NULL, NULL, NULL, 'SF1234567890123', '顺丰速运');
INSERT INTO `t_order` VALUES (22, '1755954737010303165', 3, 10.70, 10.70, 0.00, 0.00, 2, 1, '2025-08-23 21:12:24', NULL, NULL, NULL, 'huang', '12876544567', '广西壮族自治区', '南宁市', '县级市', '1234565', NULL, '2025-08-23 21:12:17', '2025-08-23 21:12:24', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_order` VALUES (23, '1755956655735a83481', 3, 106.93, 106.93, 0.00, 0.00, 2, 1, '2025-08-23 21:44:22', NULL, NULL, NULL, 'huang', '12876544567', '广西壮族自治区', '南宁市', '县级市', '1234565', NULL, '2025-08-23 21:44:16', '2025-08-23 21:44:22', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_order_item
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item`;
CREATE TABLE `t_order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `fruit_id` bigint NOT NULL COMMENT '水果ID',
  `fruit_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '水果名称',
  `fruit_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '水果图片',
  `price` decimal(10, 2) NOT NULL COMMENT '单价',
  `quantity` int NOT NULL COMMENT '数量',
  `total_price` decimal(10, 2) NOT NULL COMMENT '总价',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_fruit_id`(`fruit_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_order_item
-- ----------------------------
INSERT INTO `t_order_item` VALUES (1, 1, '17552748650331d3bf7', 2, '山东大樱桃', '/images?url=/fruits/cherry.jpg', 59.90, 1, 59.90, '2025-08-16 00:21:05', '2025-08-16 00:21:05');
INSERT INTO `t_order_item` VALUES (2, 2, '17552749505268a4d87', 2, '山东大樱桃', '/images?url=/fruits/cherry.jpg', 59.90, 1, 59.90, '2025-08-16 00:22:31', '2025-08-16 00:22:31');
INSERT INTO `t_order_item` VALUES (3, 3, '1755275135960051f47', 2, '山东大樱桃', '/images?url=/fruits/cherry.jpg', 59.90, 1, 59.90, '2025-08-16 00:25:36', '2025-08-16 00:25:36');
INSERT INTO `t_order_item` VALUES (4, 4, '17552752070192169aa', 2, '山东大樱桃', '/images?url=/fruits/cherry.jpg', 59.90, 1, 59.90, '2025-08-16 00:26:47', '2025-08-16 00:26:47');
INSERT INTO `t_order_item` VALUES (5, 5, '17552757816599120d8', 2, '山东大樱桃', 'fruits/cherry.jpg', 59.90, 1, 59.90, '2025-08-16 00:36:22', '2025-08-16 00:36:22');
INSERT INTO `t_order_item` VALUES (6, 5, '17552757816599120d8', 5, '山东大黄桃', 'fruits/peach.jpg', 25.80, 1, 25.80, '2025-08-16 00:36:22', '2025-08-16 00:36:22');
INSERT INTO `t_order_item` VALUES (7, 6, '175527729450262e9cb', 2, '山东大樱桃', '/images?url=/fruits/cherry.jpg', 59.90, 1, 59.90, '2025-08-16 01:01:35', '2025-08-16 01:01:35');
INSERT INTO `t_order_item` VALUES (8, 7, '1755281169788994413', 2, '山东大樱桃', '/images?url=/fruits/cherry.jpg', 59.90, 1, 59.90, '2025-08-16 02:06:10', '2025-08-16 02:06:10');
INSERT INTO `t_order_item` VALUES (9, 8, '1755282107411183054', 7, '海南妃子笑荔枝', 'fruits/lychee.jpg', 39.90, 2, 79.80, '2025-08-16 02:21:47', '2025-08-16 02:21:47');
INSERT INTO `t_order_item` VALUES (10, 8, '1755282107411183054', 2, '山东大樱桃', 'fruits/cherry.jpg', 59.90, 1, 59.90, '2025-08-16 02:21:47', '2025-08-16 02:21:47');
INSERT INTO `t_order_item` VALUES (11, 9, '17555523739897bbe28', 3, '泰国椰青', '/uploads/images/2025/08/17/459f0e18-d995-4e79-9b51-a85f5e05b2b8.jpg', 15.90, 3, 47.70, '2025-08-19 05:26:14', '2025-08-19 05:26:14');
INSERT INTO `t_order_item` VALUES (12, 9, '17555523739897bbe28', 7, '海南妃子笑荔枝', '/uploads/images/2025/08/17/3a25d980-d862-45b0-8ccc-5ea73c9320df.jpg', 39.90, 1, 39.90, '2025-08-19 05:26:14', '2025-08-19 05:26:14');
INSERT INTO `t_order_item` VALUES (13, 13, 'ORD202412251001', 1, '海南妃子笑荔枝', 'https://img1.baidu.com/it/u=3462355338,123123123&fm=253&fmt=auto&app=138&f=JPEG', 39.90, 1, 39.90, '2025-08-19 05:29:35', '2025-08-19 05:29:35');
INSERT INTO `t_order_item` VALUES (14, 13, 'ORD202412251001', 4, '新疆哈密瓜', 'https://img1.baidu.com/it/u=333333333,444444444&fm=253&fmt=auto&app=138&f=JPEG', 29.90, 1, 29.90, '2025-08-19 05:29:35', '2025-08-19 05:29:35');
INSERT INTO `t_order_item` VALUES (15, 13, 'ORD202412251001', 3, '泰国椰青', 'https://img1.baidu.com/it/u=111111111,222222222&fm=253&fmt=auto&app=138&f=JPEG', 15.90, 1, 15.90, '2025-08-19 05:29:35', '2025-08-19 05:29:35');
INSERT INTO `t_order_item` VALUES (16, 14, 'ORD202412251002', 2, '山东大樱桃', 'https://img1.baidu.com/it/u=123456789,987654321&fm=253&fmt=auto&app=138&f=JPEG', 59.90, 1, 59.90, '2025-08-19 05:29:35', '2025-08-19 05:29:35');
INSERT INTO `t_order_item` VALUES (17, 14, 'ORD202412251002', 6, '广西百香果', 'https://img1.baidu.com/it/u=777777777,888888888&fm=253&fmt=auto&app=138&f=JPEG', 29.90, 1, 29.90, '2025-08-19 05:29:35', '2025-08-19 05:29:35');
INSERT INTO `t_order_item` VALUES (18, 14, 'ORD202412251002', 3, '泰国椰青', 'https://img1.baidu.com/it/u=111111111,222222222&fm=253&fmt=auto&app=138&f=JPEG', 15.90, 1, 15.90, '2025-08-19 05:29:35', '2025-08-19 05:29:35');
INSERT INTO `t_order_item` VALUES (19, 15, 'ORD202412251003', 5, '山东大黄桃', 'https://img1.baidu.com/it/u=555555555,666666666&fm=253&fmt=auto&app=138&f=JPEG', 25.80, 1, 25.80, '2025-08-19 05:29:35', '2025-08-19 05:29:35');
INSERT INTO `t_order_item` VALUES (20, 15, 'ORD202412251003', 1, '海南妃子笑荔枝', 'https://img1.baidu.com/it/u=3462355338,123123123&fm=253&fmt=auto&app=138&f=JPEG', 39.90, 1, 39.90, '2025-08-19 05:29:35', '2025-08-19 05:29:35');
INSERT INTO `t_order_item` VALUES (21, 16, '1755855554098c3d2dd', 1, '新西兰加纳纯苹果', '/api/images/77e75013-b16e-4da5-b2d8-4ab645a64f10.jpg', 99.99, 1, 99.99, '2025-08-22 17:39:14', '2025-08-22 17:39:14');
INSERT INTO `t_order_item` VALUES (22, 17, '1755864260116a7db1e', 1, '新西兰加纳纯苹果', 'http://localhost:8083/image-service/api/images/77e75013-b16e-4da5-b2d8-4ab645a64f10.jpg', 99.99, 1, 99.99, '2025-08-22 20:04:20', '2025-08-22 20:04:20');
INSERT INTO `t_order_item` VALUES (23, 18, '17558705645881b686c', 6, '广西百香果', '/api/images/b5f3107f-b2c2-42db-9981-9d3b2fcf7bfa.jpg', 29.90, 3, 89.70, '2025-08-22 21:49:25', '2025-08-22 21:49:25');
INSERT INTO `t_order_item` VALUES (24, 18, '17558705645881b686c', 19, '苹果', '/api/images/da99440e-d18e-4ece-8e13-add6704d187d.jpg', 19.68, 2, 39.36, '2025-08-22 21:49:25', '2025-08-22 21:49:25');
INSERT INTO `t_order_item` VALUES (25, 18, '17558705645881b686c', 1, '新西兰加纳纯苹果', '/api/images/77e75013-b16e-4da5-b2d8-4ab645a64f10.jpg', 99.99, 1, 99.99, '2025-08-22 21:49:25', '2025-08-22 21:49:25');
INSERT INTO `t_order_item` VALUES (26, 18, '17558705645881b686c', 2, '大泽山玫瑰香葡萄', '/api/images/85b77d95-9c91-4303-863a-8d09512b8661.jpg', 59.90, 2, 119.80, '2025-08-22 21:49:25', '2025-08-22 21:49:25');
INSERT INTO `t_order_item` VALUES (27, 18, '17558705645881b686c', 15, '榴莲', '/api/images/76ecf952-b93c-4366-8f96-27690ce7768b.jpg', 156.00, 1, 156.00, '2025-08-22 21:49:25', '2025-08-22 21:49:25');
INSERT INTO `t_order_item` VALUES (28, 18, '17558705645881b686c', 8, '正宗砀山梨', '/api/images/531aa582-816a-4048-85d8-159633e422db.jpg', 59.90, 2, 119.80, '2025-08-22 21:49:25', '2025-08-22 21:49:25');
INSERT INTO `t_order_item` VALUES (29, 19, '175588259531100b01b', 6, '广西百香果', '/api/images/b5f3107f-b2c2-42db-9981-9d3b2fcf7bfa.jpg', 29.90, 2, 59.80, '2025-08-23 01:09:55', '2025-08-23 01:09:55');
INSERT INTO `t_order_item` VALUES (30, 20, '1755883535333df0173', 6, '广西百香果', '/api/images/b5f3107f-b2c2-42db-9981-9d3b2fcf7bfa.jpg', 29.90, 1, 29.90, '2025-08-23 01:25:35', '2025-08-23 01:25:35');
INSERT INTO `t_order_item` VALUES (31, 21, '17559474476280a55c9', 58, 'ceshi', '/api/images/2754559c-b169-4f83-8e1b-145a2b15b846.jpg', 0.01, 3, 0.03, '2025-08-23 19:10:48', '2025-08-23 19:10:48');
INSERT INTO `t_order_item` VALUES (32, 22, '1755954737010303165', 59, '苹果2', '/api/images/e4fa5f8c-d966-40fc-ba7e-97ac8ad4ddfe.jpg', 10.70, 1, 10.70, '2025-08-23 21:12:17', '2025-08-23 21:12:17');
INSERT INTO `t_order_item` VALUES (33, 23, '1755956655735a83481', 6, '广西百香果', '/api/images/f9c385c3-20dc-45cd-914f-31a354fc7e77.jpg', 29.90, 1, 29.90, '2025-08-23 21:44:16', '2025-08-23 21:44:16');
INSERT INTO `t_order_item` VALUES (34, 23, '1755956655735a83481', 57, '11', '/api/images/d9378be4-6d6d-4629-9378-07cdc6530ab8.jpg', 0.03, 1, 0.03, '2025-08-23 21:44:16', '2025-08-23 21:44:16');
INSERT INTO `t_order_item` VALUES (35, 23, '1755956655735a83481', 9, '红宝石莲雾', '/api/images/a3f7b5bf-a419-4403-a87e-e8d152d8111d.jpg', 15.90, 1, 15.90, '2025-08-23 21:44:16', '2025-08-23 21:44:16');
INSERT INTO `t_order_item` VALUES (36, 23, '1755956655735a83481', 40, '广西桂林沙田柚', '/api/images/0545abb9-b373-4e09-878b-4f3c693456d5.jpg', 48.00, 1, 48.00, '2025-08-23 21:44:16', '2025-08-23 21:44:16');
INSERT INTO `t_order_item` VALUES (37, 23, '1755956655735a83481', 59, '苹果2', '/api/images/e4fa5f8c-d966-40fc-ba7e-97ac8ad4ddfe.jpg', 10.70, 1, 10.70, '2025-08-23 21:44:16', '2025-08-23 21:44:16');
INSERT INTO `t_order_item` VALUES (38, 23, '1755956655735a83481', 61, '苹果4', '/api/images/a8fcc693-51d4-4d4b-826d-8235ba3aa2b1.jpg', 0.60, 4, 2.40, '2025-08-23 21:44:16', '2025-08-23 21:44:16');

-- ----------------------------
-- Table structure for t_recommend_history
-- ----------------------------
DROP TABLE IF EXISTS `t_recommend_history`;
CREATE TABLE `t_recommend_history`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `condition` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '推荐条件',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 182 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '推荐历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_recommend_history
-- ----------------------------
INSERT INTO `t_recommend_history` VALUES (1, 1, '可口', '2025-08-11 00:54:04');
INSERT INTO `t_recommend_history` VALUES (2, 1, '可口', '2025-08-11 00:59:47');
INSERT INTO `t_recommend_history` VALUES (17, 2, '孕妇吃的', '2025-08-11 01:37:18');
INSERT INTO `t_recommend_history` VALUES (18, 2, '1', '2025-08-11 01:38:06');
INSERT INTO `t_recommend_history` VALUES (19, 2, '2', '2025-08-11 01:42:43');
INSERT INTO `t_recommend_history` VALUES (23, 2, '1', '2025-08-11 22:47:05');
INSERT INTO `t_recommend_history` VALUES (24, 2, '1', '2025-08-11 22:51:54');
INSERT INTO `t_recommend_history` VALUES (25, 2, '可口', '2025-08-11 23:28:59');
INSERT INTO `t_recommend_history` VALUES (26, 2, '可口', '2025-08-11 23:29:34');
INSERT INTO `t_recommend_history` VALUES (27, 2, '孕期', '2025-08-12 22:26:08');
INSERT INTO `t_recommend_history` VALUES (28, 1, '我想要甜的水果', '2025-08-15 20:11:05');
INSERT INTO `t_recommend_history` VALUES (29, 1, '甜的水果', '2025-08-15 20:11:14');
INSERT INTO `t_recommend_history` VALUES (30, 1, '甜的水果', '2025-08-15 20:12:17');
INSERT INTO `t_recommend_history` VALUES (31, 1, '适合孕妇的水果', '2025-08-15 20:29:10');
INSERT INTO `t_recommend_history` VALUES (32, 1, '适合孕妇的水果', '2025-08-15 20:29:18');
INSERT INTO `t_recommend_history` VALUES (33, 1, '想要补充维生素C', '2025-08-15 20:38:29');
INSERT INTO `t_recommend_history` VALUES (34, 2, '1', '2025-08-15 23:00:05');
INSERT INTO `t_recommend_history` VALUES (35, 2, '可口', '2025-08-15 23:02:54');
INSERT INTO `t_recommend_history` VALUES (36, 2, '可口', '2025-08-15 23:05:11');
INSERT INTO `t_recommend_history` VALUES (37, 13, '可口', '2025-08-16 00:49:00');
INSERT INTO `t_recommend_history` VALUES (38, 2, '可口', '2025-08-17 22:20:12');
INSERT INTO `t_recommend_history` VALUES (39, 2, '可口', '2025-08-17 22:20:49');
INSERT INTO `t_recommend_history` VALUES (40, 1, '酸甜可口', '2025-08-17 22:22:41');
INSERT INTO `t_recommend_history` VALUES (41, 2, '可口', '2025-08-17 22:23:08');
INSERT INTO `t_recommend_history` VALUES (42, 2, '可口', '2025-08-17 22:33:00');
INSERT INTO `t_recommend_history` VALUES (43, 2, '可口', '2025-08-17 22:35:00');
INSERT INTO `t_recommend_history` VALUES (44, 2, '可口', '2025-08-17 22:35:23');
INSERT INTO `t_recommend_history` VALUES (45, 2, '可口', '2025-08-17 22:37:04');
INSERT INTO `t_recommend_history` VALUES (46, 2, '口感', '2025-08-18 00:15:28');
INSERT INTO `t_recommend_history` VALUES (47, 2, '口感', '2025-08-18 00:15:31');
INSERT INTO `t_recommend_history` VALUES (48, 1, '酸甜可口的水果', '2025-08-20 01:52:57');
INSERT INTO `t_recommend_history` VALUES (49, 1, '甜的红色水果', '2025-08-20 02:02:46');
INSERT INTO `t_recommend_history` VALUES (50, 1, '酸甜的黄色水果', '2025-08-20 02:05:25');
INSERT INTO `t_recommend_history` VALUES (51, 1, '适合孕妇的营养水果', '2025-08-20 02:05:32');
INSERT INTO `t_recommend_history` VALUES (52, 1, '想要补充维生素C的水果', '2025-08-20 02:09:31');
INSERT INTO `t_recommend_history` VALUES (162, 3, '草莓', '2025-08-23 21:04:50');
INSERT INTO `t_recommend_history` VALUES (163, 3, '草莓', '2025-08-23 21:04:57');
INSERT INTO `t_recommend_history` VALUES (164, 3, '水果', '2025-08-23 21:07:08');
INSERT INTO `t_recommend_history` VALUES (165, 3, '我想吃甜的水果，给我推荐一下', '2025-08-23 21:07:30');
INSERT INTO `t_recommend_history` VALUES (166, 3, '我想吃广西的水果，给我推荐一下', '2025-08-23 21:07:37');
INSERT INTO `t_recommend_history` VALUES (167, 3, '我想吃酸酸甜甜的水果，给我推荐一下', '2025-08-23 21:07:54');
INSERT INTO `t_recommend_history` VALUES (168, 3, '草莓', '2025-08-23 21:08:36');
INSERT INTO `t_recommend_history` VALUES (169, 3, '我想吃酸酸甜甜的水果，给我推荐一下', '2025-08-23 21:17:14');
INSERT INTO `t_recommend_history` VALUES (170, 3, '送礼的水果，给我推荐一下', '2025-08-23 21:17:41');
INSERT INTO `t_recommend_history` VALUES (171, 3, '送礼的水果，给我推荐一下', '2025-08-23 21:17:43');
INSERT INTO `t_recommend_history` VALUES (172, 3, '送礼的水果，给我推荐一下', '2025-08-23 21:17:46');
INSERT INTO `t_recommend_history` VALUES (173, 3, '送礼的水果，给我推荐一下', '2025-08-23 21:48:52');
INSERT INTO `t_recommend_history` VALUES (174, 3, '送礼的水果，给我推荐一下', '2025-08-23 21:49:04');
INSERT INTO `t_recommend_history` VALUES (175, 3, '送礼的水果，给我推荐一下', '2025-08-23 21:57:02');
INSERT INTO `t_recommend_history` VALUES (176, 3, '送礼的水果，给我推荐一下', '2025-08-23 22:00:34');
INSERT INTO `t_recommend_history` VALUES (177, 3, '酸酸甜甜的水果', '2025-08-23 22:00:46');
INSERT INTO `t_recommend_history` VALUES (178, 3, '广西的水果', '2025-08-23 22:00:56');
INSERT INTO `t_recommend_history` VALUES (179, 3, '广西的水果', '2025-08-23 22:09:43');
INSERT INTO `t_recommend_history` VALUES (180, 3, '广西的水果', '2025-08-23 23:47:18');
INSERT INTO `t_recommend_history` VALUES (181, 3, '台湾的水果', '2025-08-23 23:47:28');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` tinyint(1) NULL DEFAULT 0 COMMENT '0男1女',
  `birthday` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '生日',
  `address` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '地址',
  `bio` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '个人简介',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_mobile`(`mobile` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '13800138000', 'e10adc3949ba59abbe56e057f20f883e', 'test', '更新后的昵称1755013678096', NULL, 0, NULL, NULL, NULL, 'test@163.com', 1, '2025-08-20 02:09:00', '127.0.0.1', '2025-08-10 01:51:08', '2025-08-20 02:09:00', NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (2, '13900139001', 'e10adc3949ba59abbe56e057f20f883e', 'testuser2', '测试用户2', NULL, 0, NULL, NULL, NULL, 'xxx', 1, '2025-08-19 04:46:45', '127.0.0.1', '2025-08-10 02:03:19', '2025-08-20 00:03:56', NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (3, '19127810932', 'e10adc3949ba59abbe56e057f20f883e', '19127810932', '用户0932', NULL, 0, NULL, NULL, NULL, '1', 1, '2025-08-23 14:36:26', '127.0.0.1', '2025-08-10 13:22:26', '2025-08-23 14:36:25', NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (5, '13900139000', '14e1b600b1fd579f47433b88e8d85291', 'testuser', '测试用户', NULL, 0, NULL, NULL, NULL, '555555', 1, '2025-08-16 02:03:39', '127.0.0.1', '2025-08-10 16:09:56', '2025-08-17 15:20:04', NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (6, '1390041156', '14e1b600b1fd579f47433b88e8d85291', '1390041156', '用户156', NULL, 0, NULL, NULL, NULL, '444444', 1, NULL, NULL, '2025-08-11 00:49:01', '2025-08-17 15:19:58', NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (8, '139007745', 'e10adc3949ba59abbe56e057f20f883e', '139007745', '用户45', NULL, 0, NULL, NULL, NULL, '3', 1, NULL, NULL, '2025-08-12 23:46:48', '2025-08-12 23:46:48', NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (10, '1390077972', '14e1b600b1fd579f47433b88e8d85291', '1390077972', '用户972', NULL, 0, NULL, NULL, NULL, '22222', 1, NULL, NULL, '2025-08-12 23:47:58', '2025-08-17 15:19:52', NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (13, '13800138001', 'f4cc399f0effd13c888e310ea2cf5399', '13800138001', '用户8001', NULL, 0, NULL, NULL, NULL, '111111', 1, '2025-08-15 23:51:53', '127.0.0.1', '2025-08-15 23:49:36', '2025-08-17 15:19:46', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of type
-- ----------------------------
INSERT INTO `type` VALUES (1, '童装系列');
INSERT INTO `type` VALUES (2, '女装系列');
INSERT INTO `type` VALUES (3, '男装系列');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `password` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `email` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `idadmin` bit(1) NULL DEFAULT b'0' COMMENT '是否为管理员 0否1是',
  `isvalidate` bit(1) NULL DEFAULT b'1' COMMENT '账户是否有效 0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '琳娜', '123456', 'zs', 'zs@163.com', '13312345678', NULL, b'0', b'1');
INSERT INTO `user` VALUES (2, 'ls', '123456', '??', 'ls@163.com', '13312345678', '???', b'0', b'1');
INSERT INTO `user` VALUES (3, 'ww', '123456', '??', 'ww@163.com', '13312345678', '??', b'0', b'1');
INSERT INTO `user` VALUES (4, 'ZS', '123456', '调动', '1@1.COM', '13312345678', '??', b'0', b'1');
INSERT INTO `user` VALUES (5, '王瑞', '123456', 'zs', '1@1.com', '12345678912', '南宁', b'0', b'1');
INSERT INTO `user` VALUES (6, 'lll', '123456', '????', 'lz@123.com', '13312345678', '???', b'0', b'1');
INSERT INTO `user` VALUES (7, 'lll', '123456', '????', 'lz@123.com', '13312345678', '???', b'0', b'1');
INSERT INTO `user` VALUES (8, 'lll', '123456', '????', 'lz@123.com', '13312345678', '???', b'0', b'1');
INSERT INTO `user` VALUES (9, 'lll', '123456', '????', 'lz@123.com', '12345678912', '???', b'0', b'1');
INSERT INTO `user` VALUES (10, 'lll', '123456', '????', 'lz@123.com', '13312344678', '???', b'0', b'1');
INSERT INTO `user` VALUES (11, 'lll', '123456', '????', 'lz@123.com', '13312344678', '???', b'0', b'1');
INSERT INTO `user` VALUES (12, 'lll', '123456', '????', 'lz@123.com', '13312345678', '???', b'0', b'1');

SET FOREIGN_KEY_CHECKS = 1;
