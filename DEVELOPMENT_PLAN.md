# 青循环 (QingYa) - 校园二手交易平台 开发计划

## 项目概况

| 项 | 内容 |
|---|---|
| **项目名称** | 青循环 - 校园二手交易平台 |
| **仓库地址** | https://github.com/houlixiete304/qingxunhuan.git |
| **后端** | Spring Boot 4.0.6 + Java 17 + Maven |
| **Web管理端** | Vue 3 + Vite 8 |
| **小程序端** | 原生微信小程序 |
| **数据库** | MySQL 8.0 |
| **缓存** | Redis |

## 当前状态

三个子项目均为脚手架模板状态，仅有默认示例代码，无任何业务功能：
- `backend/` — Spring Boot 空项目，无 web/data/security 依赖
- `frontend/` — Vue 3 + Vite 空项目，无 router/ui/http 依赖
- `miniapp/` — 原生小程序模板，仅有 index 和 logs 示例页

---

## 总体开发阶段

```
Phase 1: 基础设施搭建
  → Phase 2: 用户与认证模块
    → Phase 3: 商品模块
      → Phase 4: 交易/订单模块
        → Phase 5: 消息/聊天模块
          → Phase 6: 后台管理模块
            → Phase 7: 集成联调与收尾
```

---

## Phase 1: 基础设施搭建

### Step 1.1 — 后端项目基础配置
**目标**: 引入所需依赖，完成基础配置，项目可启动

**具体操作**:
1. `pom.xml` 添加依赖：
   - `spring-boot-starter-web` — Web 框架
   - `mybatis-plus-boot-starter` — ORM
   - `mysql-connector-j` — MySQL 驱动
   - `spring-boot-starter-data-redis` — Redis 缓存
   - `spring-boot-starter-security` — 安全框架
   - `java-jwt` (com.auth0) — JWT 认证
   - `spring-boot-starter-validation` — 参数校验
   - `hutool-all` — 工具库
   - `knife4j-openapi3` — API 文档
2. `application.yaml` 配置：
   - MySQL 数据源连接 (数据库名: `qingxunhuan`)
   - Redis 连接
   - 服务端口 `8080`
   - MyBatis-Plus 配置
3. 创建基础包结构：
   ```
   com.qingya.qingxunhuan
   ├── config/        # 配置类
   ├── controller/    # 控制器
   ├── service/       # 业务层
   │   └── impl/
   ├── mapper/        # MyBatis Mapper
   ├── entity/        # 实体类
   ├── dto/           # 数据传输对象
   ├── vo/            # 视图对象
   ├── common/        # 公共类(统一返回、异常、常量)
   └── utils/         # 工具类
   ```
4. 创建 `common/Result.java` — 统一返回体
5. 创建 `common/GlobalExceptionHandler.java` — 全局异常处理
6. 创建 `config/CorsConfig.java` — 跨域配置
7. 验证服务可启动

**✅ 验证**: `mvnw spring-boot:run` 启动成功

---

### Step 1.2 — 前端项目基础配置
**目标**: 引入依赖，搭建路由框架和基础布局

**具体操作**:
1. 安装依赖：
   - `vue-router` — 路由
   - `pinia` — 状态管理
   - `axios` — HTTP 请求
   - `element-plus` — UI 组件库
   - `@element-plus/icons-vue` — 图标
2. 创建 `src/router/index.js` — 路由配置（首页、登录页、各管理页占位）
3. 创建 `src/stores/` — Pinia store（用户状态、token管理）
4. 创建 `src/utils/request.js` — Axios 封装（拦截器、token注入、统一错误处理）
5. 创建 `src/layout/` — 后台管理布局（侧边栏 + 顶栏 + 内容区）
6. 创建 `src/views/` — 占位页面（Login、Dashboard、User、Goods、Order等）
7. 更新 `main.js` 引入 router / pinia / Element Plus
8. 清理默认的 `HelloWorld.vue` 和 `style.css` 中的示例样式

**✅ 验证**: `npm run dev` 启动后可导航到各占位页面

---

### Step 1.3 — 小程序端基础配置
**目标**: 引入 Vant Weapp，搭建页面框架

**具体操作**:
1. 初始化 `package.json`，安装 Vant Weapp
2. 构建 npm 包
3. `app.json` 按需注册 Vant 组件
4. 创建自定义 TabBar（首页、分类、发布、消息、我的）
5. 创建各 Tab 页面的占位文件
6. `app.json` 配置 `tabBar`
7. 封装 `utils/request.js` — 小程序 HTTP 请求（wx.request 封装）
8. 封装 `utils/storage.js` — 本地存储工具

**✅ 验证**: 微信开发者工具中可看到 TabBar 切换

---

## Phase 2: 用户与认证模块

### Step 2.1 — 后端：用户表设计 + 微信登录
**目标**: 完成数据库用户表，实现微信小程序登录

