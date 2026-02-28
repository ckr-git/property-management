-- 小区物业管理系统数据库初始化脚本
CREATE DATABASE IF NOT EXISTS property_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE property_management;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    id_card VARCHAR(18) COMMENT '身份证号',
    user_type INT DEFAULT 1 COMMENT '用户类型：0-管理员，1-业主',
    house_id BIGINT COMMENT '房屋ID（业主用户关联）',
    status INT DEFAULT 0 COMMENT '账户状态：0-正常，1-禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='用户表';

-- 小区信息表
CREATE TABLE IF NOT EXISTS community_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '小区ID',
    name VARCHAR(100) NOT NULL COMMENT '小区名称',
    address VARCHAR(255) COMMENT '小区地址',
    property_company VARCHAR(100) COMMENT '物业公司',
    property_fee DECIMAL(10,2) COMMENT '物业费标准（元/月/平方米）',
    parking_fee DECIMAL(10,2) COMMENT '停车费标准（元/月）',
    description TEXT COMMENT '小区介绍',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='小区信息表';

-- 房屋信息表
CREATE TABLE IF NOT EXISTS house_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '房屋ID',
    building_no VARCHAR(20) NOT NULL COMMENT '楼栋号',
    unit_no VARCHAR(20) COMMENT '单元号',
    room_no VARCHAR(20) NOT NULL COMMENT '房间号',
    area DECIMAL(8,2) COMMENT '房屋面积（平方米）',
    house_type INT DEFAULT 1 COMMENT '房屋类型：1-住宅，2-商铺，3-车位',
    owner_name VARCHAR(50) COMMENT '业主姓名',
    owner_phone VARCHAR(20) COMMENT '业主电话',
    occupancy_status INT DEFAULT 0 COMMENT '入住状态：0-未入住，1-已入住',
    status INT DEFAULT 0 COMMENT '房屋状态：0-正常，1-出租，2-空置',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='房屋信息表';

-- 公告表
CREATE TABLE IF NOT EXISTS notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '公告ID',
    title VARCHAR(255) NOT NULL COMMENT '公告标题',
    content TEXT NOT NULL COMMENT '公告内容',
    type INT DEFAULT 1 COMMENT '公告类型：1-通知公告，2-停水停电，3-活动通知，4-温馨提示',
    publisher_id BIGINT COMMENT '发布人ID',
    publisher_name VARCHAR(50) COMMENT '发布人姓名',
    status INT DEFAULT 1 COMMENT '发布状态：0-草稿，1-已发布',
    is_top INT DEFAULT 0 COMMENT '是否置顶：0-否，1-是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='公告表';

-- 物业缴费记录表
CREATE TABLE IF NOT EXISTS property_payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '缴费记录ID',
    house_id BIGINT NOT NULL COMMENT '房屋ID',
    owner_id BIGINT COMMENT '业主ID',
    payment_type INT NOT NULL COMMENT '缴费类型：1-物业费，2-停车费，3-水费，4-电费，5-燃气费',
    payment_month VARCHAR(7) NOT NULL COMMENT '费用月份（格式：2023-01）',
    should_pay_amount DECIMAL(10,2) NOT NULL COMMENT '应缴金额',
    actual_pay_amount DECIMAL(10,2) DEFAULT 0 COMMENT '实缴金额',
    status INT DEFAULT 0 COMMENT '缴费状态：0-未缴费，1-已缴费，2-部分缴费',
    payment_time DATETIME COMMENT '缴费时间',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='物业缴费记录表';

-- 报修申请表
CREATE TABLE IF NOT EXISTS repair_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '报修ID',
    applicant_id BIGINT COMMENT '申请人ID',
    applicant_name VARCHAR(50) COMMENT '申请人姓名',
    applicant_phone VARCHAR(20) COMMENT '申请人电话',
    house_id BIGINT COMMENT '房屋ID',
    repair_type INT DEFAULT 1 COMMENT '报修类型：1-水电维修，2-门窗维修，3-电梯维修，4-公共设施，5-其他',
    description TEXT NOT NULL COMMENT '问题描述',
    images TEXT COMMENT '报修图片（多个图片用逗号分隔）',
    status INT DEFAULT 0 COMMENT '处理状态：0-待处理，1-处理中，2-已完成，3-已关闭',
    handler_id BIGINT COMMENT '处理人ID',
    handler_name VARCHAR(50) COMMENT '处理人姓名',
    handle_remark TEXT COMMENT '处理备注',
    complete_time DATETIME COMMENT '完成时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='报修申请表';

