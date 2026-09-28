<template>
  <div class="class-room-type-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">教室型号码表</span>
          <div class="header-actions">
            <el-button :loading="loading" @click="handleRefresh">
              <el-icon><Refresh /></el-icon>
              <span>刷新数据</span>
            </el-button>
            <el-button type="primary" @click="handleCreate">
              <el-icon><Plus /></el-icon>
              <span>新增型号</span>
            </el-button>
          </div>
        </div>
      </template>

      <!-- 查询表单 -->
      <el-form :inline="true" :model="query" class="search-form" @submit.prevent>
        <el-form-item label="型号名称">
          <el-input
            v-model="query.typeName"
            placeholder="请输入型号名称"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
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
          @click="handleBatchDelete"
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
          label="序号"
          width="80"
          align="center"
          :index="indexMethod"
        />
        <el-table-column
          prop="id"
          label="型号ID"
          width="120"
          align="center"
        />
        <el-table-column
          prop="typeName"
          label="型号名称"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">修改</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 空数据 -->
      <el-empty
        v-if="!loading && !errorMsg && !tableData.length"
        description="暂无教室型号数据"
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

    <!-- 新增/修改型号对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogMode === 'edit' ? '修改教室型号' : '新增教室型号'"
      width="480px"
      :close-on-click-modal="false"
      @closed="resetForm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="90px"
      >
        <el-form-item label="型号名称" prop="typeName">
          <el-input
            v-model="form.typeName"
            placeholder="请输入型号名称"
            clearable
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  classRoomTypePage,
  addClassRoomType,
  updateClassRoomType,
  removeClassRoomType
} from '@/api/classRoomType'

const loading = ref(false)
const errorMsg = ref('')
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const selectedRows = ref([])
const deleting = ref(false)

// 查询条件
const query = reactive({
  typeName: ''
})

// 新增/修改型号相关状态
const dialogVisible = ref(false)
const dialogMode = ref('create') // create | edit
const submitting = ref(false)
const formRef = ref()

const defaultForm = () => ({
  id: null,
  typeName: ''
})

const form = reactive(defaultForm())

const rules = {
  typeName: [{ required: true, message: '请输入型号名称', trigger: 'blur' }]
}

onMounted(fetchList)

async function fetchList() {
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await classRoomTypePage({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      typeName: query.typeName?.trim() || ''
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

function handleSearch() {
  pageNum.value = 1
  fetchList()
}

function handleReset() {
  query.typeName = ''
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

function handleCreate() {
  dialogMode.value = 'create'
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogMode.value = 'edit'
  Object.assign(form, {
    id: row.id,
    typeName: row.typeName || ''
  })
  dialogVisible.value = true
}

function resetForm() {
  formRef.value?.clearValidate()
  Object.assign(form, defaultForm())
}

async function handleSubmit() {
  if (submitting.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  const isEdit = dialogMode.value === 'edit'
  submitting.value = true
  try {
    const typeName = form.typeName.trim()
    if (isEdit) {
      await updateClassRoomType({ id: form.id, typeName })
      ElMessage.success('修改成功')
    } else {
      await addClassRoomType({ typeName })
      ElMessage.success('新增成功')
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

async function confirmDelete(rows) {
  const label = rows.length > 1 ? `选中的 ${rows.length} 个型号` : `型号「${rows[0].typeName}」`
  try {
    await ElMessageBox.confirm(
      `确定要删除${label}吗?此操作不可恢复。`,
      '删除确认',
      { type: 'warning', confirmButtonText: '删除', cancelButtonText: '取消' }
    )
    return true
  } catch {
    return false
  }
}

async function execDelete(rows) {
  deleting.value = true
  try {
    const ids = rows.map((row) => row.id)
    // 逐个调用删除接口,使用 allSettled 保证即使部分失败也删除其余项
    const results = await Promise.allSettled(ids.map((id) => removeClassRoomType(id)))
    const failedCount = results.filter((r) => r.status === 'rejected').length
    if (failedCount === 0) {
      ElMessage.success('删除成功')
    } else if (failedCount < results.length) {
      ElMessage.warning(`部分删除成功,${failedCount} 个删除失败`)
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

async function handleDelete(row) {
  if (!(await confirmDelete([row]))) return
  await execDelete([row])
}

async function handleBatchDelete() {
  if (!selectedRows.value.length) return
  if (!(await confirmDelete(selectedRows.value))) return
  await execDelete(selectedRows.value)
}

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
