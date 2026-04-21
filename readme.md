# 分布式秒杀系统 (Seckill-System)

一个基于 Spring Boot + Vue 3 的分布式秒杀电商系统，支持消费者、商家、管理员三种角色。

---

## 项目简介

本项目是一个完整的分布式秒杀（闪购）电商系统，采用前后端分离架构。后端基于 Spring Boot 2.7.18 构建，前端使用 Vue 3 + Element Plus 开发。

系统实现了完整的用户体系、商品管理、秒杀活动、订单处理等功能，适用于电商平台的秒杀场景。

---

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.18 | 核心框架 |
| MySQL | 8.0 | 关系型数据库 |
| Redis | - | 缓存、分布式会话 |
| RabbitMQ | 3.12 | 消息队列（可选） |
| ZooKeeper | 3.7 | 分布式协调服务 |
| MyBatis | 3.5.14 | ORM 持久层框架 |
| JWT | 0.11.5 | 无状态身份认证 |
| FastJSON2 | 2.0.47 | JSON 序列化 |
| Curator | 5.5.0 | ZooKeeper 客户端 |
| Lombok | - | 简化代码 |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue 3 | 3.4 | 渐进式 JavaScript 框架 |
| Vite | 5.4 | 下一代前端构建工具 |
| Element Plus | 2.5 | Vue 3 UI 组件库 |
| Pinia | 2.1 | Vue 3 状态管理 |
| Vue Router | 4.2 | Vue 3 官方路由 |
| Axios | - | HTTP 请求库 |

### 开发工具

| 工具 | 说明 |
|------|------|
| JDK | 1.8+ |
| Maven | 3.6+ |
| Node.js | 18+ |
| MySQL | 8.0+ |

---

## 系统架构

```
┌─────────────────────────────────────────────────────────────┐
│                        前端 (Vue 3)                         │
│                    http://localhost:5173                    │
└────────────────────────────┬────────────────────────────────┘
                             │ HTTP / JSON
                             ▼
┌─────────────────────────────────────────────────────────────┐
│                     后端 (Spring Boot)                      │
│                     http://localhost:8088                   │
│                                                             │
│  ┌───────────┐  ┌───────────┐  ┌───────────┐             │
│  │ Consumer  │  │ Merchant  │  │  Admin    │             │
│  │   API     │  │   API     │  │   API     │             │
│  └─────┬─────┘  └─────┬─────┘  └─────┬─────┘             │
│        │              │              │                     │
│  ┌─────┴──────────────┴──────────────┴─────┐              │
│  │            Service Layer                │              │
│  └─────────────────┬──────────────────────┘              │
│                    │                                       │
│  ┌─────┴─────┐ ┌───┴────┐ ┌────────────┐                 │
│  │   MySQL   │ │  Redis │ │   ZooKeeper │                │
│  └───────────┘ └────────┘ └────────────┘                 │
└─────────────────────────────────────────────────────────────┘
```

---

## 功能模块

### 1. 消费者模块 (Consumer)

- **首页浏览**：查看商品列表、秒杀活动
- **商品详情**：查看商品详细信息
- **秒杀抢购**：参与限时秒杀活动
- **我的订单**：查看、支付、取消订单
- **个人信息**：修改个人资料、修改密码
- **系统通知**：查看平台公告

### 2. 商家模块 (Merchant)

- **商家后台**：销售数据概览
- **商品管理**：添加、编辑、上架/下架商品
- **活动管理**：创建秒杀活动、查看活动状态
- **订单管理**：查看订单、发货
- **公告管理**：发布店铺公告
- **密码修改**：修改登录密码

### 3. 管理员模块 (Admin)

- **管理后台**：用户数据、商品数据概览
- **用户管理**：查看/启用/禁用用户（消费者、商家）
- **商家管理**：审核商家、管理商家状态
- **商品管理**：审核商品、上下架管理
- **活动审核**：审核秒杀活动、停止进行中的活动
- **订单管理**：查看所有订单
- **公告管理**：发布系统公告
- **密码修改**：修改管理员密码

---

## 数据库设计

