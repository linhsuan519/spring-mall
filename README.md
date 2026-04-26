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
