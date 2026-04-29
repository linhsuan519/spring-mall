# Vesto · 精選商城

Vue 3 + Spring Boot 前後端分離電商專案。

## 專案結構

```
spring-mall/
├── src/                  # Spring Boot 後端
├── frontend/             # Vue 3 前端
└── pom.xml
```

## 啟動方式

### 後端

```
mvn spring-boot:run
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

### 概覽

```
push to dev
  ├── Frontend Check  →  npm ci → prettier --check → build
  ├── Backend Check   →  mvn spotless:check
  └── Deploy          →  兩個都通過後，自動部署到 Railway
```

### GitHub Actions

Workflow 檔案位於 `.github/workflows/ci.yml`，觸發條件為 push 到 `dev` 分支。

| Job | 執行內容 |
|-----|---------|
| Frontend Check | 安裝相依套件、Prettier 格式檢查、Vite build |
| Backend Check | Spotless 格式檢查 |
| Deploy | 前兩個 job 都通過後部署到 Railway |

### 環境設定

在 GitHub repo Settings → Secrets → Actions 新增以下 secret：

| Secret | 說明 |
|--------|------|
| `RAILWAY_TOKEN` | Railway dashboard → Account Settings → Tokens 產生 |

### 本地推送前建議

推送前先在本地跑格式化，避免 CI 失敗：

```bash
# 前端
cd frontend && npm run format

# 後端
mvn spotless:apply
```
