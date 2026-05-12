# Vesto · 精選商城

Vue 3 + Spring Boot 前後端分離電商專案。

## 專案結構

```
spring-mall/
├── src/                  # Spring Boot 後端
├── frontend/             # Vue 3 前端
├── Dockerfile            # 後端 Docker 建置
├── frontend/Dockerfile   # 前端 Docker 建置
└── pom.xml
```

## 啟動方式

### 後端

```
mvn spring-boot:run -Dspring-boot.run.profiles=local
# 運行於 http://localhost:8080
```

### 前端

```
cd frontend
npm install
npm run dev
# 運行於 http://localhost:3000
```

## 程式碼格式化

### 前端（Prettier）

```
cd frontend
npm run format
```

### 後端（Spotless · Google Java Format）

```
mvn spotless:apply
```

## CI/CD 流程

### push 到 `dev` 之後會發生什麼

```
git push origin dev
    │
    ├─▶ [GitHub Actions] 同時觸發兩個檢查 Job
    │       ├── Frontend Check：npm ci → prettier --check → vite build
    │       └── Backend Check：mvn spotless:check
    │
    └─▶ 兩個 Job 都通過 → [GitHub Actions] 同時執行兩個部署 Job
            ├── Deploy Backend：docker build → push → gcloud run deploy
            └── Deploy Frontend：docker build → push → gcloud run deploy
```

> Frontend 或 Backend Check 失敗 → 部署 Job 不執行，前後端都不會更新。

### 部署環境

| 服務 | 平台 | 網址 |
|------|------|------|
| 後端 (Spring Boot) | GCP Cloud Run | `spring-mall-backend-1084089646528.asia-east1.run.app` |
| 前端 (Vue 3 + Nginx) | GCP Cloud Run | `spring-mall-frontend-ag3iighx4a-de.a.run.app` |
| 資料庫 (MySQL) | Railway | 常駐，不受部署影響 |
| 快取 / Session (Redis) | Railway | 常駐，不受部署影響 |

### GitHub Actions Jobs

Workflow 檔案位於 `.github/workflows/ci.yml`。

| Job | 執行內容 | 依賴 |
|-----|---------|------|
| Frontend Check | npm ci → Prettier 格式檢查 → Vite build | 無 |
| Backend Check | Spotless 格式檢查（Google Java Format）| 無 |
| Deploy Backend | Docker build → Artifact Registry push → Cloud Run deploy | Frontend Check + Backend Check |
| Deploy Frontend | Docker build（含 VITE_API_BASE_URL）→ Artifact Registry push → Cloud Run deploy | Frontend Check + Backend Check |

### 必要的 GitHub Secrets

GCP 驗證使用 **Workload Identity Federation**，不需要金鑰檔案，ci.yml 裡直接設定 provider 和 service account 即可。

| Secret | 說明 |
|--------|------|
| `DATASOURCE_URL` | Railway MySQL 連線 URL |
| `DATASOURCE_USERNAME` | Railway MySQL 帳號 |
| `DATASOURCE_PASSWORD` | Railway MySQL 密碼 |
| `REDIS_HOST` | Railway Redis host |
| `REDIS_PORT` | Railway Redis port |
| `REDIS_PASSWORD` | Railway Redis 密碼 |
| `BACKEND_URL` | 後端 Cloud Run 網址（給前端 build 時注入） |