**具体操作**:
1. 设计并创建数据库 `qingxunhuan`
2. 创建 `user` 表（id, openid, nickname, avatar, phone, school, campus, status, create_time, update_time）
3. 生成 `User` 实体类 + `UserMapper` + `UserService`
4. 实现微信登录接口 `POST /api/user/wx-login`：
   - 接收 `code` → 调微信 API 换取 `openid` + `session_key`
   - 新用户自动注册，老用户返回 token
   - 使用 JWT 生成 token，存入 Redis
5. 创建 JWT 工具类 `utils/JwtUtil.java`
6. 创建微信工具类 `utils/WechatUtil.java`
7. 创建 `config/SecurityConfig.java` — Spring Security 配置（放行公开接口，其余需 token）
8. 创建 JWT 认证过滤器 `config/JwtAuthFilter.java`

**✅ 验证**: 用 Postman 模拟微信 code 调用登录接口，返回 token

---

### Step 2.2 — 小程序端：登录功能
**目标**: 小程序端完成微信一键登录

**具体操作**:
1. 页面 `pages/login/login` 完成登录页 UI
2. 调用 `wx.login()` 获取 code
3. 将 code 发送到后端登录接口
4. 将返回的 token 存入本地存储
5. 将用户信息存入全局状态（app.js globalData）
6. 登录后跳转到首页
7. `pages/mine/mine` 展示用户信息

**✅ 验证**: 小程序登录后能看到自己的头像昵称

---

### Step 2.3 — Web端：管理员登录
**目标**: Web管理端完成用户名密码登录

**具体操作**:
1. 后端新增 `POST /api/admin/login` 登录接口（账号密码方式）
2. 后端创建 `admin` 表
3. 后端初始化一个默认管理员账号
4. 前端 `views/Login.vue` — 登录页面 UI
5. 前端 store 保存 token 和用户信息
6. 前端路由守卫：未登录跳转到登录页

**✅ 验证**: Web端可用管理员账号登录，登录后进入 Dashboard

---

## Phase 3: 商品模块

### Step 3.1 — 后端：商品表 + 基础CRUD
**目标**: 完成商品数据库表和基础接口

**具体操作**:
1. 创建 `category` 表（id, name, icon, sort, parent_id）
2. 创建 `goods` 表（id, title, description, price, original_price, images, category_id, user_id, school, campus, condition, status, view_count, collect_count, create_time, update_time）
3. 初始化商品分类数据（数码、书籍、生活用品、服装、运动、其他）
4. 生成对应的 Entity + Mapper + Service
5. 实现接口：
   - `GET /api/category/list` — 获取分类列表
   - `POST /api/goods` — 发布商品
   - `GET /api/goods` — 商品列表（分页 + 分类筛选 + 搜索 + 学校筛选）
   - `GET /api/goods/{id}` — 商品详情
   - `PUT /api/goods/{id}` — 编辑商品
   - `DELETE /api/goods/{id}` — 下架/删除商品
   - `POST /api/goods/{id}/collect` — 收藏商品
6. 图片上传接口 `POST /api/upload/image` — 上传到本地/OSS

**✅ 验证**: Postman 测试商品 CRUD 全部正常

---

### Step 3.2 — 小程序端：商品浏览 + 搜索 + 发布
**目标**: 小程序端核心的商品浏览和发布功能

**具体操作**:
1. `pages/index/index` — 首页改版：
   - 搜索栏
   - 分类快捷入口（横向滚动）
   - 商品瀑布流/列表（下拉刷新、上拉加载）
2. `pages/category/category` — 分类页：
   - 左侧一级分类 + 右侧二级分类
   - 点击进入商品列表
3. `pages/goods/list` — 商品列表页：
   - 分类筛选、排序（最新/价格）
   - 搜索功能
4. `pages/goods/detail` — 商品详情页：
   - 图片轮播、价格、描述、卖家信息
   - 收藏按钮
   - 联系卖家/购买按钮
5. `pages/goods/publish` — 发布商品页：
   - 图片上传、标题、描述、价格、原价
   - 分类选择、成色选择
   - 提交发布

**✅ 验证**: 可完整走通"浏览→搜索→查看详情→收藏"流程

---

### Step 3.3 — Web端：商品管理
**目标**: 管理员可管理平台商品

**具体操作**:
1. `views/Goods/List.vue` — 商品列表（表格 + 搜索 + 筛选 + 分页）
2. `views/Goods/Detail.vue` — 商品详情
3. 可下架违规商品
4. 管理商品分类

**✅ 验证**: 管理员可查看和操作商品

---

## Phase 4: 交易/订单模块

### Step 4.1 — 后端：订单表 + 订单接口
**目标**: 完成订单流程后端

**具体操作**:
1. 创建 `order` 表（id, order_no, goods_id, buyer_id, seller_id, amount, status, remark, create_time, pay_time, complete_time）
2. 订单状态枚举：`PENDING` → `PAID` → `SHIPPED` → `COMPLETED` → `CANCELLED`
3. 生成 Entity + Mapper + Service
4. 实现接口：
   - `POST /api/order` — 创建订单
   - `GET /api/order` — 我的订单列表（分页）
   - `GET /api/order/{id}` — 订单详情
   - `PUT /api/order/{id}/cancel` — 取消订单
   - `PUT /api/order/{id}/complete` — 确认完成

