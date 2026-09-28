# 酒店入住后台管理系统

基于 Vue 3 + Vite + Element Plus + Pinia + Vue Router + Axios 搭建的前端工程。

## 功能模块

1. **登录页** — 调用登录接口并存储 token
2. **控制台** — 数据概览 + 左侧导航栏(暂无接口,静态展示)
3. **房间管理** — 携带 token 调用用户列表接口并展示返回数据

## 技术栈

| 依赖 | 说明 |
| --- | --- |
| Vue 3 | 组合式 API(`<script setup>`) |
| Vite 5 | 构建工具与开发服务器 |
| Element Plus | UI 组件库 |
| Pinia | 状态管理(token 存储) |
| Vue Router 4 | 路由与登录守卫 |
| Axios | HTTP 请求与拦截器 |

## 目录结构

```
├── index.html
├── vite.config.js          # 代理配置(/api -> http://localhost:16379)
├── src/
│   ├── main.js             # 入口,注册 Element Plus 等
│   ├── App.vue
│   ├── api/
│   │   ├── request.js      # axios 实例 + 拦截器(请求头携带 token)
│   │   └── user.js         # 登录 / 用户列表接口
│   ├── store/
│   │   └── auth.js         # token 存储(localStorage 持久化)
│   ├── router/
│   │   └── index.js        # 路由 + 登录守卫
│   ├── layout/
│   │   └── index.vue       # 后台布局(左侧导航 + 顶部栏)
│   └── views/
│       ├── login/          # 登录页
│       ├── dashboard/      # 控制台
│       └── room/           # 房间管理
```

## 接口约定

### 登录

```
POST http://localhost:16379/api/user/login
Content-Type: application/json
{ "username": "admin", "password": "123456" }

响应:
{ "code": 200, "msg": "登录成功", "data": { "token": "..." } }
```

### 用户列表(房间管理页调用)

```
GET http://localhost:16379/api/user/user/list
请求头携带 token(见下方说明)
```

## 启动方式

```bash
npm install
npm run dev      # 启动开发服务器,默认 http://localhost:5173
npm run build    # 构建生产产物(输出到 build/ 目录)
```

## 关于 token 携带方式

请求拦截器 `src/api/request.js` 中会同时设置两个请求头,以最大化兼容后端:

```js
config.headers['token'] = authStore.token                  // 自定义 token 头
config.headers['Authorization'] = `Bearer ${authStore.token}` // 标准 Bearer 头
```

如果后端只认其中某一种,保留对应那一行即可(多余的请求头通常会被后端忽略)。

## 关于开发代理

开发环境通过 Vite 代理将 `/api` 转发到 `http://localhost:16379`,避免浏览器跨域。

如果后端已开启 CORS 且希望直连,可将 `src/api/request.js` 中的 `baseURL` 改为:

```js
baseURL: 'http://localhost:16379/api'
```

并删除 `vite.config.js` 中的 proxy 配置。
