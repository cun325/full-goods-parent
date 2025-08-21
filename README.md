# Full Goods 项目 - 鲜果云销系统

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-1.8+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-5.7+-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-5.0+-red.svg)](https://redis.io/)

这是一个基于Spring Boot的多模块电商系统，包含管理后台、API服务、前端应用和图片服务。系统采用微服务架构，支持商品管理、用户管理、订单处理、智能推荐等功能。

## 项目结构

```
full-goods-parent
├── full-goods-common           -- 公共模块（工具类、异常处理、配置等）
├── full-goods-admin            -- 管理后台服务（8080端口）
├── full-goods-api              -- API服务（8081端口）
├── full-goods-image-service    -- 图片服务（文件上传、图片处理）
├── front-end/
│   ├── shopapp-admin          -- 管理后台前端（Vue.js）
│   └── fruitshop-uniapp       -- 移动端应用（uni-app）
├── logs/                      -- 日志文件目录
├── docker-compose.yml         -- Docker容器编排
├── Dockerfile                 -- Docker镜像构建
├── nginx.conf                 -- Nginx配置
├── redis.conf                 -- Redis配置
├── prometheus.yml             -- 监控配置
└── API.md                     -- API接口文档
```

## 技术栈

### 后端技术
- **Spring Boot 2.7.5** - 主框架
- **MyBatis** - ORM框架
- **MySQL 5.7+** - 关系型数据库
- **Redis 5.0+** - 缓存数据库
- **Druid** - 数据库连接池
- **Swagger** - API文档生成
- **Logback** - 日志框架
- **Maven** - 项目构建工具

### 前端技术
- **Vue.js 3** - 管理后台前端框架
- **uni-app** - 跨平台移动应用开发
- **Element Plus** - UI组件库
- **Axios** - HTTP客户端
- **Webpack** - 模块打包工具

### 运维技术
- **Docker** - 容器化部署
- **Nginx** - 反向代理和负载均衡
- **Prometheus** - 监控系统
- **Redis Sentinel** - 高可用方案

## 模块说明

### full-goods-common

公共模块，包含以下内容：

- **通用响应类** - 统一API响应格式
- **全局异常处理** - 统一异常处理和错误码管理
- **基础实体类** - 通用实体类和数据传输对象
- **数据库配置** - 数据源配置和连接池设置
- **工具类** - 包含以下工具：
  - `LogUtils` - 日志记录工具
  - `SecurityUtils` - 安全工具（加密、验证等）
  - `ValidationUtils` - 输入验证工具
  - `PermissionUtils` - 权限管理工具
  - `RateLimitUtils` - API限流工具

### full-goods-admin

管理后台服务，运行在8080端口：
- 商品管理（增删改查、分类管理）
- 用户管理（用户信息、权限管理）
- 订单管理（订单处理、状态跟踪）
- 系统配置（参数设置、日志查看）
- 数据统计（销售报表、用户分析）

### full-goods-api

API服务，运行在8081端口：
- 用户认证（注册、登录、权限验证）
- 商品服务（商品查询、推荐算法）
- 订单服务（下单、支付、物流）
- 购物车服务（商品管理、批量操作）
- 文件服务（图片上传、文件管理）

### full-goods-image-service

图片服务模块：
- 图片上传和存储
- 图片格式转换和压缩
- 图片访问和缓存
- 文件安全验证

## 运行说明

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- Redis 5.0+

### 数据库配置

1. 创建数据库：full_goods
2. 执行SQL脚本：full-goods-api/src/main/resources/db/user.sql
3. 修改数据库连接配置（位于各模块的application.yml中）

### 用户表设计

用户表(t_user)结构如下：

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint | 用户ID |
| mobile | varchar(11) | 手机号 |
| password | varchar(32) | 密码(MD5加密) |
| username | varchar(50) | 用户名 |
| nickname | varchar(50) | 昵称 |
| avatar | varchar(255) | 头像 |
| status | tinyint(1) | 状态：0-禁用，1-正常 |
| last_login_time | datetime | 最后登录时间 |
| last_login_ip | varchar(50) | 最后登录IP |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |
| create_by | varchar(50) | 创建人 |
| update_by | varchar(50) | 更新人 |
| remark | varchar(255) | 备注 |

### 快速启动

#### 方式一：Docker 部署（推荐）

```bash
# 克隆项目
git clone <repository-url>
cd full-goods-parent

# 使用Docker Compose启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps
```

#### 方式二：本地开发环境

**后端服务启动：**

```bash
# 构建项目
mvn clean package

# 启动管理后台
cd full-goods-admin
mvn spring-boot:run

# 启动API服务
cd full-goods-api
mvn spring-boot:run

# 启动图片服务
cd full-goods-image-service
mvn spring-boot:run
```

**前端应用启动：**

```bash
# 启动管理后台前端
cd front-end/shopapp-admin
npm install
npm run serve

# 启动移动端应用
cd front-end/fruitshop-uniapp
npm install
npm run dev:h5
```

### 访问地址

| 服务 | 地址 | 说明 |
|------|------|------|
| 管理后台前端 | http://localhost:3000 | Vue.js管理界面 |
| 移动端应用 | http://localhost:3002 | uni-app H5版本 |
| 管理后台API | http://localhost:8080 | 后台管理服务 |
| 用户API服务 | http://localhost:8081 | 用户端API |
| 图片服务 | http://localhost:8082 | 文件上传服务 |
| API文档 | http://localhost:8081/swagger-ui/index.html | Swagger接口文档 |
| 数据库监控 | http://localhost:8081/druid | Druid监控面板 |

## 功能说明

### 用户功能

- 用户注册：支持手机号注册，密码MD5加密存储
- 用户登录：支持手机号+密码登录，返回token
- 验证码：支持发送和验证短信验证码（使用Redis存储）
- 密码管理：支持重置密码和修改密码
- 用户信息：支持查询和更新用户信息

### 智能选果助手

- 水果推荐：根据用户需求智能推荐合适的水果
- 条件筛选：支持按照口感、产地、适用人群等多维度筛选
- 历史记录：记录用户的推荐历史，方便复用

### 接口列表

#### 用户接口

- POST /user/register - 用户注册
- POST /user/login - 用户登录
- POST /user/sendVerifyCode - 发送验证码
- POST /user/resetPassword - 重置密码
- POST /user/checkMobile - 检查手机号是否已注册
- POST /user/info - 获取用户信息
- POST /user/logout - 用户登出
- POST /user/update - 更新用户信息
- POST /user/changePassword - 修改密码

#### 水果接口

- GET /fruit/list - 获取所有水果
- GET /fruit/{id} - 根据ID获取水果
- POST /fruit/recommend - 智能推荐水果
- GET /fruit/history - 获取推荐历史

## 开发指南

### 添加新功能

1. 在common模块中添加公共实体类和工具类
2. 在admin或api模块中添加业务逻辑
3. 遵循分层架构：Controller -> Service -> Mapper

### 代码规范

- 遵循阿里巴巴Java开发手册
- 使用Lombok简化代码
- 保持代码整洁，添加必要的注释

### 测试说明

项目包含以下测试类：

1. UserMapperTest：测试用户Mapper接口
2. RedisTest：测试Redis配置和操作
3. UserServiceTest：测试用户服务接口
4. FruitServiceTest：测试水果推荐服务接口

## 性能优化

### 数据库优化
- **连接池优化**：使用Druid连接池，配置合理的连接数和超时时间
- **查询优化**：添加必要的数据库索引，优化慢查询
- **分页查询**：使用MyBatis PageHelper实现高效分页
- **读写分离**：支持主从数据库配置

### 缓存策略
- **Redis缓存**：热点数据缓存，减少数据库压力
- **本地缓存**：使用Caffeine实现应用级缓存
- **缓存预热**：系统启动时预加载常用数据
- **缓存更新**：实现缓存与数据库的一致性

### 前端优化
- **代码分割**：按路由和组件进行代码分割
- **资源压缩**：CSS、JS文件压缩和合并
- **图片优化**：支持WebP格式，实现懒加载
- **CDN加速**：静态资源CDN分发

## 安全特性

### 数据安全
- **密码加密**：使用MD5+盐值加密用户密码
- **数据脱敏**：敏感信息（手机号、邮箱）自动脱敏
- **SQL注入防护**：参数化查询，防止SQL注入
- **XSS防护**：输入数据过滤和转义

### 访问控制
- **权限管理**：基于角色的权限控制（RBAC）
- **API限流**：防止恶意请求和DDoS攻击
- **输入验证**：严格的参数校验和格式检查
- **文件安全**：上传文件类型和大小限制

## 监控与运维

### 日志管理
- **分级日志**：ERROR、WARN、INFO、DEBUG四个级别
- **日志轮转**：按日期和大小自动轮转日志文件
- **异常追踪**：详细的异常堆栈信息记录
- **业务日志**：关键业务操作的审计日志

### 性能监控
- **应用监控**：使用Prometheus监控应用指标
- **数据库监控**：Druid监控数据库连接和查询性能
- **Redis监控**：监控缓存命中率和内存使用
- **系统监控**：CPU、内存、磁盘等系统资源监控

### 健康检查
- **应用健康检查**：Spring Boot Actuator健康端点
- **数据库连接检查**：定期检查数据库连接状态
- **Redis连接检查**：监控缓存服务可用性
- **外部服务检查**：第三方服务依赖检查

## 部署说明

### Docker部署

```bash
# 构建镜像
docker build -t full-goods:latest .

# 运行容器
docker run -d -p 8080:8080 -p 8081:8081 full-goods:latest

# 使用docker-compose
docker-compose up -d
```

### 生产环境配置

1. **数据库配置**：配置生产环境数据库连接
2. **Redis配置**：配置Redis集群或哨兵模式
3. **日志配置**：配置日志输出路径和级别
4. **监控配置**：配置Prometheus和Grafana
5. **负载均衡**：配置Nginx负载均衡

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| MYSQL_HOST | MySQL主机地址 | localhost |
| MYSQL_PORT | MySQL端口 | 3306 |
| MYSQL_DATABASE | 数据库名 | full_goods |
| REDIS_HOST | Redis主机地址 | localhost |
| REDIS_PORT | Redis端口 | 6379 |
| LOG_LEVEL | 日志级别 | INFO |

## 常见问题

### Q: 启动时提示数据库连接失败？
A: 请检查MySQL服务是否启动，数据库配置是否正确。

### Q: Redis连接失败？
A: 请确认Redis服务已启动，检查连接配置和防火墙设置。

### Q: 前端页面无法访问？
A: 请确认前端服务已启动，检查端口是否被占用。

### Q: 图片上传失败？
A: 请检查上传目录权限，确认文件大小未超过限制。

## 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 联系方式

- 项目维护者：[Your Name]
- 邮箱：[your.email@example.com]
- 项目地址：[https://github.com/your-username/full-goods-parent]

## 更新日志

### v1.2.0 (2024-01-20)
- ✨ 新增图片服务模块
- ✨ 添加API限流功能
- ✨ 增强安全工具类
- 🐛 修复用户登录异常
- 📝 完善API文档

### v1.1.0 (2024-01-15)
- ✨ 新增权限管理系统
- ✨ 添加数据验证工具
- 🚀 优化数据库查询性能
- 📝 更新项目文档

### v1.0.0 (2024-01-10)
- 🎉 项目初始版本发布
- ✨ 基础用户管理功能
- ✨ 商品管理功能
- ✨ 订单处理功能