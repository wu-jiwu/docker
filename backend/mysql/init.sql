-- 强制设置会话字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 创建用户
CREATE USER IF NOT EXISTS 'crud_user'@'%' IDENTIFIED BY 'Crud@123456';
GRANT ALL PRIVILEGES ON crud_db.* TO 'crud_user'@'%';
FLUSH PRIVILEGES;

-- 创建数据库（明确指定字符集）
CREATE DATABASE IF NOT EXISTS crud_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE crud_db;

-- 设置数据库字符集
ALTER DATABASE crud_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建商品表（明确指定字符集）
CREATE TABLE IF NOT EXISTS products (
                                        id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                        product_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    product_code VARCHAR(50) NOT NULL UNIQUE COMMENT '商品编码',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存',
    category VARCHAR(50) NOT NULL COMMENT '分类',
    description TEXT COMMENT '描述',
    image_url VARCHAR(500) COMMENT '图片URL',
    is_active TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否有效（1:有效，0:删除）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category (category),
    INDEX idx_created_at (created_at),
    INDEX idx_product_code (product_code)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- 插入正确编码的测试数据
INSERT INTO products (product_name, product_code, price, stock, category, description) VALUES
                                                                                           ('苹果手机 iPhone 15', 'IPHONE15', 6999.00, 100, '手机', '最新款苹果手机'),
                                                                                           ('华为 Mate 60', 'MATE60', 5999.00, 50, '手机', '华为旗舰手机'),
                                                                                           ('小米电视 75英寸', 'TV75', 4999.00, 30, '家电', '大屏智能电视'),
                                                                                           ('联想笔记本电脑', 'LENOVO', 5999.00, 20, '电脑', '高性能笔记本电脑'),
                                                                                           ('索尼游戏机 PS5', 'PS5', 3999.00, 15, '游戏机', '次世代游戏主机');