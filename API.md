# Full Goods 项目 API 文档

## 概述

Full Goods 是一个完整的电商系统，包含管理端和用户端两个主要模块。本文档详细描述了系统提供的所有 API 接口。

## 基础信息

- **基础URL**: `http://localhost:8080`
- **API版本**: v1.0
- **认证方式**: JWT Token
- **数据格式**: JSON
- **字符编码**: UTF-8

## 通用响应格式

所有API接口都遵循统一的响应格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": "2024-01-20T10:30:00Z"
}
```

### 响应字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 响应状态码，200表示成功 |
| message | String | 响应消息 |
| data | Object | 响应数据，具体结构根据接口而定 |
| timestamp | String | 响应时间戳 |

### 常用状态码

| 状态码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权访问 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 认证相关接口

### 用户登录

**接口地址**: `POST /api/auth/login`

**请求参数**:

```json
{
  "username": "admin",
  "password": "123456"
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": 1,
      "username": "admin",
      "nickname": "管理员",
      "email": "admin@example.com",
      "roles": ["ADMIN"]
    }
  }
}
```

### 用户注册

**接口地址**: `POST /api/auth/register`

**请求参数**:

```json
{
  "username": "newuser",
  "password": "123456",
  "email": "newuser@example.com",
  "phone": "13800138000"
}
```

### 退出登录

**接口地址**: `POST /api/auth/logout`

**请求头**: `Authorization: Bearer {token}`

### 刷新Token

**接口地址**: `POST /api/auth/refresh`

**请求头**: `Authorization: Bearer {refresh_token}`

## 用户管理接口

### 获取用户列表

**接口地址**: `GET /api/users`

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| size | Integer | 否 | 每页大小，默认10 |
| keyword | String | 否 | 搜索关键词 |
| status | Integer | 否 | 用户状态 |

**响应示例**:

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "total": 100,
    "pages": 10,
    "current": 1,
    "size": 10,
    "records": [
      {
        "id": 1,
        "username": "user001",
        "nickname": "用户001",
        "email": "user001@example.com",
        "phone": "13800138001",
        "status": 1,
        "createTime": "2024-01-01T00:00:00Z"
      }
    ]
  }
}
```

### 获取用户详情

**接口地址**: `GET /api/users/{id}`

### 创建用户

**接口地址**: `POST /api/users`

### 更新用户

**接口地址**: `PUT /api/users/{id}`

### 删除用户

**接口地址**: `DELETE /api/users/{id}`

## 商品管理接口

### 获取商品列表

**接口地址**: `GET /api/products`

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| size | Integer | 否 | 每页大小，默认10 |
| categoryId | Long | 否 | 分类ID |
| keyword | String | 否 | 搜索关键词 |
| minPrice | BigDecimal | 否 | 最低价格 |
| maxPrice | BigDecimal | 否 | 最高价格 |
| status | Integer | 否 | 商品状态 |

**响应示例**:

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "total": 500,
    "pages": 50,
    "current": 1,
    "size": 10,
    "records": [
      {
        "id": 1,
        "name": "iPhone 15 Pro",
        "description": "苹果最新旗舰手机",
        "price": 7999.00,
        "originalPrice": 8999.00,
        "stock": 100,
        "categoryId": 1,
        "categoryName": "手机数码",
        "images": [
          "https://example.com/image1.jpg",
          "https://example.com/image2.jpg"
        ],
        "status": 1,
        "createTime": "2024-01-01T00:00:00Z"
      }
    ]
  }
}
```

### 获取商品详情

**接口地址**: `GET /api/products/{id}`

### 创建商品

**接口地址**: `POST /api/products`

**请求参数**:

```json
{
  "name": "新商品",
  "description": "商品描述",
  "price": 99.99,
  "originalPrice": 199.99,
  "stock": 100,
  "categoryId": 1,
  "images": [
    "https://example.com/image1.jpg"
  ],
  "attributes": {
    "color": "红色",
    "size": "L"
  }
}
```

### 更新商品

**接口地址**: `PUT /api/products/{id}`

### 删除商品

**接口地址**: `DELETE /api/products/{id}`

### 批量删除商品

**接口地址**: `DELETE /api/products/batch`

**请求参数**:

```json
{
  "ids": [1, 2, 3, 4, 5]
}
```

## 分类管理接口

### 获取分类树

**接口地址**: `GET /api/categories/tree`

**响应示例**:

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "name": "手机数码",
      "parentId": 0,
      "level": 1,
      "sort": 1,
      "icon": "phone",
      "children": [
        {
          "id": 11,
          "name": "手机",
          "parentId": 1,
          "level": 2,
          "sort": 1,
          "children": []
        }
      ]
    }
  ]
}
```

### 获取分类列表

**接口地址**: `GET /api/categories`

### 创建分类

**接口地址**: `POST /api/categories`

