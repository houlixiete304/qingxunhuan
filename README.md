# 青芽 - 校园二手交易平台

校园二手闲置物品交易平台，包含**小程序端**、**Web管理端**和**Spring Boot后端**。

## 项目结构

```
qingxunhuan/
├── backend/                 # Spring Boot 后端
│   └── src/main/resources/
│       ├── application.yaml # 后端配置
│       └── sql/schema.sql   # 数据库建表脚本
├── frontend/                # Vue 3 Web管理端
└── miniapp/                 # 微信小程序端
```

## 环境要求

| 依赖 | 版本 | 说明 |
|------|------|------|
| JDK | 17+ | 后端运行环境 |
| MySQL | 8.0+ | 数据库 |
| Redis | 6.0+ | 缓存（token存储） |
| Node.js | 18+ | 前端构建 |
| Maven | 3.9+ | 后端构建（项目自带mvnw，无需单独安装） |
| 微信开发者工具 | 最新版 | 小程序调试 |

---

## 一、克隆项目

```bash
git clone https://github.com/houlixiete304/qingxunhuan.git
cd qingxunhuan
```

---

## 二、数据库配置

### 1. 确保 MySQL 8.0 已启动

### 2. 创建数据库并导入表结构

用 MySQL 客户端（Navicat/命令行/python 均可）执行：

```
backend/src/main/resources/sql/schema.sql
```

或者命令行方式：

```bash
# 方式一：用 mysql 命令行
mysql -u root -p < backend/src/main/resources/sql/schema.sql

# 方式二：用 Python
pip install pymysql
python -c "
import pymysql
conn = pymysql.connect(host='localhost', user='root', password='你的密码')
with open('backend/src/main/resources/sql/schema.sql', 'r', encoding='utf-8') as f:
    for stmt in f.read().split(';'):
        if stmt.strip() and not stmt.strip().startswith('--'):
            try: conn.cursor().execute(stmt)
            except: pass
    conn.commit()
    conn.close()
print('Done')
"
```

### 3. 修改数据库连接信息

编辑 `backend/src/main/resources/application.yaml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/qingxunhuan?...
    username: root          # 改成你的MySQL用户名
    password: 123456        # 改成你的MySQL密码
```

---

## 三、Redis 配置

确保 Redis 已启动（默认连接 `localhost:6379`，无密码）。

Windows 下可以用 `redis-server.exe` 启动，或使用 `redis-server --service-start` 启动服务。

如果 Redis 有密码，修改 `application.yaml`：

```yaml
spring:
  data:
    redis:
      password: 你的密码
```

---

## 四、启动后端

```bash
cd backend

# 首次运行会下载依赖，需要几分钟
./mvnw spring-boot:run
```

看到 `Started BackendApplication` 即启动成功，默认端口 **8080**。

验证：浏览器访问 `http://localhost:8080/api/health`，返回 `{"code":200,"data":"ok"}`。

### 默认管理员账号

| 用户名 | 密码 |
|--------|------|
| admin | 123456 |

---

## 五、启动 Web 管理端

```bash
cd frontend

# 安装依赖（仅首次）
npm install

# 启动开发服务器
npm run dev
```

访问 `http://localhost:5173`，用管理员账号登录。

> 开发模式下，前端 `/api` 请求会自动代理到后端 `localhost:8080`，无需额外配置。

---

## 六、启动小程序端

### 1. 打开项目

用**微信开发者工具**打开 `miniapp/` 目录。

### 2. 构建 npm

菜单栏 **工具 → 构建 npm**，等待构建完成。

### 3. 关闭域名校验（开发阶段）

菜单栏 **详情 → 本地设置**，勾选：

> ☑ 不校验合法域名、web-view（业务域名）、TLS 版本以及 HTTPS 证书

否则 `localhost:8080` 请求会被拦截。

### 4. 编译运行

点击 **编译** 按钮即可预览。

> 小程序通过 `utils/request.js` 连接后端，默认地址为 `http://localhost:8080/api`。如需修改，编辑 `miniapp/utils/request.js` 中的 `BASE_URL`。

---

## 七、小程序 AppID

项目当前 AppID：`YOUR_APPID`（在 `project.config.json` 中）

如果你有自己的小程序 AppID，改为你自己的即可。

---

## 常见问题

### 后端启动报错 "Table 'qingxunhuan.xxx' doesn't exist"
没导入建表 SQL，参考第二章第2步执行 `schema.sql`。

### 后端启动报错 "Access denied for user"
MySQL 用户名或密码不对，检查 `application.yaml`。

### 后端启动报错 "Unable to connect to Redis"
Redis 没启动，或密码不对。

### 小程序编译报错 "未找到组件"
没有构建 npm，点 **工具 → 构建 npm**。

### 小程序请求失败 "不在合法域名列表"
没有关闭域名校验，参考第六章第3步。

### 前端登录报错 "网络错误"
后端没启动，或 8080 端口被占用。

### 端口被占用
```bash
# Windows 查看端口占用
netstat -ano | findstr 8080
# 改端口：编辑 application.yaml 中的 server.port
```

---

## 技术栈

| 端 | 技术 |
|---|---|
| 后端 | Spring Boot 3.4 + MyBatis-Plus 3.5 + MySQL + Redis + Spring Security + JWT |
| Web管理端 | Vue 3 + Element Plus + Axios + Pinia + Vite |
| 小程序 | 原生微信小程序 + Vant Weapp |
