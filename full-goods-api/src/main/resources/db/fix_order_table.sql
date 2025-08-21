-- 修复订单表缺失字段的SQL脚本

-- 添加 pay_time 字段（如果不存在）
SET @sql = IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'goods_shop' AND TABLE_NAME = 't_order' AND COLUMN_NAME = 'pay_time') = 0,
    'ALTER TABLE `t_order` ADD COLUMN `pay_time` datetime DEFAULT NULL COMMENT "支付时间" AFTER `status`',
    'SELECT "pay_time column already exists" as message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 create_by 字段（如果不存在）
SET @sql = IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'goods_shop' AND TABLE_NAME = 't_order' AND COLUMN_NAME = 'create_by') = 0,
    'ALTER TABLE `t_order` ADD COLUMN `create_by` varchar(50) DEFAULT NULL COMMENT "创建人" AFTER `update_time`',
    'SELECT "create_by column already exists" as message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 update_by 字段（如果不存在）
SET @sql = IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'goods_shop' AND TABLE_NAME = 't_order' AND COLUMN_NAME = 'update_by') = 0,
    'ALTER TABLE `t_order` ADD COLUMN `update_by` varchar(50) DEFAULT NULL COMMENT "更新人" AFTER `create_by`',
    'SELECT "update_by column already exists" as message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 remark 字段（如果不存在）
SET @sql = IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'goods_shop' AND TABLE_NAME = 't_order' AND COLUMN_NAME = 'remark') = 0,
    'ALTER TABLE `t_order` ADD COLUMN `remark` varchar(255) DEFAULT NULL COMMENT "备注" AFTER `update_by`',
    'SELECT "remark column already exists" as message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 修复订单项表的缺失字段
SET @sql = IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'goods_shop' AND TABLE_NAME = 't_order_item' AND COLUMN_NAME = 'fruit_name') = 0,
    'ALTER TABLE `t_order_item` ADD COLUMN `fruit_name` varchar(100) DEFAULT NULL COMMENT "水果名称" AFTER `fruit_id`',
    'SELECT "fruit_name column already exists" as message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 显示修复后的表结构
SELECT 'Order table structure after fix:' as message;
DESCRIBE `t_order`;

SELECT 'Order item table structure after fix:' as message;
DESCRIBE `t_order_item`;