import { defineStore } from 'pinia'

const TOKEN_KEY = 'hotel_admin_token'
const USERNAME_KEY = 'hotel_admin_username'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    // 从 localStorage 恢复 token,保证刷新页面后仍然保持登录态
    token: localStorage.getItem(TOKEN_KEY) || '',
    username: localStorage.getItem(USERNAME_KEY) || ''
  }),
  getters: {
    isLoggedIn: (state) => !!state.token
  },
  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem(TOKEN_KEY, token)
    },
    setUsername(username) {
      this.username = username
      localStorage.setItem(USERNAME_KEY, username)
    },
    clear() {
      this.token = ''
      this.username = ''
      localStorage.removeItem(TOKEN_KEY)
      localStorage.removeItem(USERNAME_KEY)
    }
  }
})
