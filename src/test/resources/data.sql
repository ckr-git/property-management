-- H2数据库测试数据初始化

-- 插入管理员用户 (密码: 123456)
INSERT INTO sys_user (username, password, real_name, phone, user_type, status) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyZFzUWwIM5cvTAqR8vYqhF0Zuq', '系统管理员', '13800138000', 0, 0);

-- 插入测试业主用户 (密码: 123456)
INSERT INTO sys_user (username, password, real_name, phone, id_card, user_type, status) 
VALUES ('zhangsan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyZFzUWwIM5cvTAqR8vYqhF0Zuq', '张三', '13901234567', '110101199001011234', 1, 0);

-- 插入小区信息
INSERT INTO community_info (name, address, property_company, property_fee, parking_fee, description, contact_phone) 
VALUES ('阳光小区', '北京市朝阳区阳光大街123号', '阳光物业管理有限公司', 2.5, 200.00, '阳光小区是一个环境优美、设施完善的现代化住宅小区', '400-123-4567');

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
(1, 2, 1, '2024-01', 301.25, 301.25, 1, CURRENT_TIMESTAMP),
(1, 2, 2, '2024-01', 200.00, 200.00, 1, CURRENT_TIMESTAMP),
(3, NULL, 1, '2024-01', 339.50, 0, 0, NULL),
(4, NULL, 1, '2024-01', 221.50, 221.50, 1, CURRENT_TIMESTAMP);

-- 插入报修记录
INSERT INTO repair_request (applicant_id, applicant_name, applicant_phone, house_id, repair_type, description, status, handler_id, handler_name, handle_remark, complete_time) 
VALUES 
(2, '张三', '13901234567', 1, 1, '卫生间水龙头漏水，需要维修', 2, 1, '系统管理员', '已安排师傅上门维修完成', CURRENT_TIMESTAMP),
(NULL, '李四', '13912345678', 3, 2, '客厅窗户关不严，有风漏进来', 1, 1, '系统管理员', '已联系维修师傅，预计明天上门', NULL),
(NULL, '王五', '13923456789', 4, 3, '电梯按钮失灵，3楼按钮按不了', 0, NULL, NULL, NULL, NULL);
