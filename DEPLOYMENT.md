# 部署設定指南

Railway + GitHub Actions CI/CD 完整設定步驟。

---

## 目錄

1. [Railway 設定](#1-railway-設定)
2. [GitHub Actions 設定](#2-github-actions-設定)
3. [環境變數設定](#3-環境變數設定)
4. [驗證部署](#4-驗證部署)

---

## 1. Railway 設定

### 1.1 建立帳號

前往 [railway.app](https://railway.app) 註冊，建議用 GitHub 帳號登入，之後連結 repo 更方便。

### 1.2 建立 Project

1. Dashboard → **New Project**
2. 選擇 **Deploy from GitHub repo**
3. 授權 Railway 存取你的 GitHub，選擇 `spring-mall` repo
4. Railway 會自動偵測這是 Maven 專案並建立 Service

### 1.3 建立 MySQL 資料庫

1. 在 Project 頁面點 **+ New** → **Database** → **MySQL**
2. Railway 會自動建立 MySQL 並產生連線資訊
3. 點進 MySQL service → **Variables** 頁籤，記下以下變數（之後設定 Spring Boot 用）：
   - `MYSQL_URL`
   - `MYSQL_USER`
   - `MYSQL_PASSWORD`

### 1.4 設定 Backend Service 環境變數

1. 點進 Spring Boot service → **Variables** 頁籤
2. 新增以下變數：

   | 變數名稱 | 值 |
   |---------|---|
   | `SPRING_DATASOURCE_URL` | `jdbc:mysql://<MYSQL_URL>/spring_mall` |
   | `SPRING_DATASOURCE_USERNAME` | MySQL 的 `MYSQL_USER` 值 |
   | `SPRING_DATASOURCE_PASSWORD` | MySQL 的 `MYSQL_PASSWORD` 值 |
   | `SERVER_PORT` | `8080` |

### 1.5 產生 Railway Token

1. 右上角頭像 → **Account Settings**
2. 左側選單 **Tokens** → **New Token**
3. 填入名稱（例如 `github-actions`），點 **Create**
4. 複製 token，**只會顯示一次**，請立即儲存

---

## 2. GitHub Actions 設定

### 2.1 新增 Repository Secret

1. 前往 GitHub repo → **Settings**
2. 左側選單 **Secrets and variables** → **Actions**
3. 點 **New repository secret**，新增以下 secret：

   | Secret 名稱 | 值 |
   |------------|---|
   | `RAILWAY_TOKEN` | 步驟 1.5 複製的 token |

### 2.2 確認 Workflow 檔案存在

專案根目錄應有以下檔案：

```
.github/
└── workflows/
    └── ci.yml
```

內容已設定為 push 到 `dev` 分支時自動觸發，無需額外修改。

### 2.3 觸發第一次 CI/CD

```
git add .
git commit -m "ci: add github actions workflow"
git push origin dev
```

推送後前往 GitHub repo → **Actions** 頁籤，可以看到 workflow 執行狀態。

---

## 3. 環境變數設定

### 本地開發

本地使用 `src/main/resources/application.properties`，不會影響 Railway。

### Railway Production

所有敏感資訊（DB 密碼等）只放在 Railway Variables 頁籤，不提交到 git。

確認 `.gitignore` 包含：

```
application-prod.properties
.env
```

---

## 4. 驗證部署

### 確認 CI 通過

GitHub → Actions 頁籤，三個 job 都顯示綠色勾勾：

```
✅ Frontend Check
✅ Backend Check
✅ Deploy
```

### 確認 Railway 部署成功

1. Railway Dashboard → 點進 Spring Boot service
2. **Deployments** 頁籤確認最新一筆狀態為 `SUCCESS`
3. **Settings** → **Networking** 取得公開 domain（格式為 `xxx.railway.app`）
4. 瀏覽器開啟 `https://xxx.railway.app/products` 確認 API 回傳資料

### 常見問題

**CI 格式檢查失敗**

在本地先跑格式化再推送：

```bash
# 前端
cd frontend && npm run format

# 後端
mvn spotless:apply
```

**Railway 部署失敗**

Railway Dashboard → service → **Deployments** → 點進失敗的 deployment 查看 log。
