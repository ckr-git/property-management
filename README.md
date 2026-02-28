# 小区物业管理系统

## 项目简介

小区物业管理系统是一个基于 Spring Boot + Vue 3 的全栈 Web 应用，为小区物业管理提供数字化服务平台。系统分为业主端和管理端，支持用户管理、房屋信息管理、公告通知、在线缴费、报修申请等核心功能。

## 技术栈

### 后端
- **Java 8+** / **Spring Boot 2.7.14**
- **Spring Security** - 安全框架
- **MyBatis Plus 3.5.3** - ORM 框架（含分页插件）
- **MySQL 8.0** - 数据库
- **Maven** - 构建工具
- **BCrypt** - 密码加密

### 前端
- **Vue 3** (Composition API) / **Vite 4**
- **Element Plus** - UI 组件库
- **Vue Router 4** - 路由管理
- **Axios** - HTTP 客户端
- **Pinia** - 状态管理

## 功能特性

### 业主端
- 用户注册、登录
- 个人信息编辑（姓名、电话、身份证）
- 密码修改（原密码验证）
- 房屋信息查看（通过手机号自动匹配）
- 公告通知浏览
- 缴费记录查询与在线缴费
- 报修申请提交（支持类型选择、表单验证）
- 仪表板数据统计（待缴费用、报修状态、公告数量）

### 管理端
- 仪表板数据概览（用户数、房屋数、收入、待处理报修）
- 用户管理（列表、搜索、分页、查看详情）
- 房屋信息管理（CRUD、搜索、分页、表单验证）
- 公告发布管理（CRUD、搜索、分页、表单验证）
- 缴费记录管理（列表、搜索、分页）
- 报修处理管理（列表、搜索、状态筛选、分页、处理/完成操作）

## 项目结构

```
小区物业管理系统/
├── src/main/java/com/community/
│   ├── config/                              # 配置类（MyBatis Plus 分页等）
│   ├── common/                              # 公共类（Result 统一返回）
│   ├── entity/                              # 实体类
│   ├── dto/                                 # 数据传输对象
│   ├── mapper/                              # MyBatis 映射接口
│   ├── service/                             # 业务逻辑层
│   ├── controller/                          # 控制器层
│   └── PropertyManagementApplication.java   # 启动类
├── src/main/resources/
│   ├── application.yml                      # 配置文件
│   └── mapper/                              # MyBatis XML 映射文件
├── frontend/
│   ├── src/
│   │   ├── views/                           # 页面组件
│   │   │   ├── Home.vue                     # 首页
│   │   │   ├── Login.vue                    # 登录页
│   │   │   ├── Register.vue                 # 注册页
│   │   │   ├── admin/AdminDashboard.vue     # 管理端
│   │   │   └── user/UserDashboard.vue       # 业主端
│   │   ├── router/                          # 路由配置
│   │   ├── App.vue                          # 根组件
│   │   └── main.js                          # 入口文件
│   ├── package.json                         # NPM 配置
│   └── vite.config.js                       # Vite 配置
├── database/
│   └── init.sql                             # 数据库初始化脚本
├── pom.xml                                  # Maven 配置
└── README.md
```

## 安装部署

### 环境要求
- JDK 8+
- Node.js 16+ 和 npm
- MySQL 8.0
- Maven 3.6+

### 1. 创建数据库

```bash
mysql -u root -p
source database/init.sql
```

### 2. 修改配置

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/property_management
    username: your_username
    password: your_password
```

### 3. 启动后端

```bash
mvn clean compile
mvn spring-boot:run
```

后端服务启动在 http://localhost:8080/api

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端应用启动在 http://localhost:3000

## 默认账户

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

详细表结构参考 `database/init.sql`。

## 许可证

MIT License
