<template>
  <div class="student-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">学生管理</span>
          <el-button :loading="loading" @click="handleRefresh">
            <el-icon><Refresh /></el-icon>
            <span>刷新数据</span>
          </el-button>
        </div>
      </template>

      <!-- 条件检索表单 -->
      <el-form :inline="true" :model="query" class="search-form" @submit.prevent>
        <el-form-item label="学生姓名">
          <el-input
            v-model="query.studentName"
            placeholder="请输入学生姓名"
            clearable
            style="width: 180px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="所属批次">
          <el-select
            v-model="query.batchId"
            placeholder="请选择批次"
            clearable
            filterable
            style="width: 200px"
          >
            <el-option
              v-for="b in batchOptions"
              :key="b.batchId"
              :label="b.batchName"
              :value="b.batchId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="入住状态">
          <el-select
            v-model="query.checkInStatus"
            placeholder="请选择入住状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="s in checkInStatusOptions"
              :key="s"
              :label="s"
              :value="s"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSearch">
            <el-icon><Search /></el-icon>
            <span>查询</span>
          </el-button>
          <el-button @click="handleReset">
            <el-icon><RefreshLeft /></el-icon>
            <span>重置</span>
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column
          type="index"
          label="序号"
          width="80"
          align="center"
          :index="indexMethod"
        />
        <el-table-column
          prop="studentName"
          label="学生姓名"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="batchName"
          label="批次名称"
          min-width="150"
          show-overflow-tooltip
        />
        <el-table-column
          prop="className"
          label="班级名称"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="studentPhone"
          label="联系方式"
          width="140"
          align="center"
        />
        <el-table-column
          prop="roomName"
          label="宿舍名称"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="bedName"
          label="床位名称"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="checkInStatus"
          label="入住状态"
          width="110"
          align="center"
        >
          <template #default="{ row }">
            <el-tag v-if="row.checkInStatus" :type="checkInStatusTagType(row.checkInStatus)">
              {{ row.checkInStatus }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
      </el-table>

      <!-- 空数据 -->
      <el-empty
        v-if="!loading && !errorMsg && !tableData.length"
        description="暂无学生数据"
      />

      <!-- 错误提示 -->
      <el-alert
        v-if="errorMsg"
        :title="errorMsg"
        type="error"
        show-icon
        :closable="false"
        class="error-alert"
      />

      <!-- 分页 -->
      <div v-if="total > 0" class="pagination">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :current-page="pageNum"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20, 50]"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { studentPage } from '@/api/student'
import { getBatchSimpleList } from '@/api/batch'

const loading = ref(false)
const errorMsg = ref('')
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

// 条件检索条件
const query = reactive({
  studentName: '',
  batchId: '',
  checkInStatus: ''
})

// 批次下拉选项
const batchOptions = ref([])

// 入住状态枚举(字符串)
const checkInStatusOptions = ['入住中', '未入住']
const checkInStatusTagMap = {
  入住中: 'success',
  未入住: 'warning'
}

/** 入住状态 -> 标签颜色 */
function checkInStatusTagType(value) {
  return checkInStatusTagMap[value] || 'info'
}

onMounted(() => {
  fetchList()
  loadBatchOptions()
})

async function fetchList() {
  loading.value = true
  errorMsg.value = ''
  try {
    // 同一接口支持通用查询(空条件)与条件检索(带条件)
    const res = await studentPage(buildQuery())
    const data = res?.data || {}
    tableData.value = Array.isArray(data.records) ? data.records : []
    total.value = Number(data.total) || 0
  } catch (e) {
    errorMsg.value = e?.message || '请求失败,请检查后端服务是否启动'
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

/** 根据当前查询表单构造请求参数,仅携带非空条件 */
function buildQuery() {
  const payload = {
    pageNum: pageNum.value,
    pageSize: pageSize.value
  }
  const name = query.studentName?.trim()
  if (name) payload.studentName = name
  const batchId = query.batchId
  if (batchId !== '' && batchId !== null && batchId !== undefined) {
    const num = Number(batchId)
    if (Number.isFinite(num)) payload.batchId = num
  }
  const status = query.checkInStatus
  if (status) payload.checkInStatus = status
  return payload
}

/** 加载批次简易下拉选项(检索表单) */
async function loadBatchOptions() {
  try {
    const res = await getBatchSimpleList()
    batchOptions.value = Array.isArray(res?.data) ? res.data : []
  } catch (e) {
    batchOptions.value = []
    // 错误提示已由响应拦截器统一处理
  }
}

function handleSearch() {
  pageNum.value = 1
  fetchList()
}

function handleReset() {
  query.studentName = ''
  query.batchId = ''
  query.checkInStatus = ''
  pageNum.value = 1
  fetchList()
}

function handleRefresh() {
  pageNum.value = 1
  fetchList()
}

function handlePageChange(page) {
  pageNum.value = page
  fetchList()
}

function handleSizeChange(size) {
  pageSize.value = size
  pageNum.value = 1
  fetchList()
}

/** 序号:基于分页计算连续编号,从 1 开始(不展示 id) */
function indexMethod(index) {
  return (pageNum.value - 1) * pageSize.value + index + 1
}
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

.search-form {
  margin-bottom: 12px;
}

.error-alert {
  margin-top: 12px;
}

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
