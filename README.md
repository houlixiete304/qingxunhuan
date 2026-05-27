# 青芽 - 校园二手交易平台

校园二手闲置物品交易平台，包含小程序端、Web管理端和Spring Boot后端。

## 项目结构

```
qingxunhuan/
├── backend/    # Spring Boot 后端
├── frontend/   # Vue 3 Web管理端
└── miniapp/    # 微信小程序端
```

## 环境要求

- JDK 17+
- MySQL 8.0
- Redis
- Node.js 18+
- 微信开发者工具

## 快速启动

### 1. 数据库

执行 `backend/src/main/resources/sql/schema.sql` 创建数据库和表。

### 2. 后端

```bash
cd backend
./mvnw spring-boot:run
```
默认管理员: `admin` / `123456`

### 3. Web管理端

```bash
cd frontend
npm install
npm run dev
```

### 4. 小程序端

用微信开发者工具打开 `miniapp/` 目录，构建npm后运行。

## 技术栈

| 端 | 技术 |
|---|---|
| 后端 | Spring Boot 3.4 + MyBatis-Plus + MySQL + Redis + JWT |
| Web | Vue 3 + Element Plus + Axios + Pinia |
| 小程序 | 原生微信小程序 + Vant Weapp |
