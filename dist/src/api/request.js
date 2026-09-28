import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/store/auth'
import router from '@/router'

// 创建 axios 实例
// baseURL 为 '/api',开发环境通过 vite 代理转发到 http://localhost:16379
// 如需直接访问后端,可将 baseURL 改为 'http://localhost:16379/api'
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器:在请求头中携带 token
request.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore()
    if (authStore.token) {
      // 后端约定在请求头中以 token 字段携带
      config.headers['token'] = authStore.token
      // 兼容同时支持标准 Authorization 头的后端
      config.headers['Authorization'] = `Bearer ${authStore.token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器:统一处理业务状态码
request.interceptors.response.use(
  (response) => {
    const res = response.data
    // 若响应体包含 code 字段,按业务码处理
    if (res && typeof res === 'object' && 'code' in res) {
      if (res.code === 200) {
        return res
      }
      // 登录态失效,清除 token 并跳转登录页
      if (res.code === 401) {
        handleUnauthorized()
      }
      ElMessage.error(res.msg || '请求失败')
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
    return res
  },
  (error) => {
    const status = error.response?.status
    if (status === 401) {
      handleUnauthorized()
    }
    const message = error.response?.data?.msg || error.message || '网络异常'
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

function handleUnauthorized() {
  const authStore = useAuthStore()
  authStore.clear()
  ElMessage.error('登录已失效,请重新登录')
  router.replace('/login')
}

export default request
