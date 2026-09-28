<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-header">
        <el-icon :size="34" color="#409eff"><OfficeBuilding /></el-icon>
        <h1 class="login-title">酒店入住后台管理系统</h1>
        <p class="login-subtitle">Hotel Management System</p>
      </div>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        size="large"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
            clearable
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { login } from '@/api/user'
import { useAuthStore } from '@/store/auth'

const router = useRouter()
const authStore = useAuthStore()

const formRef = ref()
const loading = ref(false)
const form = reactive({
  username: 'admin',
  password: '123456'
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  if (loading.value) return
  await formRef.value.validate()

  loading.value = true
  try {
    // 调用登录接口 POST /api/user/login
    const res = await login({
      username: form.username,
      password: form.password
    })
    // 响应结构: { code, msg, data: { token } }
    const token = res?.data?.token
    if (!token) {
      ElMessage.error('登录失败:未获取到 token')
      return
    }
    authStore.setToken(token)
    authStore.setUsername(form.username)
    ElMessage.success('登录成功')
    router.replace('/dashboard')
  } catch (e) {
    // 错误提示已由响应拦截器统一处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1f3b73 0%, #2f6bd8 50%, #409eff 100%);
}

.login-card {
  width: 400px;
  padding: 40px 36px 32px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.2);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-title {
  font-size: 20px;
  margin-top: 12px;
  color: #303133;
}

.login-subtitle {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
  letter-spacing: 1px;
}

.login-btn {
  width: 100%;
  letter-spacing: 4px;
}
</style>
