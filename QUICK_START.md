# 🚀 小区物业管理系统 - 快速启动指南

## 📋 环境要求

在启动项目之前，请确保已安装以下软件：

### 必需环境
- **JDK 8+** - Java开发环境
- **Maven 3.6+** - Java项目构建工具
- **Node.js 16+** - JavaScript运行时环境
- **npm** - Node.js包管理器（通常随Node.js一起安装）
- **MySQL 8.0** - 数据库服务

### 环境安装链接
- JDK: https://www.oracle.com/java/technologies/downloads/
- Maven: https://maven.apache.org/download.cgi
- Node.js: https://nodejs.org/
- MySQL: https://dev.mysql.com/downloads/mysql/

## ⚡ 一键启动（推荐）

1. **双击运行 `start-all.bat`**
   - 这是最简单的启动方式
   - 会自动检查环境并同时启动前后端服务
   - 适合快速体验项目

2. **等待启动完成**
   - 后端服务约需15-30秒启动
   - 前端服务首次运行需安装依赖，可能需要2-5分钟

3. **访问应用**
   - 前端地址：http://localhost:3000
   - 后端接口：http://localhost:8080/api

## 🔧 分步启动

### 步骤1: 初始化数据库

**选项A: 使用脚本（推荐）**
```bash
双击运行 init-database.bat
```

**选项B: 手动执行**
```bash
# 连接MySQL数据库
mysql -u root -p

# 执行初始化脚本
source database/init.sql
```

### 步骤2: 启动后端服务

**选项A: 使用脚本**
```bash
双击运行 start-backend.bat
```

**选项B: 命令行**
```bash
# 在项目根目录执行
mvn clean spring-boot:run
```

### 步骤3: 启动前端服务

**选项A: 使用脚本**
```bash
双击运行 start-frontend.bat
```

**选项B: 命令行**
```bash
# 进入前端目录
cd frontend

# 安装依赖（仅首次运行需要）
npm install

# 启动开发服务器
npm run dev
```

## 🎯 测试账户

系统预置了以下测试账户：

### 管理员账户
- **用户名**: `admin`
- **密码**: `123456`
- **权限**: 完整管理权限，可访问管理后台

### 业主账户
- **用户名**: `zhangsan`
- **密码**: `123456`
- **权限**: 业主权限，可使用业主功能

## 🌐 访问地址

启动成功后，可以通过以下地址访问：

- **前端应用**: http://localhost:3000
- **后端接口**: http://localhost:8080/api
- **API测试**: http://localhost:8080/api/notice/list

## 📱 功能预览

### 业主端功能
- ✅ 用户注册登录
- ✅ 查看公告通知  
- ✅ 个人信息管理
- ✅ 房屋信息查看
- ✅ 提交报修申请

### 管理端功能
- ✅ 系统数据概览
- ✅ 用户信息管理
- ✅ 发布管理公告
- ✅ 处理报修申请
- ✅ 房屋信息管理

## 🛠️ 常见问题

### Q: 启动后端时提示"找不到Java环境"
**A**: 请安装JDK并确保添加到系统PATH环境变量

### Q: 启动前端时提示"找不到Node.js"
**A**: 请安装Node.js并确保添加到系统PATH环境变量

### Q: 数据库连接失败
**A**: 请检查：
- MySQL服务是否启动
- 数据库连接信息是否正确（用户名/密码）
- 是否执行了数据库初始化脚本

### Q: 前端页面无法加载
**A**: 请检查：
- 后端服务是否正常启动（端口8080）
- 前端开发服务器是否启动成功
- 浏览器是否禁用了跨域请求

### Q: 登录后提示权限错误
**A**: 请确保使用预置的测试账户，或检查用户类型设置

## 📞 技术支持

如遇到其他问题，请：
1. 检查控制台错误信息
2. 查看README.md详细文档
3. 确认环境配置是否正确

---

**祝您使用愉快！** 🎉