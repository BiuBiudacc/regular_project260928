import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  build: {
    outDir: 'build'
  },
  server: {
    port: 5173,
    // 开发环境代理:将 /api 开头的请求转发到后端,避免跨域问题
    proxy: {
      '/api': {
        target: 'http://localhost:16379',
        changeOrigin: true
        // 不重写路径,保留 /api 前缀 => http://localhost:16379/api/xxx
      }
    }
  }
})
