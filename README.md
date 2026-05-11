# 小区物业管理系统

## 项目简介

小区物业管理系统是一个基于 Spring Boot + Vue 3 的全栈 Web 应用，为小区物业管理提供数字化服务平台。系统分为业主端和管理端，支持用户管理、房屋信息管理、公告通知、在线缴费、报修申请等核心功能。

## 技术栈

### 后端
- Java 8+ / Spring Boot 2.7.14
- Spring Security
- MyBatis Plus 3.5.3
- MySQL 8.0
- Maven

### 前端
- Vue 3 (Composition API) / Vite 4
- Element Plus
- Vue Router 4
- Axios
- Pinia

## 功能特性

### 业主端
- 用户注册、登录
- 个人信息编辑（姓名、电话、身份证）
- 密码修改（原密码验证）
- 房屋信息查看（通过手机号自动匹配）
- 公告通知浏览
- 缴费记录查询与在线缴费
- 报修申请提交
- 仪表板数据统计

### 管理端
- 仪表板数据概览
- 用户管理（列表、搜索、分页、查看详情）
- 房屋信息管理（CRUD、搜索、分页、表单验证）
- 公告发布管理（CRUD、搜索、分页、表单验证）
- 缴费记录管理（列表、搜索、分页）
- 报修处理管理（列表、搜索、状态筛选、分页、处理/完成操作）

## 项目结构

```
小区物业管理系统/
├── src/main/java/com/community/
│   ├── config/
│   ├── common/
│   ├── entity/
│   ├── dto/
│   ├── mapper/
│   ├── service/
│   ├── controller/
│   └── PropertyManagementApplication.java
├── src/main/resources/
│   ├── application.yml
│   └── mapper/
├── frontend/
│   ├── src/
│   │   ├── views/
│   │   ├── router/
│   │   ├── App.vue
│   │   └── main.js
│   ├── package.json
│   └── vite.config.js
├── database/
│   └── init.sql
├── pom.xml
└── README.md
```

## 快速开始

### 环境要求
- JDK 8+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0

### 启动步骤

1. 初始化数据库

```bash
mysql -u root -p < database/init.sql
```

2. 修改后端配置

编辑 `src/main/resources/application.yml`，确认数据库连接信息正确：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/property_management
    username: your_username
    password: your_password
```

3. 启动后端

```bash
mvn spring-boot:run
```

后端地址: http://localhost:8080/api

4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端地址: http://localhost:3000

### 默认账户

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | `admin` | `123456` | 可访问管理后台 |
| 业主 | `zhangsan` | `123456` | 普通业主功能 |

## API 接口

### 用户接口
| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| POST | `/api/user/login` | 用户登录 | username, password |
| POST | `/api/user/register` | 用户注册 | RequestBody: User |
| GET | `/api/user/list` | 用户列表（分页） | pageNum, pageSize, keyword |
| GET | `/api/user/{id}` | 获取用户信息 | - |
| PUT | `/api/user/{id}` | 更新用户信息 | RequestBody: User (realName, phone, idCard) |
| PUT | `/api/user/{id}/password` | 修改密码 | RequestBody: ChangePasswordDTO |

### 公告接口
| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | `/api/notice/list` | 公告列表（分页） | pageNum, pageSize, keyword |
| GET | `/api/notice/top` | 置顶公告 | - |
| GET | `/api/notice/{id}` | 公告详情 | - |
| POST | `/api/notice/publish` | 发布公告 | RequestBody: Notice |
| PUT | `/api/notice/{id}` | 更新公告 | RequestBody: Notice |
| DELETE | `/api/notice/{id}` | 删除公告 | - |

### 房屋接口
| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | `/api/house/list` | 房屋列表（分页） | pageNum, pageSize, keyword |
| GET | `/api/house/owner-id/{ownerId}` | 业主房屋查询 | - |
| GET | `/api/house/{id}` | 房屋详情 | - |
| POST | `/api/house/add` | 新增房屋 | RequestBody: House |
| PUT | `/api/house/{id}` | 更新房屋 | RequestBody: House |
| DELETE | `/api/house/{id}` | 删除房屋 | - |

### 报修接口
| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | `/api/repair/list` | 报修列表（分页） | pageNum, pageSize, keyword, status |
| GET | `/api/repair/applicant/{id}` | 业主报修记录 | - |
| POST | `/api/repair/submit` | 提交报修 | RequestBody: RepairRequest |
| PUT | `/api/repair/{id}/handle` | 处理报修 | RequestBody |
| PUT | `/api/repair/{id}/complete` | 完成报修 | RequestBody |

### 缴费接口
| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | `/api/payment/list` | 缴费列表（分页） | pageNum, pageSize, status, paymentType |
| GET | `/api/payment/owner/{ownerId}` | 业主缴费记录 | - |
| PUT | `/api/payment/{id}/pay` | 在线缴费 | - |

## 数据库设计

| 表名 | 说明 |
|------|------|
| `sys_user` | 用户表 |
| `house_info` | 房屋信息表 |
| `notice` | 公告表 |
| `property_payment` | 物业缴费记录表 |
| `repair_request` | 报修申请表 |
| `community_info` | 小区信息表 |

## 许可证

MIT License
