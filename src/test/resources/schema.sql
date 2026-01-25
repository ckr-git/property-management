-- H2数据库Schema初始化

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    id_card VARCHAR(18),
    user_type INT DEFAULT 1,
    house_id BIGINT,
    status INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 小区信息表
CREATE TABLE IF NOT EXISTS community_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    property_company VARCHAR(100),
    property_fee DECIMAL(10,2),
    parking_fee DECIMAL(10,2),
    description TEXT,
    contact_phone VARCHAR(20),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 房屋信息表
CREATE TABLE IF NOT EXISTS house_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    building_no VARCHAR(20) NOT NULL,
    unit_no VARCHAR(20),
    room_no VARCHAR(20) NOT NULL,
    area DECIMAL(8,2),
    house_type INT DEFAULT 1,
    owner_name VARCHAR(50),
    owner_phone VARCHAR(20),
    occupancy_status INT DEFAULT 0,
    status INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 公告表
CREATE TABLE IF NOT EXISTS notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    type INT DEFAULT 1,
    publisher_id BIGINT,
    publisher_name VARCHAR(50),
    status INT DEFAULT 1,
    is_top INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 物业缴费记录表
CREATE TABLE IF NOT EXISTS property_payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    house_id BIGINT NOT NULL,
    owner_id BIGINT,
    payment_type INT NOT NULL,
    payment_month VARCHAR(7) NOT NULL,
    should_pay_amount DECIMAL(10,2) NOT NULL,
    actual_pay_amount DECIMAL(10,2) DEFAULT 0,
    status INT DEFAULT 0,
    payment_time TIMESTAMP,
    remark TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 报修申请表
CREATE TABLE IF NOT EXISTS repair_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    applicant_id BIGINT,
    applicant_name VARCHAR(50),
    applicant_phone VARCHAR(20),
    house_id BIGINT,
    repair_type INT DEFAULT 1,
    description TEXT NOT NULL,
    images TEXT,
    status INT DEFAULT 0,
    handler_id BIGINT,
    handler_name VARCHAR(50),
    handle_remark TEXT,
    complete_time TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
