# 青循环 - 校园二手交易平台

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

### 1. 安装 MySQL 8.0

如果已安装可跳过。Windows 用户推荐直接下载 MySQL 安装包：

https://dev.mysql.com/downloads/mysql/8.0.html

安装时记住设置的 **root 密码**，后面需要用到。

### 2. 确认 MySQL 已启动

Windows 下按 `Win + R`，输入 `services.msc`，找到 `MySQL80` 服务确保状态为"正在运行"。

### 3. 导入建表 SQL

SQL 文件位置：`backend/src/main/resources/sql/schema.sql`

这个文件会自动创建 `qingxunhuan` 数据库和全部 7 张表（user、admin、category、goods、collect、order、message），同时初始化商品分类数据和默认管理员账号。

以下三种方式**任选一种**：

---

**方式一：Navicat / DataGrip 等 GUI 工具（推荐新手）**

1. 打开 Navicat，点击 **连接 → MySQL**
2. 连接名随便填，主机 `localhost`，端口 `3306`，用户名 `root`，密码填你的
3. 双击连接，右键 **新建数据库**，数据库名 `qingxunhuan`，字符集选 `utf8mb4`
4. 双击 `qingxunhuan` 打开，右键 **运行 SQL 文件**
5. 选择 `backend/src/main/resources/sql/schema.sql`，点开始

---

**方式二：MySQL 命令行**

```bash
# 先登录 MySQL（输入你的 root 密码）
mysql -u root -p

# 然后在 MySQL 命令行里执行：
source backend/src/main/resources/sql/schema.sql

# 查看是否成功
SHOW TABLES;
# 应该看到 7 张表
```

> 注意：`source` 后面要写 `schema.sql` 的**绝对路径**，比如 Windows 上可能长这样：
> `source D:/Project/JavaProject/qingxunhuan/backend/src/main/resources/sql/schema.sql;`

---

**方式三：Python 脚本**

```bash
pip install pymysql
```

```bash
python -c "
import pymysql
conn = pymysql.connect(host='localhost', user='root', password='你的密码')
cur = conn.cursor()
with open('backend/src/main/resources/sql/schema.sql', 'r', encoding='utf-8') as f:
    for stmt in f.read().split(';'):
        stmt = stmt.strip()
        if stmt and not stmt.startswith('--'):
            try:
                cur.execute(stmt)
                print('.', end='')
            except Exception as e:
                print(f'Skip: {e}')
conn.commit()
cur.execute('SHOW TABLES')
print([t[0] for t in cur.fetchall()])
conn.close()
"
```

成功后应看到：`['admin', 'category', 'collect', 'goods', 'message', 'order', 'user']`

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
