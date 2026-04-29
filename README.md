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

### push 到 `dev` 之後會發生什麼

```
git push origin dev
    │
    ├─▶ [GitHub Actions] 同時觸發兩個檢查 Job
    │       ├── Frontend Check：npm ci → prettier --check → vite build
    │       └── Backend Check：mvn spotless:check
    │
    ├─▶ 兩個 Job 都通過 → [GitHub Actions] Deploy Job 執行
    │       └── railway up --detach（部署後端到 Railway）
    │
    └─▶ [Vercel] 偵測到 dev branch 有新 commit，自動部署前端
            └── 部署完成後更新 https://spring-mall.vercel.app
```

> Frontend 或 Backend Check 失敗 → Deploy Job 不執行，後端不會更新。
> Vercel 獨立監聽 git push，無論 GitHub Actions 結果如何，push 到 `dev` 就會自動部署前端。

### 部署環境

| 服務 | 平台 | 觸發條件 |
|------|------|---------|
| 後端 (Spring Boot) | Railway | GitHub Actions Deploy Job 通過後 |
| 前端 (Vue 3) | Vercel | push 到 `dev` branch 即自動部署 |
| 資料庫 (MySQL) | Railway | 常駐，不受部署影響 |

### GitHub Actions Jobs

Workflow 檔案位於 `.github/workflows/ci.yml`。

| Job | 執行內容 | 依賴 |
|-----|---------|------|
| Frontend Check | npm ci → Prettier 格式檢查 → Vite build | 無 |
| Backend Check | Spotless 格式檢查（Google Java Format）| 無 |
| Deploy | `railway up --detach` 部署後端 | 前兩個都通過 |