<template>
  <div class="batch-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">批次管理</span>
          <div class="header-actions">
            <el-button :loading="loading" @click="handleRefresh">
              <el-icon><Refresh /></el-icon>
              <span>刷新数据</span>
            </el-button>
            <el-button type="primary" @click="handleCreate">
              <el-icon><Plus /></el-icon>
              <span>新增批次</span>
            </el-button>
          </div>
        </div>
      </template>

      <!-- 高级查询表单 -->
      <el-form :inline="true" :model="query" class="search-form" @submit.prevent>
        <el-form-item label="批次id">
          <el-input
            v-model="query.batchId"
            placeholder="请输入批次id"
            clearable
            style="width: 160px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="批次信息">
          <el-input
            v-model="query.batchInfo"
            placeholder="请输入批次名称/描述"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="主管老师">
          <el-input
            v-model="query.chargeTeacher"
            placeholder="请输入主管老师"
            clearable
            style="width: 160px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="批次开始日期">
          <el-date-picker
            v-model="query.queryStartTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择开始日期"
            style="width: 160px"
          />
        </el-form-item>
        <el-form-item label="批次结束日期">
          <el-date-picker
            v-model="query.queryEndTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择结束日期"
            style="width: 160px"
          />
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

      <!-- 列表上方的批量操作栏 -->
      <div class="toolbar">
        <el-button
          type="danger"
          :disabled="!selectedRows.length"
          :loading="deleting"
          @click="handleDelete"
        >
          <el-icon><Delete /></el-icon>
          <span>删除</span>
        </el-button>
        <span v-if="selectedRows.length" class="selected-tip">
          已选择 {{ selectedRows.length }} 项
        </span>
      </div>

      <!-- 数据表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column
          type="index"
          label="批次序号"
          width="90"
          align="center"
          :index="indexMethod"
        />
        <el-table-column
          prop="batchName"
          label="批次名称"
          min-width="150"
          show-overflow-tooltip
        />
        <el-table-column
          prop="batchDesc"
          label="批次描述"
          min-width="170"
          show-overflow-tooltip
        />
        <el-table-column
          prop="chargeTeacher"
          label="主管老师姓名"
          width="120"
          align="center"
        />
        <el-table-column
          prop="chargePhone"
          label="主管老师联系方式"
          width="150"
          align="center"
        />
        <el-table-column label="起止时间" min-width="240" align="center">
          <template #default="{ row }">
            <span>{{ formatRange(row) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="estimateStudentNum"
          label="预估学生数量"
          width="120"
          align="center"
        />
        <el-table-column
          prop="estimateTeacherNum"
          label="预估老师数量"
          width="120"
          align="center"
        />
        <el-table-column label="操作" width="90" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">修改</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 空数据 -->
      <el-empty
        v-if="!loading && !errorMsg && !tableData.length"
        description="暂无批次数据"
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

    <!-- 新增/修改批次对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogMode === 'edit' ? '修改批次' : '新增批次'"
      width="560px"
      :close-on-click-modal="false"
      @closed="resetForm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="110px"
      >
        <el-form-item label="批次名称" prop="batchName">
          <el-input
            v-model="form.batchName"
            placeholder="请输入批次名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="批次描述" prop="batchDesc">
          <el-input
            v-model="form.batchDesc"
            type="textarea"
            :rows="2"
            placeholder="请输入批次描述(选填)"
          />
        </el-form-item>
        <el-form-item label="预估学生数量" prop="estimateStudentNum">
          <el-input-number
            v-model="form.estimateStudentNum"
            :min="0"
            :max="999999"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="预估老师数量" prop="estimateTeacherNum">
          <el-input-number
            v-model="form.estimateTeacherNum"
            :min="0"
            :max="999999"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          v-if="dialogMode === 'edit'"
          label="主管老师"
          prop="chargeTeacher"
        >
          <el-input
            v-model="form.chargeTeacher"
            placeholder="请输入主管老师(选填)"
            clearable
          />
        </el-form-item>
        <el-form-item
          v-if="dialogMode === 'edit'"
          label="主管老师电话"
          prop="chargePhone"
        >
          <el-input
            v-model="form.chargePhone"
            placeholder="请输入主管老师电话(选填)"
            clearable
          />
        </el-form-item>
        <el-form-item label="批次开始时间" prop="batchStartTime">
          <el-date-picker
            v-model="form.batchStartTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="批次结束时间" prop="batchEndTime">
          <el-date-picker
            v-model="form.batchEndTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择结束时间"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">
          确 定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { advancedPage, createBatch, updateBatch, deleteBatch } from '@/api/batch'

const loading = ref(false)
const errorMsg = ref('')
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const selectedRows = ref([])
const deleting = ref(false)

// 高级查询条件
const query = reactive({
  batchId: '',
  batchInfo: '',
  chargeTeacher: '',
  queryStartTime: '',
  queryEndTime: ''
})

// 新增/修改批次相关状态
const dialogVisible = ref(false)
const dialogMode = ref('create') // create | edit
const submitting = ref(false)
const formRef = ref()

const defaultForm = () => ({
  batchId: null,
  batchName: '',
  batchDesc: '',
  estimateStudentNum: null,
  estimateTeacherNum: null,
  chargeTeacher: '',
  chargePhone: '',
  batchStartTime: '',
  batchEndTime: ''
})

const form = reactive(defaultForm())

// 新增时开始/结束时间为必填,修改时仅批次名称为必填(与接口文档一致)
const rules = computed(() => {
  const base = {
    batchName: [{ required: true, message: '请输入批次名称', trigger: 'blur' }]
  }
  if (dialogMode.value === 'create') {
    base.batchStartTime = [{ required: true, message: '请选择批次开始时间', trigger: 'change' }]
    base.batchEndTime = [{ required: true, message: '请选择批次结束时间', trigger: 'change' }]
  }
  return base
})

onMounted(fetchList)

async function fetchList() {
  loading.value = true
  errorMsg.value = ''
  try {
    // 携带 token 调用批次高级查询接口,后端根据 query 组合条件查询并分页
    const res = await advancedPage({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      query: buildQuery()
    })
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

/** 根据当前查询表单构造 query 对象,仅携带非空条件 */
function buildQuery() {
  const q = {}
  const id = query.batchId
  if (id !== '' && id !== null && id !== undefined) {
    const num = Number(id)
    if (Number.isFinite(num)) q.batchId = num
  }
  const info = query.batchInfo?.trim()
  if (info) q.batchInfo = info
  const teacher = query.chargeTeacher?.trim()
  if (teacher) q.chargeTeacher = teacher
  if (query.queryStartTime) q.queryStartTime = query.queryStartTime
  if (query.queryEndTime) q.queryEndTime = query.queryEndTime
  return q
}

function handleSearch() {
  pageNum.value = 1
  fetchList()
}

function handleReset() {
  query.batchId = ''
  query.batchInfo = ''
  query.chargeTeacher = ''
  query.queryStartTime = ''
  query.queryEndTime = ''
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

function handleSelectionChange(rows) {
  selectedRows.value = rows
}

async function handleDelete() {
  const ids = selectedRows.value.map((row) => row.batchId)
  if (!ids.length) return

  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${ids.length} 个批次吗?此操作不可恢复。`,
      '删除确认',
      { type: 'warning', confirmButtonText: '删除', cancelButtonText: '取消' }
    )
  } catch {
    return
  }

  deleting.value = true
  try {
    // 逐个调用删除接口,使用 allSettled 保证即使部分失败也删除其余项
    const results = await Promise.allSettled(ids.map((id) => deleteBatch(id)))
    const failedCount = results.filter((r) => r.status === 'rejected').length
    if (failedCount === 0) {
      ElMessage.success('删除成功')
    } else if (failedCount < results.length) {
      ElMessage.warning(`部分删除成功,${failedCount} 个删除失败(可能批次下存在关联数据)`)
    }
    // 若当前页数据被全部删除且非第一页,回退一页
    if (tableData.value.length === ids.length && pageNum.value > 1) {
      pageNum.value -= 1
    }
    selectedRows.value = []
    fetchList()
  } finally {
    deleting.value = false
  }
}

function handleCreate() {
  dialogMode.value = 'create'
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogMode.value = 'edit'
  Object.assign(form, {
    batchId: row.batchId,
    batchName: row.batchName || '',
    batchDesc: row.batchDesc || '',
    estimateStudentNum: row.estimateStudentNum ?? null,
    estimateTeacherNum: row.estimateTeacherNum ?? null,
    chargeTeacher: row.chargeTeacher || '',
    chargePhone: row.chargePhone || '',
    batchStartTime: normalizeTime(row.batchStartTime),
    batchEndTime: normalizeTime(row.batchEndTime)
  })
  dialogVisible.value = true
}

function resetForm() {
  formRef.value?.clearValidate()
  Object.assign(form, defaultForm())
}

/** 将列表返回的时间统一转换为 yyyy-MM-dd HH:mm:ss,保证日期选择器能正确回显 */
function normalizeTime(value) {
  if (!value) return ''
  return String(value).replace('T', ' ').slice(0, 19)
}

async function handleSubmit() {
  if (submitting.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  // 结束时间不能早于开始时间(yyyy-MM-dd HH:mm:ss 固定长度可直接按字典序比较)
  if (
    form.batchStartTime &&
    form.batchEndTime &&
    form.batchEndTime < form.batchStartTime
  ) {
    ElMessage.warning('结束时间不能早于开始时间')
    return
  }

  const isEdit = dialogMode.value === 'edit'
  submitting.value = true
  try {
    const payload = {
      batchName: form.batchName.trim(),
      batchDesc: form.batchDesc,
      estimateStudentNum: form.estimateStudentNum,
      estimateTeacherNum: form.estimateTeacherNum,
      batchStartTime: form.batchStartTime,
      batchEndTime: form.batchEndTime
    }
    if (isEdit) {
      payload.batchId = form.batchId
      payload.chargeTeacher = form.chargeTeacher
      payload.chargePhone = form.chargePhone
      await updateBatch(payload)
      ElMessage.success('修改批次成功')
    } else {
      await createBatch(payload)
      ElMessage.success('新增批次成功')
    }
    dialogVisible.value = false
    // 新增后回到第一页刷新;修改后停留在当前页刷新
    if (!isEdit) pageNum.value = 1
    fetchList()
  } catch (e) {
    // 错误提示已由响应拦截器统一处理
  } finally {
    submitting.value = false
  }
}

/**
 * 批次序号:基于分页计算连续编号,从 1 开始
 * index 为当前页内 0 基下标
 */
function indexMethod(index) {
  return (pageNum.value - 1) * pageSize.value + index + 1
}

/**
 * 将 ISO 时间字符串格式化为 yyyy-MM-dd HH:mm
 */
function formatTime(value) {
  if (!value) return '-'
  const str = String(value)
  return str.replace('T', ' ').slice(0, 16)
}

function formatRange(row) {
  const start = formatTime(row?.batchStartTime)
  const end = formatTime(row?.batchEndTime)
  if (start === '-' && end === '-') return '-'
  return `${start} ~ ${end}`
}
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.search-form {
  margin-bottom: 12px;
}

.toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.selected-tip {
  color: #909399;
  font-size: 13px;
}

.card-title {
  font-weight: 600;
  font-size: 15px;
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