**✅ 验证**: Postman 测试订单流程正常

---

### Step 4.2 — 小程序端：下单 + 订单管理
**目标**: 小程序端完成交易流程

**具体操作**:
1. 商品详情页加入"立即购买"按钮 → 跳转下单页
2. `pages/order/create` — 确认订单页（选择数量、填写备注）
3. `pages/order/list` — 订单列表（按状态 Tab 切换）
4. `pages/order/detail` — 订单详情

**✅ 验证**: 可完整走通"下单→查看订单→取消订单"流程

---

### Step 4.3 — Web端：订单管理
**目标**: 管理员可查看和管理订单

**具体操作**:
1. `views/Order/List.vue` — 订单列表（筛选用）
2. 可查看订单详情

**✅ 验证**: 管理员可查看所有订单

---

## Phase 5: 消息/聊天模块

### Step 5.1 — 后端：消息表 + WebSocket
**目标**: 实现在线聊天功能

**具体操作**:
1. 添加 `spring-boot-starter-websocket` 依赖
2. 创建 `message` 表（id, from_user_id, to_user_id, goods_id, content, is_read, create_time）
3. 配置 WebSocket + STOMP
4. 实现接口：
   - `POST /api/message` — 发送消息
   - `GET /api/message/list` — 会话列表
   - `GET /api/message/{userId}` — 与某用户的聊天记录
   - `PUT /api/message/read/{userId}` — 标记已读

**✅ 验证**: Postman + WebSocket 客户端测试消息收发

---

### Step 5.2 — 小程序端：聊天
**目标**: 小程序端实现在线聊天

**具体操作**:
1. `pages/chat/list` — 会话列表
2. `pages/chat/detail` — 聊天界面（WebSocket 实时通信）
3. 商品详情页"联系卖家"按钮跳转聊天页
4. TabBar 消息 Tab 显示未读角标

**✅ 验证**: 两个小程序用户可实时聊天

---

## Phase 6: 后台管理模块

### Step 6.1 — Web端：Dashboard + 用户管理
**目标**: 完善后台管理功能

**具体操作**:
1. `views/Dashboard.vue` — 仪表盘：
   - 统计卡片（用户数、商品数、订单数、成交额）
   - ECharts 图表（近7天新增数据趋势）
2. `views/User/List.vue` — 用户管理：
   - 用户列表、搜索、启用/禁用

**✅ 验证**: 仪表盘数据正确展示

---

### Step 6.2 — 后端 + Web端：数据统计
**目标**: 完善数据统计接口和展示

**具体操作**:
1. 后端统计接口：
   - `GET /api/admin/statistics/overview` — 核心统计数
   - `GET /api/admin/statistics/trend` — 趋势数据
2. Web端对接统计数据

**✅ 验证**: 后台仪表盘展示真实数据

---

## Phase 7: 集成联调与收尾

### Step 7.1 — 全端联调测试
**目标**: 确保三端协同工作正常

**具体操作**:
1. 完善小程序端各页面 UI 细节
2. 完善 Web 端各页面 UI 细节
3. 端到端测试所有核心流程：
   - 小程序：注册登录 → 浏览 → 发布 → 下单 → 聊天
   - Web端：登录 → 管理商品 → 管理订单 → 管理用户 → 查看统计
4. 修复联调中发现的问题

**✅ 验证**: 核心流程全部跑通

---

### Step 7.2 — 收尾工作
**目标**: 完善文档和项目配置

**具体操作**:
1. 更新各端 `README.md`
2. 创建数据库初始化 SQL 脚本
3. 创建项目启动说明文档
4. 各端代码规范检查、清理无用代码
5. 最终提交到 GitHub

**✅ 验证**: 仓库完整可运行

---

## 技术约定

| 约定项 | 说明 |
|---|---|
| **API 前缀** | 所有接口以 `/api/` 开头 |
| **统一返回格式** | `{ "code": 200, "message": "success", "data": ... }` |
| **小程序请求封装** | 统一处理 token 注入和错误提示 |
| **前端请求封装** | Axios 拦截器统一处理 token 和 401 跳转 |
| **Git 提交规范** | 每次完成一个 Step 后 commit + push，commit message 格式：`feat(模块): 完成xxx功能` |
| **分支策略** | 直接在 `main` 分支开发 |

---

## 计划总结

| 阶段 | 步骤数 | 核心交付 |
|---|---|---|
| Phase 1 基础设施 | 3 Steps | 三端框架就绪，可开始业务开发 |
| Phase 2 用户认证 | 3 Steps | 小程序微信登录 + Web管理员登录 |
| Phase 3 商品模块 | 3 Steps | 商品发布/浏览/搜索/收藏/管理 |
| Phase 4 订单模块 | 3 Steps | 下单/订单管理完整流程 |
| Phase 5 消息模块 | 2 Steps | 实时聊天 |
| Phase 6 后台管理 | 2 Steps | Dashboard + 用户管理 + 数据统计 |
| Phase 7 联调收尾 | 2 Steps | 全端联调通过，文档完整 |

**共计 18 个开发步骤**，每个步骤完成后提交并推送到 GitHub。