### 用户表 (t_user)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 用户ID |
| username | VARCHAR(64) | 用户名（唯一） |
| password | VARCHAR(128) | 密码（MD5加密） |
| salt | VARCHAR(32) | 盐值 |
| nickname | VARCHAR(64) | 昵称 |
| phone | VARCHAR(20) | 手机号 |
| email | VARCHAR(128) | 邮箱 |
| role | TINYINT | 角色：0-消费者 1-商家 2-管理员 |
| status | TINYINT | 状态：0-禁用 1-正常 |
| avatar | VARCHAR(256) | 头像URL |
| create_time | DATETIME | 创建时间 |

### 商品表 (t_goods)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 商品ID |
| merchant_id | BIGINT | 商家ID |
| goods_name | VARCHAR(256) | 商品名称 |
| goods_title | VARCHAR(512) | 商品标题 |
| goods_img | VARCHAR(512) | 商品图片URL |
| goods_detail | TEXT | 商品详情 |
| goods_price | DECIMAL(10,2) | 原价 |
| seckill_price | DECIMAL(10,2) | 秒杀价 |
| stock_count | INT | 库存数量 |
| status | TINYINT | 状态：0-下架 1-上架 |
| category | VARCHAR(64) | 分类 |

### 秒杀活动表 (t_seckill_activity)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 活动ID |
| goods_id | BIGINT | 商品ID |
| activity_name | VARCHAR(256) | 活动名称 |
| seckill_price | DECIMAL(10,2) | 秒杀价格 |
| stock_count | INT | 秒杀库存 |
| start_time | DATETIME | 开始时间 |
| end_time | DATETIME | 结束时间 |
| status | TINYINT | 状态：0-待审核 1-进行中 2-已结束 3-已通过 4-已拒绝 |
| limit_count | INT | 每人限购数量 |

### 订单表 (t_seckill_order)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 订单ID |
| user_id | BIGINT | 用户ID |
| goods_id | BIGINT | 商品ID |
| activity_id | BIGINT | 活动ID |
| order_no | VARCHAR(64) | 订单号（唯一） |
| goods_price | DECIMAL(10,2) | 商品价格 |
| status | TINYINT | 状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消 5-已退款 |
| create_time | DATETIME | 创建时间 |
| pay_time | DATETIME | 支付时间 |

### 公告表 (t_notice)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 公告ID |
| title | VARCHAR(256) | 标题 |
| content | TEXT | 内容 |
| status | TINYINT | 状态：0-草稿 1-已发布 |
| create_time | DATETIME | 创建时间 |

---

## 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 123456 |
| 商家 | merchant1 | 123456 |
| 消费者 | user1 | 123456 |

---

## 部署运行

### 环境要求

- JDK 1.8+
- Maven 3.6+
- Node.js 18+
- MySQL 8.0+
- Redis（可选，用于缓存）
- ZooKeeper 3.7+（用于分布式协调）

### 1. 数据库配置

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS seckill DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE seckill;

-- 执行初始化脚本
SOURCE sql/init.sql;
```

初始化脚本位于 `sql/init.sql`，包含完整的表结构和初始数据。

### 2. 修改配置文件

**后端配置**：`backend/src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/seckill?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456  # 修改为你的MySQL密码

  redis:
    host: localhost
    port: 6379
    password:          # Redis密码，无密码则留空

  zookeeper:
    connect-string: localhost:2181
```

**前端配置**：`frontend/vite.config.js`

```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8088',  // 后端地址
    changeOrigin: true
  }
}
```

### 3. 启动后端

```bash
cd backend

# 编译项目
mvn clean compile

