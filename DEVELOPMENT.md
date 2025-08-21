# Full Goods 开发指南

本文档为 Full Goods 项目的开发指南，包含代码规范、开发流程、最佳实践等内容。

## 目录

- [开发环境搭建](#开发环境搭建)
- [代码规范](#代码规范)
- [开发流程](#开发流程)
- [数据库设计规范](#数据库设计规范)
- [API设计规范](#api设计规范)
- [前端开发规范](#前端开发规范)
- [测试规范](#测试规范)
- [性能优化指南](#性能优化指南)
- [安全开发规范](#安全开发规范)
- [故障排查指南](#故障排查指南)

## 开发环境搭建

### 必需软件

- **JDK 1.8+**：Java开发环境
- **Maven 3.6+**：项目构建工具
- **MySQL 5.7+**：关系型数据库
- **Redis 5.0+**：缓存数据库
- **Node.js 14+**：前端开发环境
- **Git**：版本控制工具

### 推荐IDE

- **IntelliJ IDEA**：Java开发（推荐）
- **VS Code**：前端开发
- **Navicat**：数据库管理工具
- **Redis Desktop Manager**：Redis管理工具

### 开发环境配置

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd full-goods-parent
   ```

2. **数据库初始化**
   ```sql
   CREATE DATABASE full_goods CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Redis配置**
   ```bash
   # 启动Redis服务
   redis-server
   ```

4. **配置文件修改**
   - 修改 `application.yml` 中的数据库连接信息
   - 修改 Redis 连接配置

## 代码规范

### Java代码规范

#### 命名规范

- **类名**：使用大驼峰命名法（PascalCase）
  ```java
  public class UserService {}
  public class OrderController {}
  ```

- **方法名**：使用小驼峰命名法（camelCase）
  ```java
  public void getUserInfo() {}
  public List<User> findUsersByStatus(int status) {}
  ```

- **变量名**：使用小驼峰命名法
  ```java
  private String userName;
  private List<Order> orderList;
  ```

- **常量名**：使用全大写，下划线分隔
  ```java
  public static final String DEFAULT_CHARSET = "UTF-8";
  public static final int MAX_RETRY_COUNT = 3;
  ```

#### 注释规范

- **类注释**：
  ```java
  /**
   * 用户服务类
   * 提供用户相关的业务逻辑处理
   * 
   * @author Your Name
   * @since 1.0.0
   */
  public class UserService {
  }
  ```

- **方法注释**：
  ```java
  /**
   * 根据用户ID获取用户信息
   * 
   * @param userId 用户ID
   * @return 用户信息，如果不存在返回null
   * @throws IllegalArgumentException 当userId为空时抛出
   */
  public User getUserById(Long userId) {
  }
  ```

#### 异常处理

- 使用统一的异常处理机制
- 不要捕获并忽略异常
- 记录详细的异常信息

```java
try {
    // 业务逻辑
} catch (SpecificException e) {
    LogUtils.error("操作失败", e);
    throw new BusinessException("操作失败：" + e.getMessage());
}
```

### 前端代码规范

#### Vue.js规范

- **组件命名**：使用PascalCase
  ```javascript
  // 好的例子
  export default {
    name: 'UserProfile'
  }
  ```

- **Props定义**：明确类型和默认值
  ```javascript
  props: {
    userId: {
      type: [String, Number],
      required: true
    },
    showAvatar: {
      type: Boolean,
      default: true
    }
  }
  ```

- **事件命名**：使用kebab-case
  ```javascript
  this.$emit('user-updated', userData);
  ```

## 开发流程

### Git工作流

1. **分支策略**
   - `main`：主分支，用于生产环境
   - `develop`：开发分支，用于集成测试
   - `feature/*`：功能分支，用于新功能开发
   - `hotfix/*`：热修复分支，用于紧急修复

2. **提交规范**
   ```bash
   # 格式：<type>(<scope>): <subject>
   feat(user): 添加用户注册功能
   fix(order): 修复订单状态更新问题
   docs(readme): 更新项目文档
   style(css): 调整按钮样式
   refactor(service): 重构用户服务代码
   test(unit): 添加用户服务单元测试
   ```

3. **代码审查**
   - 所有代码必须经过Code Review
   - 检查代码规范、逻辑正确性、性能问题
   - 确保测试覆盖率达到要求

### 开发步骤

1. **需求分析**
   - 理解业务需求
   - 设计技术方案
   - 评估开发工作量

2. **数据库设计**
   - 设计表结构
   - 添加必要索引
   - 编写SQL脚本

3. **后端开发**
   - 创建实体类
   - 编写Mapper接口
   - 实现Service层
   - 开发Controller层

4. **前端开发**
   - 设计页面布局
   - 实现组件功能
   - 调用后端API
   - 处理异常情况

5. **测试验证**
   - 单元测试
   - 集成测试
   - 功能测试
   - 性能测试

## 数据库设计规范

### 表设计规范

1. **命名规范**
   - 表名：使用小写字母和下划线，如 `t_user`、`t_order`
   - 字段名：使用小写字母和下划线，如 `user_name`、`create_time`

2. **字段规范**
   - 主键：统一使用 `id` 作为主键，类型为 `bigint`
   - 创建时间：`create_time datetime`
   - 更新时间：`update_time datetime`
   - 创建人：`create_by varchar(50)`
   - 更新人：`update_by varchar(50)`
   - 备注：`remark varchar(255)`

3. **索引规范**
   - 为经常查询的字段添加索引
   - 复合索引遵循最左前缀原则
   - 避免过多索引影响写入性能

### SQL编写规范

1. **查询优化**
   ```sql
   -- 好的例子：使用索引字段查询
   SELECT * FROM t_user WHERE mobile = '13800138000';
   
   -- 避免：使用函数导致索引失效
   SELECT * FROM t_user WHERE DATE(create_time) = '2024-01-01';
   ```

2. **分页查询**
   ```sql
   -- 使用LIMIT进行分页
   SELECT * FROM t_user ORDER BY id DESC LIMIT 10 OFFSET 20;
   ```

## API设计规范

### RESTful API规范

1. **URL设计**
   ```
   GET    /api/users          # 获取用户列表
   GET    /api/users/{id}     # 获取指定用户
   POST   /api/users          # 创建用户
   PUT    /api/users/{id}     # 更新用户
   DELETE /api/users/{id}     # 删除用户
   ```

2. **响应格式**
   ```json
   {
     "code": 200,
     "message": "操作成功",
     "data": {
       "id": 1,
       "username": "admin"
     },
     "timestamp": "2024-01-01T12:00:00Z"
   }
   ```

3. **错误处理**
   ```json
   {
     "code": 400,
     "message": "参数错误",
     "data": null,
     "errors": [
       {
         "field": "username",
         "message": "用户名不能为空"
       }
     ]
   }
   ```

### 接口文档

- 使用Swagger生成API文档
- 详细描述接口功能、参数、响应
- 提供示例请求和响应

## 前端开发规范

### 组件开发

1. **组件结构**
   ```vue
   <template>
     <!-- 模板内容 -->
   </template>
   
   <script>
   export default {
     name: 'ComponentName',
     props: {},
     data() {
       return {}
     },
     computed: {},
     methods: {},
     mounted() {}
   }
   </script>
   
   <style scoped>
   /* 样式内容 */
   </style>
   ```

2. **状态管理**
   - 使用Vuex管理全局状态
   - 组件内部状态使用data
   - 避免直接修改props

3. **路由管理**
   - 使用Vue Router进行路由管理
   - 实现路由懒加载
   - 添加路由守卫

### 样式规范

1. **CSS命名**
   - 使用BEM命名规范
   - 避免使用ID选择器
   - 使用有意义的类名

2. **响应式设计**
   - 使用媒体查询适配不同屏幕
   - 优先考虑移动端体验
   - 使用相对单位（rem、em、%）

## 测试规范

### 单元测试

1. **测试覆盖率**
   - Service层测试覆盖率 > 80%
   - Controller层测试覆盖率 > 70%
   - 工具类测试覆盖率 > 90%

2. **测试命名**
   ```java
   @Test
   public void testGetUserById_WhenUserExists_ShouldReturnUser() {
       // 测试逻辑
   }
   
   @Test
   public void testGetUserById_WhenUserNotExists_ShouldReturnNull() {
       // 测试逻辑
   }
   ```

3. **Mock使用**
   ```java
   @Mock
   private UserMapper userMapper;
   
   @Test
   public void testGetUserById() {
       // Given
       Long userId = 1L;
       User expectedUser = new User();
       when(userMapper.selectById(userId)).thenReturn(expectedUser);
       
       // When
       User actualUser = userService.getUserById(userId);
       
       // Then
       assertEquals(expectedUser, actualUser);
   }
   ```

### 集成测试

1. **API测试**
   - 测试完整的请求响应流程
   - 验证数据库操作
   - 检查异常处理

2. **数据库测试**
   - 使用测试数据库
   - 每个测试方法独立
   - 测试后清理数据

## 性能优化指南

### 后端优化

1. **数据库优化**
   - 合理使用索引
   - 避免N+1查询问题
   - 使用连接池
   - 实现读写分离

2. **缓存策略**
   - 热点数据缓存
   - 设置合理的过期时间
   - 缓存穿透防护
   - 缓存雪崩防护

3. **代码优化**
   - 避免在循环中进行数据库操作
   - 使用批量操作
   - 合理使用线程池
   - 避免内存泄漏

### 前端优化

1. **加载优化**
   - 代码分割和懒加载
   - 图片懒加载
   - 资源压缩
   - CDN加速

2. **渲染优化**
   - 虚拟滚动
   - 防抖和节流
   - 避免不必要的重渲染
   - 使用Web Workers

## 安全开发规范

### 输入验证

1. **参数校验**
   ```java
   @PostMapping("/users")
   public Result createUser(@Valid @RequestBody UserCreateRequest request) {
       // 业务逻辑
   }
   ```

2. **SQL注入防护**
   ```java
   // 使用参数化查询
   @Select("SELECT * FROM t_user WHERE username = #{username}")
   User findByUsername(@Param("username") String username);
   ```

3. **XSS防护**
   ```java
   // 输入过滤
   String safeContent = ValidationUtils.cleanHtml(userInput);
   ```

### 权限控制

1. **接口权限**
   ```java
   @PreAuthorize("hasRole('ADMIN')")
   @GetMapping("/admin/users")
   public Result getUsers() {
       // 管理员才能访问
   }
   ```

2. **数据权限**
   ```java
   // 只能查看自己的数据
   public List<Order> getUserOrders(Long userId) {
       if (!SecurityUtils.getCurrentUserId().equals(userId)) {
           throw new AccessDeniedException("无权访问");
       }
       return orderService.getOrdersByUserId(userId);
   }
   ```

## 故障排查指南

### 常见问题

1. **数据库连接问题**
   - 检查数据库服务状态
   - 验证连接配置
   - 查看连接池状态

2. **Redis连接问题**
   - 检查Redis服务状态
   - 验证连接配置
   - 查看网络连通性

3. **内存溢出**
   - 分析堆转储文件
   - 检查内存泄漏
   - 调整JVM参数

4. **性能问题**
   - 分析慢查询日志
   - 检查缓存命中率
   - 监控系统资源

### 日志分析

1. **日志级别**
   - ERROR：系统错误，需要立即处理
   - WARN：警告信息，需要关注
   - INFO：一般信息，记录关键操作
   - DEBUG：调试信息，开发环境使用

2. **日志格式**
   ```
   [时间] [级别] [线程] [类名] - [消息]
   2024-01-01 12:00:00.123 INFO [http-nio-8080-exec-1] c.e.s.UserService - 用户登录成功: userId=1
   ```

### 监控指标

1. **应用指标**
   - QPS（每秒请求数）
   - 响应时间
   - 错误率
   - 活跃用户数

2. **系统指标**
   - CPU使用率
   - 内存使用率
   - 磁盘IO
   - 网络IO

3. **数据库指标**
   - 连接数
   - 查询时间
   - 锁等待
   - 缓存命中率

## 总结

本开发指南涵盖了Full Goods项目开发的各个方面，包括环境搭建、代码规范、开发流程、测试规范、性能优化和安全规范等。遵循这些规范和最佳实践，可以提高代码质量，减少bug，提升开发效率。

建议开发团队定期回顾和更新这些规范，确保与项目发展保持同步。同时，鼓励团队成员分享经验和最佳实践，持续改进开发流程。