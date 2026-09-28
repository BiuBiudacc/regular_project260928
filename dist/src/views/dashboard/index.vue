<template>
  <div class="dashboard">
    <!-- 欢迎卡片 -->
    <el-card shadow="never" class="welcome-card">
      <div class="welcome">
        <div>
          <h2>你好,{{ authStore.username || '管理员' }} 👋</h2>
          <p>欢迎使用酒店入住后台管理系统</p>
        </div>
        <el-tag type="success" effect="dark" size="large">系统运行正常</el-tag>
      </div>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :xs="12" :sm="12" :md="6" v-for="item in stats" :key="item.title">
        <el-card shadow="hover" class="stat-card">
          <div class="stat">
            <div class="stat-icon" :style="{ backgroundColor: item.bg }">
              <el-icon :size="26" color="#fff">
                <component :is="item.icon" />
              </el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-title">{{ item.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 说明卡片 -->
    <el-card shadow="never" class="tip-card">
      <template #header>
        <span class="tip-header">系统功能模块</span>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="控制台">数据概览与统计</el-descriptions-item>
        <el-descriptions-item label="房间管理">房间列表与入住信息查询</el-descriptions-item>
        <el-descriptions-item label="客户管理">客户信息管理</el-descriptions-item>
        <el-descriptions-item label="设施管理">酒店设施维护</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/store/auth'

const authStore = useAuthStore()

const stats = [
  { title: '房间总数', value: 128, icon: 'House', bg: '#409eff' },
  { title: '入住中', value: 86, icon: 'UserFilled', bg: '#67c23a' },
  { title: '空房', value: 42, icon: 'OfficeBuilding', bg: '#e6a23c' },
  { title: '本月订单', value: 305, icon: 'Tickets', bg: '#f56c6c' }
]
</script>

<style scoped>
.welcome-card {
  margin-bottom: 20px;
}

.welcome {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.welcome h2 {
  font-size: 22px;
  margin-bottom: 6px;
}

.welcome p {
  color: #909399;
  font-size: 14px;
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  margin-bottom: 0;
}

.stat {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-value {
  font-size: 26px;
  font-weight: 700;
  color: #303133;
}

.stat-title {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}

.tip-header {
  font-weight: 600;
}
</style>
