<template>
  <div class="room-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">房间管理</span>
          <el-button type="primary" :loading="loading" @click="fetchList">
            <el-icon><Refresh /></el-icon>
            <span>刷新数据</span>
          </el-button>
        </div>
      </template>

      <!-- 加载中 -->
      <div v-if="loading" v-loading="true" class="placeholder">正在加载数据...</div>

      <!-- 数据表格 -->
      <template v-else-if="tableData.length">
        <el-table :data="tableData" border stripe height="460" style="width: 100%">
          <el-table-column
            type="index"
            label="序号"
            width="60"
            align="center"
          />
          <el-table-column
            v-for="col in columns"
            :key="col"
            :prop="col"
            :label="col"
            min-width="140"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <span>{{ formatCell(row[col]) }}</span>
            </template>
          </el-table-column>
        </el-table>
      </template>

      <!-- 空数据 -->
      <el-empty v-else-if="!errorMsg" description="暂无数据" />

      <!-- 错误提示 -->
      <el-alert
        v-if="errorMsg"
        :title="errorMsg"
        type="error"
        show-icon
        :closable="false"
        class="error-alert"
      />
    </el-card>

    <!-- 原始返回信息 -->
    <el-card shadow="never" class="raw-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">接口返回信息(GET /api/user/user/list)</span>
        </div>
      </template>
      <pre class="raw-json">{{ prettyJson }}</pre>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getUserList } from '@/api/user'

const loading = ref(false)
const errorMsg = ref('')
const rawData = ref(null)
const tableData = ref([])
const columns = ref([])

onMounted(fetchList)

async function fetchList() {
  loading.value = true
  errorMsg.value = ''
  try {
    // 携带 token 以 GET 方式调用用户列表接口
    const res = await getUserList()
    rawData.value = res
    // 兼容多种返回结构,提取表格数据
    const list = extractList(res?.data)
    tableData.value = list
    columns.value = list.length ? Object.keys(list[0]) : []
  } catch (e) {
    errorMsg.value = e?.message || '请求失败,请检查后端服务是否启动'
    tableData.value = []
    columns.value = []
  } finally {
    loading.value = false
  }
}

/**
 * 从返回数据中提取数组列表:
 * - 直接为数组 => 原样返回
 * - 对象中常见列表字段 list / rows / records / data => 提取
 */
function extractList(data) {
  if (Array.isArray(data)) return data
  if (data && typeof data === 'object') {
    for (const key of ['list', 'rows', 'records', 'items', 'data']) {
      if (Array.isArray(data[key])) return data[key]
    }
  }
  return []
}

function formatCell(value) {
  if (value === null || value === undefined) return '-'
  if (typeof value === 'object') return JSON.stringify(value)
  return value
}

const prettyJson = computed(() => {
  try {
    return JSON.stringify(rawData.value, null, 2) || '暂无返回数据'
  } catch (e) {
    return String(rawData.value ?? '暂无返回数据')
  }
})
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-title {
  font-weight: 600;
  font-size: 15px;
}

.placeholder {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.error-alert {
  margin-top: 12px;
}

.raw-card {
  margin-top: 20px;
}

.raw-json {
  background-color: #f5f7fa;
  border-radius: 6px;
  padding: 16px;
  font-size: 13px;
  line-height: 1.6;
  color: #303133;
  overflow: auto;
  max-height: 400px;
  font-family: 'Consolas', 'Monaco', monospace;
}
</style>
