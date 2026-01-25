# 小区物业管理系统

## 项目简介

小区物业管理系统是一个基于 SpringBoot + Vue3 的全栈 Web 应用，旨在为小区物业管理提供便民的数字化服务平台。系统分为业主端和管理端，支持用户管理、房屋信息管理、公告通知、缴费记录、报修申请等核心功能。

## 技术栈

### 后端技术栈
- **Java 8+**
- **Spring Boot 2.7.14** - 主框架
- **Spring Security** - 安全框架
- **MyBatis Plus 3.5.3** - ORM框架
- **MySQL 8.0** - 数据库
- **Maven** - 构建工具
- **JWT** - 身份认证

### 前端技术栈
- **Vue 3** - 前端框架
- **Element Plus** - UI组件库
- **Vue Router 4** - 路由管理
- **Axios** - HTTP客户端
- **Pinia** - 状态管理
- **Vite** - 构建工具

## 功能特性

### 用户端功能
- ✅ 用户注册登录
- ✅ 个人信息管理
- ✅ 房屋信息查看
- ✅ 公告通知浏览
- ✅ 缴费记录查询
- ✅ 报修申请提交
- ✅ 访客登记（开发中）
- ✅ 投诉建议（开发中）

### 管理端功能
- ✅ 仪表板数据概览
- ✅ 用户管理
- ✅ 房屋信息管理
- ✅ 公告发布管理
- ✅ 缴费记录管理
- ✅ 报修处理管理
- ✅ 小区信息管理
- ✅ 系统日志（开发中）

## 项目结构

```
小区物业管理系统/
├── src/main/java/com/community/          # 后端源码
│   ├── entity/                           # 实体类
│   ├── mapper/                           # MyBatis映射接口
│   ├── service/                          # 业务逻辑层
│   ├── controller/                       # 控制器层
│   ├── common/                           # 公共类
│   └── PropertyManagementApplication.java # 启动类
├── src/main/resources/                   # 资源文件
│   ├── application.yml                   # 配置文件
│   └── mapper/                           # MyBatis XML映射文件
├── frontend/                             # 前端源码
│   ├── src/
│   │   ├── views/                        # 页面组件
│   │   ├── router/                       # 路由配置
│   │   ├── App.vue                       # 根组件
│   │   └── main.js                       # 入口文件
│   ├── package.json                      # NPM配置
│   └── vite.config.js                    # Vite配置
├── database/                             # 数据库脚本
│   └── init.sql                          # 初始化SQL脚本
├── pom.xml                               # Maven配置
└── README.md                             # 项目文档
```

## 安装部署

### 环境要求
- JDK 8 或更高版本
- Node.js 16+ 和 npm
- MySQL 8.0
- Maven 3.6+

### 后端部署

1. **克隆项目**
   ```bash
   git clone <项目地址>
   cd 小区物业管理系统
   ```

2. **创建数据库**
   ```bash
   # 连接MySQL数据库
   mysql -u root -p
   
   # 执行初始化脚本
   source database/init.sql
   ```

3. **修改配置**
   编辑 `src/main/resources/application.yml`，修改数据库连接信息：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/property_management
       username: your_username
       password: your_password
   ```

4. **编译运行**
   ```bash
   # 编译项目
   mvn clean compile
   
   # 运行项目
   mvn spring-boot:run
   ```
   
   后端服务将在 http://localhost:8080/api 启动

### 前端部署

1. **安装依赖**
   ```bash
   cd frontend
   npm install
   ```

2. **开发环境运行**
   ```bash
   npm run dev
   ```
   前端应用将在 http://localhost:3000 启动

3. **生产环境构建**
   ```bash
   npm run build
   ```

## 默认账户

系统预置了以下测试账户：

### 管理员账户
- 用户名：`admin`
- 密码：`123456`
- 权限：管理员权限，可访问管理后台

### 业主账户
- 用户名：`zhangsan`
- 密码：`123456`
- 权限：普通业主权限，可使用业主功能

## API 接口文档

### 用户相关接口
- `POST /api/user/login` - 用户登录
- `POST /api/user/register` - 用户注册
- `GET /api/user/{id}` - 获取用户信息
- `PUT /api/user/{id}` - 更新用户信息

### 公告相关接口
- `GET /api/notice/list` - 获取公告列表
- `GET /api/notice/top` - 获取置顶公告
- `GET /api/notice/{id}` - 获取公告详情
- `POST /api/notice/publish` - 发布公告（管理员）
- `PUT /api/notice/{id}` - 更新公告（管理员）
- `DELETE /api/notice/{id}` - 删除公告（管理员）

### 房屋相关接口
- `GET /api/house/list` - 获取房屋列表
- `GET /api/house/{id}` - 获取房屋详情
- `GET /api/house/building/{buildingNo}` - 根据楼栋号查询房屋
- `GET /api/house/owner/{ownerName}` - 根据业主姓名查询房屋
- `GET /api/house/vacant` - 获取空置房屋列表
- `POST /api/house/add` - 新增房屋（管理员）
- `PUT /api/house/{id}` - 更新房屋信息（管理员）
- `DELETE /api/house/{id}` - 删除房屋（管理员）

### 报修相关接口
- `GET /api/repair/list` - 获取所有报修申请（管理员）
- `GET /api/repair/pending` - 获取待处理报修（管理员）
- `GET /api/repair/{id}` - 获取报修详情
- `GET /api/repair/applicant/{applicantId}` - 根据申请人获取报修记录
- `POST /api/repair/submit` - 提交报修申请（业主）
- `PUT /api/repair/{id}/handle` - 处理报修申请（管理员）
- `PUT /api/repair/{id}/complete` - 完成报修申请（管理员）
- `DELETE /api/repair/{id}` - 删除报修申请（管理员）

## 数据库设计

### 主要数据表
- `sys_user` - 用户表
- `community_info` - 小区信息表
- `house_info` - 房屋信息表
- `notice` - 公告表
- `property_payment` - 物业缴费记录表
- `repair_request` - 报修申请表

详细的表结构请参考 `database/init.sql` 文件。

## 开发说明

### 后端开发
- 使用 MyBatis Plus 简化数据库操作
- 统一返回结果格式 `Result<T>`
- 使用 BCrypt 加密用户密码
- JWT 实现用户身份认证

### 前端开发
- 使用 Vue 3 Composition API
- Element Plus 提供 UI 组件
- Axios 拦截器处理请求响应
- Vue Router 实现前端路由

## 最新完成功能
- ✅ JWT身份认证机制
- ✅ Spring Security安全配置
- ✅ 全局异常处理
- ✅ 房屋管理完整功能
- ✅ 报修申请管理功能
- ✅ MyBatis XML映射文件
- ✅ 完整的CRUD操作接口

## 待开发功能
- [ ] 缴费记录管理功能
- [ ] 小区信息管理功能
- [ ] 用户权限细化管理
- [ ] 文件上传功能
- [ ] 数据统计图表
- [ ] 消息推送功能
- [ ] 移动端适配
- [ ] 系统日志管理
- [ ] 数据备份恢复

## 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

如有问题或建议，请通过以下方式联系：
- 邮箱：example@email.com
- QQ群：123456789

## 更新日志

### v1.0.0 (2024-01-15)
- ✅ 完成基础框架搭建
- ✅ 实现用户注册登录功能
- ✅ 实现公告管理功能
- ✅ 实现前端界面设计
- ✅ 完善项目文档