# 启动服务
mvn spring-boot:run
```

后端启动后运行在 http://localhost:8088

### 4. 启动前端

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端启动后运行在 http://localhost:5173

### 5. 访问系统

打开浏览器访问 http://localhost:5173 ，系统会自动跳转到登录页面。

---

## 项目结构

```
seckill-system/
├── backend/                          # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/seckill/
│   │       │   ├── SeckillApplication.java    # 启动类
│   │       │   ├── config/                     # 配置类
│   │       │   ├── controller/                  # 控制器
│   │       │   ├── service/                     # 服务层
│   │       │   ├── mapper/                      # MyBatis Mapper
│   │       │   ├── entity/                      # 实体类
│   │       │   ├── dto/                        # 数据传输对象
│   │       │   ├── util/                       # 工具类
│   │       │   └── interceptor/                # 拦截器
│   │       └── resources/
│   │           ├── application.yml              # 配置文件
│   │           ├── mapper/                      # MyBatis XML映射文件
│   │           └── static/images/              # 静态图片资源
│   └── pom.xml
│
├── frontend/                         # 前端项目
│   ├── src/
│   │   ├── api/                      # API 接口
│   │   ├── assets/                   # 静态资源
│   │   ├── components/               # 公共组件
│   │   ├── router/                   # 路由配置
│   │   ├── stores/                   # Pinia 状态管理
│   │   ├── views/                    # 页面组件
│   │   │   ├── admin/                # 管理后台页面
│   │   │   ├── merchant/             # 商家后台页面
│   │   │   ├── consumer/              # 消费者页面
│   │   │   └── common/               # 公共页面（登录、注册）
│   │   ├── App.vue
│   │   └── main.js
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
│
├── sql/                              # SQL 脚本
│   └── init.sql                      # 数据库初始化脚本
│
└── readme.md                         # 项目说明文档
```

---

## API 接口

### 认证接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/user/login | 用户登录 |
| POST | /api/user/register | 用户注册 |
| GET | /api/user/current | 获取当前用户信息 |

### 商品接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/goods/list | 获取商品列表 |
| GET | /api/goods/detail/{id} | 获取商品详情 |

### 秒杀接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/seckill/activities | 获取秒杀活动列表 |
| GET | /api/seckill/activity/{id} | 获取活动详情 |
| POST | /api/seckill/execute | 执行秒杀 |
| GET | /api/seckill/my-orders | 我的订单 |
| POST | /api/seckill/pay/{id} | 支付订单 |
| POST | /api/seckill/cancel/{id} | 取消订单 |

### 管理接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/admin/users | 用户列表 |
| PUT | /api/admin/user/{id}/status | 更新用户状态 |
| GET | /api/admin/activities | 活动列表（审核） |
| PUT | /api/admin/activity/{id}/audit | 审核活动 |

---

## 核心特性

1. **分布式架构**：基于 ZooKeeper 实现分布式协调，支持集群部署
2. **高性能缓存**：Redis 缓存热门数据，减少数据库压力
3. **消息队列**：RabbitMQ 实现异步订单处理（需自行配置启用）
4. **JWT 认证**：无状态身份认证，支持集群部署
5. **角色权限**：三种角色（消费者、商家、管理员），权限分明
6. **实时秒杀**：倒计时、库存控制、限购策略
7. **订单管理**：完整的订单状态流转（待支付→已支付→已发货→已完成）
8. **响应式设计**：适配多种屏幕尺寸

---

## 注意事项

1. **MySQL**：确保 MySQL 8.0+ 已安装，并创建 `seckill` 数据库
2. **Redis**：如启用缓存，需启动 Redis 服务
3. **ZooKeeper**：如启用分布式协调，需启动 ZooKeeper 服务
4. **RabbitMQ**：消息队列功能默认禁用，如需启用请在 `application.yml` 中配置
5. **图片资源**：商品图片放在 `backend/src/main/resources/static/images/` 目录
6. **端口占用**：8088（后端）、5173（前端）端口不能被其他程序占用

---

## 常见问题

**Q: 启动后端报 Port 8088 已被占用？**
A: 关闭占用 8088 端口的程序，或修改 `application.yml` 中的 `server.port` 为其他端口。

**Q: 前端无法访问后端 API？**
A: 检查 `vite.config.js` 中的 proxy 配置，确保指向正确的后端地址。

**Q: 商品图片不显示？**
A: 检查数据库 `goods_img` 字段的 URL 是否正确，图片文件是否放在 `static/images/` 目录。

**Q: 登录失败？**
A: 确认数据库中用户的密码是 MD5(密码+salt) 格式，默认密码为 123456。

---

## 开源协议

本项目仅供学习参考，如需用于生产环境请自行承担风险。