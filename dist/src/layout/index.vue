<template>
  <el-container class="layout">
    <!-- 左侧导航栏 -->
    <el-aside width="220px" class="layout-aside">
      <div class="logo">
        <el-icon :size="24" color="#409eff"><OfficeBuilding /></el-icon>
        <span class="logo-text">酒店管理系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#001529"
        text-color="#c0c4cc"
        active-text-color="#ffffff"
        class="layout-menu"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>控制台</span>
        </el-menu-item>
        <el-menu-item index="/room">
          <el-icon><House /></el-icon>
          <span>房间管理</span>
        </el-menu-item>
        <el-sub-menu index="resident">
          <template #title>
            <el-icon><User /></el-icon>
            <span>入住人员管理</span>
          </template>
          <el-menu-item index="/batch">
            <el-icon><Tickets /></el-icon>
            <span>批次管理</span>
          </el-menu-item>
          <el-menu-item index="/class">
            <el-icon><School /></el-icon>
            <span>班级管理</span>
          </el-menu-item>
          <el-menu-item index="/classRoom">
            <el-icon><OfficeBuilding /></el-icon>
            <span>教室管理</span>
          </el-menu-item>
          <el-menu-item index="/student">
            <el-icon><UserFilled /></el-icon>
            <span>学生管理</span>
          </el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="code-table">
          <template #title>
            <el-icon><Collection /></el-icon>
            <span>基础码表</span>
          </template>
          <el-menu-item index="/classRoomType">
            <el-icon><Grid /></el-icon>
            <span>教室型号码表</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 顶部栏 -->
      <el-header class="layout-header">
        <div class="header-title">{{ currentTitle }}</div>
        <div class="header-user">
          <el-avatar :size="30" class="header-avatar">
            {{ usernameInitial }}
          </el-avatar>
          <span class="username">{{ authStore.username || '管理员' }}</span>
          <el-dropdown @command="handleCommand">
            <el-icon class="header-more"><ArrowDown /></el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区 -->
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useAuthStore } from '@/store/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || '控制台')
const usernameInitial = computed(() =>
  (authStore.username || '管').charAt(0).toUpperCase()
)

function handleCommand(command) {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(() => {
        authStore.clear()
        ElMessage.success('已退出登录')
        router.replace('/login')
      })
      .catch(() => {})
  }
}
</script>

<style scoped>
.layout {
  height: 100%;
}

.layout-aside {
  background-color: #001529;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #ffffff;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo-text {
  font-size: 17px;
  font-weight: 600;
  letter-spacing: 1px;
}

.layout-menu {
  border-right: none;
  flex: 1;
}

.layout-header {
  height: 60px;
  background-color: #ffffff;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
}

.header-user {
  display: flex;
  align-items: center;
  gap: 10px;
}

.header-avatar {
  background-color: #409eff;
  color: #fff;
  font-weight: 600;
}

.username {
  font-size: 14px;
  color: #303133;
}

.header-more {
  cursor: pointer;
  color: #606266;
}

.layout-main {
  padding: 20px;
  overflow: auto;
}
</style>