-- 创建索引
CREATE INDEX idx_user_username ON sys_user(username);
CREATE INDEX idx_user_phone ON sys_user(phone);
CREATE INDEX idx_house_building_room ON house_info(building_no, room_no);
CREATE INDEX idx_notice_status_top ON notice(status, is_top);
CREATE INDEX idx_payment_house_month ON property_payment(house_id, payment_month);
CREATE INDEX idx_repair_status ON repair_request(status);

-- 初始化数据

-- 插入小区信息
INSERT INTO community_info (name, address, property_company, property_fee, parking_fee, description, contact_phone) 
VALUES ('阳光小区', '北京市朝阳区阳光大街123号', '阳光物业管理有限公司', 2.5, 200.00, '阳光小区是一个环境优美、设施完善的现代化住宅小区', '400-123-4567');

-- 插入管理员用户
INSERT INTO sys_user (username, password, real_name, phone, user_type, status)
VALUES ('admin', '$2a$10$jKMRNsE80krKdlhvft4IRua4JmXiajljYD5XGcnYtw7KZntLC5j96', '系统管理员', '13800138000', 0, 0);
-- 密码是：123456

-- 插入测试业主用户
INSERT INTO sys_user (username, password, real_name, phone, id_card, user_type, status)
VALUES ('zhangsan', '$2a$10$jKMRNsE80krKdlhvft4IRua4JmXiajljYD5XGcnYtw7KZntLC5j96', '张三', '13901234567', '110101199001011234', 1, 0);

-- 插入房屋信息
INSERT INTO house_info (building_no, unit_no, room_no, area, house_type, owner_name, owner_phone, occupancy_status, status) 
VALUES 
('1号楼', '1单元', '101', 120.50, 1, '张三', '13901234567', 1, 0),
('1号楼', '1单元', '102', 95.30, 1, NULL, NULL, 0, 2),
('1号楼', '2单元', '201', 135.80, 1, '李四', '13912345678', 1, 0),
('2号楼', '1单元', '101', 88.60, 1, '王五', '13923456789', 1, 0),
('商铺', '', 'S001', 45.20, 2, '赵六', '13934567890', 1, 1);

-- 插入公告信息
INSERT INTO notice (title, content, type, publisher_id, publisher_name, status, is_top) 
VALUES 
('小区停水通知', '因市政管网维修，本小区将于2024年1月15日8:00-18:00停水，请各位业主提前储水。给您带来不便，敬请谅解！', 2, 1, '系统管理员', 1, 1),
('物业费缴费通知', '2024年第一季度物业费开始收取，请各位业主及时缴纳。缴费方式：线上缴费或到物业办公室现场缴费。', 1, 1, '系统管理员', 1, 0),
('春节假期物业值班安排', '春节假期期间（2月8日-2月17日），物业将安排值班人员，紧急事务请拨打值班电话：400-123-4567', 4, 1, '系统管理员', 1, 0),
('小区绿化改造通知', '为美化小区环境，物业将于本月底开始对小区绿化进行改造升级，施工期间可能产生噪音，敬请谅解。', 1, 1, '系统管理员', 1, 0);

-- 插入缴费记录
INSERT INTO property_payment (house_id, owner_id, payment_type, payment_month, should_pay_amount, actual_pay_amount, status, payment_time) 
VALUES 
(1, 2, 1, '2024-01', 301.25, 301.25, 1, '2024-01-05 10:30:00'),
(1, 2, 2, '2024-01', 200.00, 200.00, 1, '2024-01-05 10:30:00'),
(3, NULL, 1, '2024-01', 339.50, 0, 0, NULL),
(4, NULL, 1, '2024-01', 221.50, 221.50, 1, '2024-01-03 14:20:00');

-- 插入报修记录
INSERT INTO repair_request (applicant_id, applicant_name, applicant_phone, house_id, repair_type, description, status, handler_id, handler_name, handle_remark, complete_time) 
VALUES 
(2, '张三', '13901234567', 1, 1, '卫生间水龙头漏水，需要维修', 2, 1, '系统管理员', '已安排师傅上门维修完成', '2024-01-10 16:30:00'),
(NULL, '李四', '13912345678', 3, 2, '客厅窗户关不严，有风漏进来', 1, 1, '系统管理员', '已联系维修师傅，预计明天上门', NULL),
(NULL, '王五', '13923456789', 4, 3, '电梯按钮失灵，3楼按钮按不了', 0, NULL, NULL, NULL, NULL);

COMMIT;