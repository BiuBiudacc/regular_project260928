<template>
  <div class="class-room-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">教室管理</span>
          <div class="header-actions">
            <el-button :loading="loading" @click="handleRefresh">
              <el-icon><Refresh /></el-icon>
              <span>刷新数据</span>
            </el-button>
            <el-button type="primary" @click="handleCreate">
              <el-icon><Plus /></el-icon>
              <span>新增教室</span>
            </el-button>
          </div>
        </div>
      </template>

      <!-- 条件检索表单 -->
      <el-form :inline="true" :model="query" class="search-form" @submit.prevent>
        <el-form-item label="教室名称">
          <el-input
            v-model="query.roomName"
            placeholder="请输入教室名称"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="教室状态">
          <el-select
            v-model="query.roomStatus"
            placeholder="请选择教室状态"
            clearable
            style="width: 160px"
          >
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
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
          prop="roomName"
          label="教室名称"
          min-width="160"
          show-overflow-tooltip
        />
        <el-table-column
          prop="roomAddress"
          label="教室地址"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column
          prop="modelName"
          label="型号名称"
          min-width="140"
          show-overflow-tooltip
        />
        <el-table-column
          prop="roomStatus"
          label="教室状态"
          width="120"
          align="center"
        >
          <template #default="{ row }">
            <el-tag v-if="row.roomStatus != null" :type="statusTagType(row.roomStatus)">
              {{ statusLabel(row.roomStatus) }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">修改</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 空数据 -->
      <el-empty
        v-if="!loading && !errorMsg && !tableData.length"
        description="暂无教室数据"
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

    <!-- 新增/修改教室对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogMode === 'edit' ? '修改教室' : '新增教室'"
      width="520px"
      :close-on-click-modal="false"
      @closed="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="教室名称" prop="roomName">
          <el-input
            v-model="form.roomName"
            placeholder="请输入教室名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="教室地址" prop="roomAddress">
          <el-input
            v-model="form.roomAddress"
            placeholder="请输入教室地址(选填)"
            clearable
          />
        </el-form-item>
        <el-form-item label="教室型号" prop="roomTypeId">
          <el-select
            v-model="form.roomTypeId"
            placeholder="请选择教室型号"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="type in typeOptions"
              :key="type.id"
              :label="type.typeName"
              :value="type.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="教室状态" prop="roomStatus">
          <el-select
            v-model="form.roomStatus"
            placeholder="请选择教室状态"
            style="width: 100%"
          >
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
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
  classRoomPage,
  addClassRoom,
  updateClassRoom,
  deleteClassRoom
} from '@/api/classRoom'
import { getClassRoomTypeList } from '@/api/classRoomType'

const loading = ref(false)
const errorMsg = ref('')
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

// 条件检索条件
const query = reactive({
  roomName: '',
  roomStatus: ''
})

// 教室状态枚举:1 正常,2 停用
const statusOptions = [
  { value: 1, label: '正常' },
  { value: 2, label: '停用' }
]
const statusMap = {
  1: '正常',
  2: '停用'
}
const statusTagMap = {
  1: 'success',
  2: 'info'
}

/** 教室状态 -> 中文文案 */
function statusLabel(value) {
  return statusMap[value] ?? String(value ?? '-')
}

/** 教室状态 -> 标签颜色 */
function statusTagType(value) {
  return statusTagMap[value] || 'info'
}

// 新增/修改教室相关状态
const dialogVisible = ref(false)
const dialogMode = ref('create') // create | edit
const submitting = ref(false)
const formRef = ref()
const typeOptions = ref([]) // 教室型号下拉选项
// 编辑时暂存列表返回的型号名称,用于列表未返回 typeId 时反查型号 id 以便回显
const editingModelName = ref('')

const defaultForm = () => ({
  id: null,
  roomName: '',
  roomAddress: '',
  roomTypeId: null,
  roomStatus: null
})

const form = reactive(defaultForm())

const rules = {
  roomName: [{ required: true, message: '请输入教室名称', trigger: 'blur' }],
  roomTypeId: [{ required: true, message: '请选择教室型号', trigger: 'change' }],
  roomStatus: [{ required: true, message: '请选择教室状态', trigger: 'change' }]
}

onMounted(fetchList)

async function fetchList() {
  loading.value = true
  errorMsg.value = ''
  try {
    // 同一接口支持通用查询(空条件)与条件检索(带条件)
    const res = await classRoomPage(buildQuery())
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
  const name = query.roomName?.trim()
  if (name) payload.roomName = name
  const status = query.roomStatus
  if (status !== '' && status !== null && status !== undefined) {
    const num = Number(status)
    if (Number.isFinite(num)) payload.roomStatus = num
  }
  return payload
}

function handleSearch() {
  pageNum.value = 1
  fetchList()
}

function handleReset() {
  query.roomName = ''
  query.roomStatus = ''
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

function handleCreate() {
  dialogMode.value = 'create'
  editingModelName.value = ''
  dialogVisible.value = true
  loadTypeOptions()
}

function handleEdit(row) {
  dialogMode.value = 'edit'
  editingModelName.value = row.modelName || ''
  Object.assign(form, {
    id: row.roomId,
    roomName: row.roomName || '',
    roomAddress: row.roomAddress || '',
    roomTypeId: row.roomTypeId ?? null,
    roomStatus: row.roomStatus ?? null
  })
  dialogVisible.value = true
  loadTypeOptions()
}

/** 加载教室型号下拉选项;编辑时若列表未返回 typeId,则依据型号名称反查 id 回显 */
async function loadTypeOptions() {
  try {
    const res = await getClassRoomTypeList()
    typeOptions.value = Array.isArray(res?.data) ? res.data : []
    if (dialogMode.value === 'edit' && form.roomTypeId == null && editingModelName.value) {
      const matched = typeOptions.value.find((t) => t.typeName === editingModelName.value)
      if (matched) form.roomTypeId = matched.id
    }
  } catch (e) {
    typeOptions.value = []
    // 错误提示已由响应拦截器统一处理
  }
}

function resetForm() {
  formRef.value?.clearValidate()
  Object.assign(form, defaultForm())
  editingModelName.value = ''
}

async function handleSubmit() {
  if (submitting.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  const isEdit = dialogMode.value === 'edit'
  submitting.value = true
  try {
    const payload = {
      roomName: form.roomName.trim(),
      roomAddress: form.roomAddress?.trim() || '',
      roomTypeId: form.roomTypeId,
      roomStatus: form.roomStatus
    }
    if (isEdit) {
      payload.id = form.id
      await updateClassRoom(payload)
      ElMessage.success('修改成功')
    } else {
      await addClassRoom(payload)
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

/** 删除教室:确认后调用删除接口 */
async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(
      `确定要删除教室「${row.roomName}」吗?此操作不可恢复。`,
      '删除确认',
      {
        type: 'warning',
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        confirmButtonClass: 'el-button--danger'
      }
    )
  } catch {
    return
  }

  try {
    await deleteClassRoom(row.roomId)
    ElMessage.success('删除成功')
    // 当前页删除后若为空且非第一页,回退一页
    if (tableData.value.length === 1 && pageNum.value > 1) {
      pageNum.value -= 1
    }
    fetchList()
  } catch (e) {
    // 错误提示已由响应拦截器统一处理
  }
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