### 更新分类

**接口地址**: `PUT /api/categories/{id}`

### 删除分类

**接口地址**: `DELETE /api/categories/{id}`

## 订单管理接口

### 获取订单列表

**接口地址**: `GET /api/orders`

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| size | Integer | 否 | 每页大小，默认10 |
| orderNo | String | 否 | 订单号 |
| status | Integer | 否 | 订单状态 |
| userId | Long | 否 | 用户ID |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |

**响应示例**:

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "total": 200,
    "pages": 20,
    "current": 1,
    "size": 10,
    "records": [
      {
        "id": 1,
        "orderNo": "ORD202401010001",
        "userId": 1,
        "username": "user001",
        "totalAmount": 299.99,
        "payAmount": 299.99,
        "status": 1,
        "statusName": "待付款",
        "payType": 1,
        "payTypeName": "微信支付",
        "createTime": "2024-01-01T10:00:00Z",
        "items": [
          {
            "productId": 1,
            "productName": "iPhone 15 Pro",
            "price": 7999.00,
            "quantity": 1,
            "totalPrice": 7999.00
          }
        ]
      }
    ]
  }
}
```

### 获取订单详情

**接口地址**: `GET /api/orders/{id}`

### 创建订单

**接口地址**: `POST /api/orders`

### 更新订单状态

**接口地址**: `PUT /api/orders/{id}/status`

### 取消订单

**接口地址**: `PUT /api/orders/{id}/cancel`

## 购物车接口

### 获取购物车

**接口地址**: `GET /api/cart`

### 添加到购物车

**接口地址**: `POST /api/cart`

**请求参数**:

```json
{
  "productId": 1,
  "quantity": 2
}
```

### 更新购物车商品数量

**接口地址**: `PUT /api/cart/{id}`

### 删除购物车商品

**接口地址**: `DELETE /api/cart/{id}`

### 清空购物车

**接口地址**: `DELETE /api/cart/clear`

## 文件上传接口

### 上传图片

**接口地址**: `POST /api/upload/image`

**请求类型**: `multipart/form-data`

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| file | File | 是 | 图片文件 |
| type | String | 否 | 图片类型（product/avatar/banner） |

**响应示例**:

```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "url": "https://example.com/uploads/2024/01/01/image.jpg",
    "filename": "image.jpg",
    "size": 1024000
  }
}
```

### 批量上传图片

**接口地址**: `POST /api/upload/images`

## 统计分析接口

### 获取首页统计数据

**接口地址**: `GET /api/statistics/dashboard`

**响应示例**:

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "userCount": 10000,
    "productCount": 5000,
    "orderCount": 20000,
    "todayOrderCount": 100,
    "todayRevenue": 50000.00,
    "monthRevenue": 1500000.00
  }
}
```

### 获取销售统计

**接口地址**: `GET /api/statistics/sales`

### 获取用户统计

**接口地址**: `GET /api/statistics/users`

### 获取商品统计

**接口地址**: `GET /api/statistics/products`

## 系统配置接口

### 获取系统配置

**接口地址**: `GET /api/config`

### 更新系统配置

**接口地址**: `PUT /api/config`

## 错误处理

### 错误响应格式

```json
{
  "code": 400,
  "message": "请求参数错误",
  "data": null,
  "timestamp": "2024-01-20T10:30:00Z",
  "errors": [
    {
      "field": "username",
      "message": "用户名不能为空"
    }
  ]
}
```

### 常见错误码

| 错误码 | 说明 | 解决方案 |
|--------|------|----------|
| 1001 | 用户名或密码错误 | 检查登录凭据 |
| 1002 | Token已过期 | 重新登录获取新Token |
| 1003 | 权限不足 | 联系管理员分配权限 |
| 2001 | 商品不存在 | 检查商品ID是否正确 |
| 2002 | 库存不足 | 减少购买数量 |
| 3001 | 订单不存在 | 检查订单ID是否正确 |
| 3002 | 订单状态不允许此操作 | 检查订单当前状态 |

## 接口限流

为了保护系统稳定性，部分接口实施了限流策略：

- **登录接口**: 每分钟最多5次请求
- **注册接口**: 每分钟最多3次请求
- **上传接口**: 每分钟最多10次请求
- **其他接口**: 每分钟最多100次请求

超出限制时会返回429状态码。

## 接口版本控制

当前API版本为v1.0，后续版本更新时会在URL中体现：

- v1.0: `/api/...`
- v2.0: `/api/v2/...`

## 测试环境

- **测试地址**: `http://test.example.com:8080`
- **测试账号**: 
  - 管理员: `admin` / `123456`
  - 普通用户: `user` / `123456`

## 联系方式

如有API相关问题，请联系开发团队：

- **邮箱**: dev@example.com
- **文档更新时间**: 2024-01-20
- **文档版本**: v1.0