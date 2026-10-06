复制 `.env.example` 为 `.env`，填写 MySQL 密码和随机 JWT 密钥，密钥至少 32 字节。

已有数据库执行一次 `src/main/resources/db/migrate.sql`，新建表会在应用启动时自动创建。

本地运行：

```powershell
mvn spring-boot:run
```

Docker 运行：

```powershell
docker compose up -d --build
```

页面地址：http://localhost:8080
