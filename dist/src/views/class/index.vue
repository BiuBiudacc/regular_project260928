<template>
  <div class="class-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">班级管理</span>
          <div class="header-actions">
            <el-button :loading="loading" @click="handleRefresh">
              <el-icon><Refresh /></el-icon>
              <span>刷新数据</span>
            </el-button>
            <el-button type="primary" @click="handleCreate">
              <el-icon><Plus /></el-icon>
              <span>新增班级</span>
            </el-button>
          </div>
        </div>
      </template>

      <!-- 高级检索(条件查询)表单 -->
      <el-form :inline="true" :model="query" class="search-form" @submit.prevent>
        <el-form-item label="班级名称">
          <el-input
            v-model="query.className"
            placeholder="请输入班级名称"
            clearable
            style="width: 180px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="班级状态">
          <el-select
            v-model="query.classStatus"
            placeholder="请选择班级状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="教室id">
          <el-input
            v-model="query.classRoomId"
            placeholder="请输入教室id"
            clearable
            style="width: 150px"
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

      <!-- 工具栏:班级学生调整(独立于列表) -->
      <div class="toolbar">
        <el-button
          type="primary"
          :disabled="!currentRow"
          @click="handleStudentManage"
        >
          <el-icon><User /></el-icon>
          <span>班级学生调整</span>
        </el-button>
        <span class="selected-tip">
          {{ currentRow ? `已选择:${currentRow.className}` : '请先在下方列表点击选择班级' }}
        </span>
      </div>

      <!-- 数据表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        highlight-current-row
        style="width: 100%"
        @current-change="handleCurrentChange"
      >
        <el-table-column
          type="index"
          label="班级序号"
          width="90"
          align="center"
          :index="indexMethod"
        />
        <el-table-column
          prop="className"
          label="班级名称"
          min-width="140"
          show-overflow-tooltip
        />
        <el-table-column
          prop="classDesc"
          label="班级描述"
          min-width="160"
          show-overflow-tooltip
        />
        <el-table-column
          prop="manageTeacherName"
          label="班级老师名称"
          width="120"
          align="center"
        />
        <el-table-column
          prop="teacherPhone"
          label="班级老师电话"
          width="150"
          align="center"
        />
        <el-table-column
          prop="classStatus"
          label="班级状态"
          width="100"
          align="center"
        >
          <template #default="{ row }">
            <el-tag
              v-if="row.classStatus != null"
              :type="statusTagType(row.classStatus)"
            >
              {{ statusLabel(row.classStatus) }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="roomName"
          label="教室名称"
          width="140"
          show-overflow-tooltip
        />
        <el-table-column
          prop="studentCount"
          label="学生数量"
          width="110"
          align="center"
        />
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
        description="暂无班级数据"
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

    <!-- 新增班级对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogMode === 'edit' ? '修改班级' : '新增班级'"
      width="560px"
      :close-on-click-modal="false"
      @closed="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="班级名称" prop="className">
          <el-input
            v-model="form.className"
            placeholder="请输入班级名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="班级描述" prop="classDesc">
          <el-input
            v-model="form.classDesc"
            type="textarea"
            :rows="2"
            placeholder="请输入班级描述(选填)"
          />
        </el-form-item>
        <el-form-item label="班级状态" prop="classStatus">
          <el-select
            v-model="form.classStatus"
            placeholder="请选择班级状态"
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
        <el-form-item label="所在教室" prop="classRoomId">
          <el-select
            v-model="form.classRoomId"
            placeholder="请选择教室"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="room in roomOptions"
              :key="room.id"
              :label="room.roomName"
              :value="room.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="管理老师" prop="manageTeacherName">
          <el-input
            v-model="form.manageTeacherName"
            placeholder="请输入班级管理老师(选填)"
            clearable
          />
        </el-form-item>
        <el-form-item label="老师电话" prop="teacherPhone">
          <el-input
            v-model="form.teacherPhone"
            placeholder="请输入老师联系电话(选填)"
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

    <!-- 班级学生调整对话框 -->
    <el-dialog
      v-model="studentDialogVisible"
      :title="currentRow ? `班级学生调整 - ${currentRow.className}` : '班级学生调整'"
      width="860px"
      :close-on-click-modal="false"
    >
      <el-tabs v-model="studentTab">
        <!-- 页签一:班级学生(当前班级已加入的学生) -->
        <el-tab-pane label="班级学生" name="list">
          <div v-loading="studentsLoading" class="student-dialog-body">
            <el-empty
              v-if="!studentsLoading && !studentList.length"
              description="该班级暂无学生"
            />
            <div v-else-if="studentList.length">
              <!-- 退出班级操作栏 -->
              <div class="quit-toolbar">
                <template v-if="!quitModeActive">
                  <el-button type="danger" plain @click="quitModeActive = true">
                    <el-icon><Remove /></el-icon>
                    <span>退出班级</span>
                  </el-button>
                </template>
                <template v-else>
                  <el-button
                    type="danger"
                    :disabled="!selectedQuitIds.length"
                    :loading="quittingLoading"
                    @click="handleBatchQuit"
                  >
                    <el-icon><Remove /></el-icon>
                    <span>
                      确认退出{{ selectedQuitIds.length ? `(${selectedQuitIds.length})` : '' }}
                    </span>
                  </el-button>
                  <el-button @click="cancelQuitMode">取消</el-button>
                </template>
              </div>

              <div class="student-dialog-layout">
                <!-- 学生卡片网格(仅图标 + 姓名,可多选退出) -->
                <div class="student-card-grid">
                  <div
                    v-for="stu in studentList"
                    :key="stu.id"
                    class="student-card"
                    :class="{ 'is-selected': quitModeActive && isQuitSelected(stu), 'is-preview': !quitModeActive && selectedStudent?.id === stu.id }"
                    @click="handleStudentCardClick(stu)"
                  >
                    <el-avatar
                      :size="32"
                      class="student-avatar"
                      :style="{ background: avatarBg(stu.studentGender) }"
                    >
                      {{ avatarText(stu.studentName) }}
                    </el-avatar>
                    <span class="student-name">{{ stu.studentName || '-' }}</span>
                    <el-icon v-if="quitModeActive && isQuitSelected(stu)" class="student-check">
                      <CircleCheckFilled />
                    </el-icon>
                  </div>
                </div>

              <!-- 预览卡片:显示选中学生的详情 -->
              <el-card
                v-if="selectedStudent"
                class="student-preview"
                shadow="never"
              >
                <template #header>
                  <div class="preview-header">
                    <el-icon class="preview-header-icon"><View /></el-icon>
                    <span>学生详情</span>
                  </div>
                </template>

                <div class="preview-identity">
                  <el-avatar
                    :size="56"
                    class="preview-avatar"
                    :style="{ background: avatarBg(selectedStudent.studentGender) }"
                  >
                    {{ avatarText(selectedStudent.studentName) }}
                  </el-avatar>
                  <div class="preview-name-wrap">
                    <span class="preview-name">{{ selectedStudent.studentName || '-' }}</span>
                    <el-tag
                      size="small"
                      effect="light"
                      :type="selectedStudent.studentGender === 2 ? 'danger' : 'primary'"
                    >
                      {{ genderLabel(selectedStudent.studentGender) }}
                    </el-tag>
                  </div>
                </div>

                <el-divider class="preview-divider" />

                <div class="preview-info">
                  <div class="preview-info-item">
                    <el-icon class="info-icon"><Postcard /></el-icon>
                    <span class="info-label">身份证号</span>
                    <el-tooltip
                      :content="selectedStudent.idCard || '暂无身份证号码'"
                      placement="top"
                      effect="dark"
                      :disabled="!selectedStudent.idCard"
                    >
                      <span class="info-value">{{ maskIdCard(selectedStudent.idCard) }}</span>
                    </el-tooltip>
                  </div>
                  <div class="preview-info-item">
                    <el-icon
                      class="info-icon"
                      :class="selectedStudent.studentGender === 2 ? 'c-female' : 'c-male'"
                    >
                      <Female v-if="selectedStudent.studentGender === 2" />
                      <Male v-else />
                    </el-icon>
                    <span class="info-label">性别</span>
                    <span class="info-value">{{ genderLabel(selectedStudent.studentGender) }}</span>
                  </div>
                  <div class="preview-info-item">
                    <el-icon class="info-icon"><Calendar /></el-icon>
                    <span class="info-label">年龄</span>
                    <span class="info-value">
                      {{ selectedStudent.studentAge != null ? `${selectedStudent.studentAge} 岁` : '-' }}
                    </span>
                  </div>
                  <div class="preview-info-item">
                    <el-icon class="info-icon"><Phone /></el-icon>
                    <span class="info-label">电话</span>
                    <span class="info-value">{{ selectedStudent.studentPhone || '-' }}</span>
                  </div>
                  <div class="preview-info-item">
                    <el-icon class="info-icon"><User /></el-icon>
                    <span class="info-label">紧急联系人</span>
                    <span class="info-value">{{ selectedStudent.emergencyContactName || '-' }}</span>
                  </div>
                  <div class="preview-info-item">
                    <el-icon class="info-icon"><PhoneFilled /></el-icon>
                    <span class="info-label">紧急联系电话</span>
                    <span class="info-value">{{ selectedStudent.emergencyContactPhone || '-' }}</span>
                  </div>
                </div>
              </el-card>
                <el-empty
                  v-else
                  class="preview-empty"
                  description="请选择学生查看详情"
                  :image-size="80"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 页签二:加入学生(按批次筛选学生) -->
        <el-tab-pane label="加入学生" name="add">
          <div class="add-student-tab">
            <!-- 批次下拉:基于批次简易查询接口 -->
            <div class="batch-select-row">
              <span class="batch-select-label">选择批次</span>
              <el-select
                v-model="selectedBatchId"
                placeholder="请选择批次"
                clearable
                filterable
                style="width: 260px"
                @change="handleBatchChange"
              >
                <el-option
                  v-for="b in batchOptions"
                  :key="b.batchId"
                  :label="b.batchName"
                  :value="b.batchId"
                />
              </el-select>

              <div class="batch-add-actions">
                <el-button
                  type="primary"
                  :disabled="!selectedJoinIds.length"
                  :loading="addingLoading"
                  @click="handleBatchAdd"
                >
                  <el-icon><Plus /></el-icon>
                  <span>
                    加入班级{{ selectedJoinIds.length ? `(${selectedJoinIds.length})` : '' }}
                  </span>
                </el-button>
              </div>
            </div>

            <!-- 指定批次学生卡片 -->
            <div v-loading="adjustLoading" class="adjust-student-area">
              <el-empty
                v-if="!selectedBatchId"
                description="请先选择批次"
              />
              <el-empty
                v-else-if="!adjustLoading && !adjustList.length"
                description="该批次暂无学生"
              />
              <div v-else class="adjust-card-grid">
                <el-card
                  v-for="stu in adjustList"
                  :key="stu.studentId"
                  class="adjust-card"
                  :class="{ 'is-selected': isJoinSelected(stu) }"
                  shadow="hover"
                  @click="toggleJoinStudent(stu)"
                >
                  <el-icon v-if="isJoinSelected(stu)" class="adjust-check">
                    <CircleCheckFilled />
                  </el-icon>
                  <div class="adjust-card-header">
                    <el-avatar
                      :size="32"
                      class="adjust-avatar"
                      :style="{ background: avatarBg(stu.studentGender) }"
                    >
                      {{ avatarText(stu.studentName) }}
                    </el-avatar>
                    <span class="adjust-name">{{ stu.studentName || '-' }}</span>
                    <el-icon
                      class="adjust-gender-icon"
                      :class="stu.studentGender === 2 ? 'c-female' : 'c-male'"
                    >
                      <Female v-if="stu.studentGender === 2" />
                      <Male v-else />
                    </el-icon>
                  </div>
                  <div class="adjust-card-footer">
                    <el-tag
                      size="small"
                      effect="light"
                      :type="stu.hasClass ? 'success' : 'info'"
                    >
                      {{ stu.hasClass ? '已有班级' : '未分配' }}
                    </el-tag>
                    <span class="adjust-class-name">
                      {{ stu.hasClass ? stu.className || '-' : '暂无班级' }}
                    </span>
                  </div>
                </el-card>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  classPageQuery,
  createClass,
  updateClass,
  deleteClass,
  getClassRoomSelectList,
  getClassStudentList,
  getAdjustStudentList,
  batchAddStudent,
  batchQuitStudent
} from '@/api/class'
import { getBatchSimpleList } from '@/api/batch'

const loading = ref(false)
const errorMsg = ref('')
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

// 高级检索(条件查询)条件
const query = reactive({
  className: '',
  classStatus: '',
  classRoomId: ''
})

// 班级状态枚举:1 待开班,2 授课中,3 已结束
const statusOptions = [
  { value: 1, label: '待开班' },
  { value: 2, label: '授课中' },
  { value: 3, label: '已结束' }
]
const statusMap = {
  1: '待开班',
  2: '授课中',
  3: '已结束'
}
const statusTagMap = {
  1: 'warning',
  2: 'success',
  3: 'info'
}

/** 班级状态 -> 中文文案 */
function statusLabel(value) {
  return statusMap[value] ?? String(value ?? '-')
}

/** 班级状态 -> 标签颜色 */
function statusTagType(value) {
  return statusTagMap[value] || 'info'
}

// 新增/修改班级相关状态
const dialogVisible = ref(false)
const dialogMode = ref('create') // create | edit
const submitting = ref(false)
const formRef = ref()
const roomOptions = ref([]) // 教室下拉选项

const defaultForm = () => ({
  id: null,
  className: '',
  classDesc: '',
  manageTeacherName: '',
  teacherPhone: '',
  classStatus: null,
  classRoomId: null
})

const form = reactive(defaultForm())

// 班级状态在创建与修改接口中均为必填
const rules = {
  className: [{ required: true, message: '请输入班级名称', trigger: 'blur' }],
  classStatus: [{ required: true, message: '请选择班级状态', trigger: 'change' }]
}

// 班级学生调整相关状态
const currentRow = ref(null) // 当前选中的班级行
const studentDialogVisible = ref(false)
const studentsLoading = ref(false)
const studentList = ref([]) // 学生列表
const selectedStudent = ref(null) // 当前选中的学生(预览卡片展示)

// 加入学生相关状态
const studentTab = ref('list') // 当前页签:list 班级学生 / add 加入学生
const batchOptions = ref([]) // 批次简易下拉选项
const selectedBatchId = ref(null) // 选中的批次 id
const adjustList = ref([]) // 指定批次学生列表
const adjustLoading = ref(false)
const selectedJoinIds = ref([]) // 加入页签中选中的学生 id 集合
const addingLoading = ref(false) // 批量加入进行中
const selectedQuitIds = ref([]) // 班级学生页签中选中的待退出学生 id 集合
const quittingLoading = ref(false) // 批量退出进行中
const quitModeActive = ref(false) // 是否处于退出选择模式

// 学生性别枚举:1 男,2 女(文档未给出取值,如与实际不符请调整)
const genderMap = {
  1: '男',
  2: '女'
}

function genderLabel(value) {
  return genderMap[value] ?? '-'
}

/** 身份证号脱敏:默认仅显示前 6 位,其余用 * 隐藏;悬浮可见完整号码 */
function maskIdCard(idCard) {
  if (!idCard) return '-'
  const str = String(idCard)
  if (str.length <= 6) return str
  return `${str.slice(0, 6)}${'*'.repeat(str.length - 6)}`
}

/** 学生姓名首字,用于头像占位 */
function avatarText(name) {
  const str = String(name || '').trim()
  return str ? str.charAt(0) : '?'
}

/** 头像背景色(按性别区分渐变) */
function avatarBg(gender) {
  return gender === 2
    ? 'linear-gradient(135deg, #f56c6c, #d54941)'
    : 'linear-gradient(135deg, #409eff, #2b7cd3)'
}

onMounted(fetchList)

async function fetchList(options = {}) {
  const { keepSelection = false } = options
  // 保持选中态时记录当前班级 id,刷新后按 id 恢复(用于弹窗内操作影响 studentCount 的场景)
  const prevId = keepSelection ? currentRow.value?.id : null
  loading.value = true
  errorMsg.value = ''
  currentRow.value = null
  try {
    // 同一个接口同时支持通用查询(空 query)与条件检索(带条件)
    const res = await classPageQuery({
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
    if (keepSelection && prevId != null) {
      currentRow.value = tableData.value.find((r) => r.id === prevId) || null
    }
  }
}

/** 根据当前查询表单构造 query 对象,仅携带非空条件 */
function buildQuery() {
  const q = {}
  const name = query.className?.trim()
  if (name) q.className = name
  const status = query.classStatus
  if (status !== '' && status !== null && status !== undefined) {
    const num = Number(status)
    if (Number.isFinite(num)) q.classStatus = num
  }
  const roomId = query.classRoomId
  if (roomId !== '' && roomId !== null && roomId !== undefined) {
    const num = Number(roomId)
    if (Number.isFinite(num)) q.classRoomId = num
  }
  return q
}

function handleSearch() {
  pageNum.value = 1
  fetchList()
}

function handleReset() {
  query.className = ''
  query.classStatus = ''
  query.classRoomId = ''
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

/** 班级序号:基于分页计算连续编号,从 1 开始 */
function indexMethod(index) {
  return (pageNum.value - 1) * pageSize.value + index + 1
}

function handleCreate() {
  dialogMode.value = 'create'
  dialogVisible.value = true
  loadRoomOptions()
}

function handleEdit(row) {
  dialogMode.value = 'edit'
  Object.assign(form, {
    id: row.id,
    className: row.className || '',
    classDesc: row.classDesc || '',
    manageTeacherName: row.manageTeacherName || '',
    teacherPhone: row.teacherPhone || '',
    classStatus: row.classStatus ?? null,
    classRoomId: row.classRoomId ?? null
  })
  dialogVisible.value = true
  loadRoomOptions()
}

function handleCurrentChange(row) {
  currentRow.value = row
}

/** 删除班级:确认后调用删除接口,仅无学生的班级可删除 */
async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(
      `确定要删除班级「${row.className}」吗?此操作不可恢复。`,
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
    await deleteClass(row.id)
    ElMessage.success('删除成功')
    // 若删除的是当前选中行,清空选中态
    if (currentRow.value?.id === row.id) {
      currentRow.value = null
    }
    // 当前页删除后若为空且非第一页,回退一页
    if (tableData.value.length === 1 && pageNum.value > 1) {
      pageNum.value -= 1
    }
    fetchList()
  } catch (e) {
    // 错误提示已由响应拦截器统一处理
  }
}

/** 班级学生调整:调用班级学生列表接口,以紧凑卡片展示学生,点击后在预览卡片显示详情 */
async function handleStudentManage() {
  if (!currentRow.value) {
    ElMessage.warning('请先在列表中点击选择班级')
    return
  }
  studentDialogVisible.value = true
  // 重置加入/退出页签状态
  studentTab.value = 'list'
  selectedBatchId.value = null
  adjustList.value = []
  selectedJoinIds.value = []
  selectedQuitIds.value = []
  quitModeActive.value = false
  loadBatchOptions()
  loadClassStudents()
}

/** 加载当前班级的学生列表(班级学生页签) */
async function loadClassStudents() {
  studentsLoading.value = true
  studentList.value = []
  selectedStudent.value = null
  try {
    const res = await getClassStudentList(currentRow.value.id)
    studentList.value = Array.isArray(res?.data) ? res.data : []
    // 默认选中第一个学生,便于直接查看详情
    selectedStudent.value = studentList.value[0] || null
  } catch (e) {
    studentList.value = []
    selectedStudent.value = null
    // 错误提示已由响应拦截器统一处理
  } finally {
    studentsLoading.value = false
  }
}

/** 选中某个学生,更新预览卡片 */
function selectStudent(stu) {
  selectedStudent.value = stu
}

/** 判断某学生是否已在退出选中集合中 */
function isQuitSelected(stu) {
  return selectedQuitIds.value.includes(stu.id)
}

/** 点击班级学生卡片:非选择模式仅预览,选择模式可切换退出选中态 */
function handleStudentCardClick(stu) {
  if (quitModeActive.value) {
    const id = stu.id
    const idx = selectedQuitIds.value.indexOf(id)
    if (idx > -1) {
      selectedQuitIds.value.splice(idx, 1)
    } else {
      selectedQuitIds.value.push(id)
    }
  }
  selectStudent(stu)
}

/** 取消退出选择模式 */
function cancelQuitMode() {
  quitModeActive.value = false
  selectedQuitIds.value = []
}

/** 批量退出:将选中的学生移出当前班级 */
async function handleBatchQuit() {
  const ids = selectedQuitIds.value
  if (!ids.length) return

  try {
    await ElMessageBox.confirm(
      `确定要将选中的 ${ids.length} 名学生退出当前班级吗?`,
      '退出确认',
      {
        type: 'warning',
        confirmButtonText: '退出',
        cancelButtonText: '取消'
      }
    )
  } catch {
    return
  }

  quittingLoading.value = true
  try {
    await batchQuitStudent({ studentIdList: ids })
    ElMessage.success('退出成功')
    selectedQuitIds.value = []
    quitModeActive.value = false
    // 刷新班级学生列表
    await loadClassStudents()
    // 同步刷新列表(studentCount 可能变化),并保持当前选中班级
    fetchList({ keepSelection: true })
  } catch (e) {
    // 错误提示已由响应拦截器统一处理
  } finally {
    quittingLoading.value = false
  }
}

/** 加载批次简易下拉选项(加入学生页签) */
async function loadBatchOptions() {
  try {
    const res = await getBatchSimpleList()
    batchOptions.value = Array.isArray(res?.data) ? res.data : []
  } catch (e) {
    batchOptions.value = []
    // 错误提示已由响应拦截器统一处理
  }
}

/** 批次下拉变更:根据所选批次查询该批次学生 */
async function handleBatchChange(batchId) {
  selectedJoinIds.value = []
  if (!batchId) {
    adjustList.value = []
    return
  }
  adjustLoading.value = true
  adjustList.value = []
  try {
    const res = await getAdjustStudentList(batchId)
    adjustList.value = Array.isArray(res?.data) ? res.data : []
  } catch (e) {
    adjustList.value = []
    // 错误提示已由响应拦截器统一处理
  } finally {
    adjustLoading.value = false
  }
}

/** 判断某学生是否已在加入选中集合中 */
function isJoinSelected(stu) {
  return selectedJoinIds.value.includes(stu.studentId)
}

/** 切换加入页签中某个学生的选中态(点选可多选) */
function toggleJoinStudent(stu) {
  const id = stu.studentId
  const idx = selectedJoinIds.value.indexOf(id)
  if (idx > -1) {
    selectedJoinIds.value.splice(idx, 1)
    return
  }
  // 若该学生已有班级,提示但不拦截
  if (stu.hasClass) {
    ElMessage.warning('该学生已有班级,加入后将转移其班级')
  }
  selectedJoinIds.value.push(id)
}

/** 批量加入:将选中的学生加入当前班级 */
async function handleBatchAdd() {
  const ids = selectedJoinIds.value
  if (!ids.length || !currentRow.value) return

  // 若选中学生中已有班级,提示确认但允许继续(不强行拦截)
  const selectedStus = adjustList.value.filter((s) => ids.includes(s.studentId))
  const hasClassCount = selectedStus.filter((s) => s.hasClass).length
  if (hasClassCount > 0) {
    try {
      await ElMessageBox.confirm(
        `选中的学生中有 ${hasClassCount} 名已有班级,加入后将从原班级转移至当前班级,是否继续?`,
        '加入确认',
        {
          type: 'warning',
          confirmButtonText: '继续加入',
          cancelButtonText: '取消'
        }
      )
    } catch {
      return
    }
  }

  addingLoading.value = true
  try {
    await batchAddStudent({
      classId: currentRow.value.id,
      studentIdList: ids
    })
    ElMessage.success('加入成功')
    selectedJoinIds.value = []
    // 刷新班级学生页签与当前批次学生列表
    await loadClassStudents()
    if (selectedBatchId.value) {
      await handleBatchChange(selectedBatchId.value)
    }
    // 同步刷新列表(studentCount 可能变化),并保持当前选中班级
    fetchList({ keepSelection: true })
  } catch (e) {
    // 错误提示已由响应拦截器统一处理
  } finally {
    addingLoading.value = false
  }
}

/** 打开表单时调用教室信息查询接口,渲染教室下拉选项 */
async function loadRoomOptions() {
  try {
    const res = await getClassRoomSelectList()
    roomOptions.value = Array.isArray(res?.data) ? res.data : []
  } catch (e) {
    roomOptions.value = []
    // 错误提示已由响应拦截器统一处理
  }
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
    if (isEdit) {
      await updateClass({
        id: form.id,
        className: form.className.trim(),
        classDesc: form.classDesc,
        manageTeacherName: form.manageTeacherName,
        teacherPhone: form.teacherPhone,
        classStatus: form.classStatus,
        classRoomId: form.classRoomId
      })
      ElMessage.success('修改班级成功')
    } else {
      await createClass({
        className: form.className.trim(),
        classDesc: form.classDesc,
        manageTeacherName: form.manageTeacherName,
        teacherPhone: form.teacherPhone,
        classStatus: form.classStatus,
        classRoomId: form.classRoomId
      })
      ElMessage.success('新增班级成功')
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

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
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

.student-dialog-body {
  min-height: 120px;
}

.student-dialog-layout {
  display: flex;
  gap: 16px;
  align-items: flex-start;
}

/* 学生卡片网格(仅图标 + 姓名) */
.student-card-grid {
  flex: 1;
  min-width: 0;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(130px, 1fr));
  gap: 10px;
  align-content: start;
}

.student-card {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: #f8f9fb;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.student-card:hover {
  border-color: #c6e2ff;
  background: #ecf5ff;
}

.student-card.is-selected {
  border-color: #409eff;
  background: #ecf5ff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.15);
}

.student-card.is-preview {
  border-color: #a0cfff;
  background: #f0f7ff;
}

.student-avatar {
  flex-shrink: 0;
  font-weight: 600;
  color: #fff;
}

.student-name {
  flex: 1;
  min-width: 0;
  font-size: 14px;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.student-check {
  flex-shrink: 0;
  font-size: 16px;
  color: #409eff;
}

.quit-toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

/* 预览卡片 */
.student-preview {
  width: 300px;
  flex-shrink: 0;
  position: sticky;
  top: 0;
  border-radius: 12px;
}

.preview-header {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 600;
  font-size: 14px;
  color: #303133;
}

.preview-header-icon {
  color: #409eff;
  font-size: 16px;
}

.preview-identity {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 4px 0;
}

.preview-avatar {
  font-weight: 600;
  color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
}

.preview-name-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.preview-name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.preview-divider {
  margin: 12px 0;
}

.preview-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.preview-info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 10px;
  background: #f5f7fa;
  border-radius: 8px;
  font-size: 13px;
}

.info-icon {
  font-size: 16px;
  color: #909399;
  flex-shrink: 0;
}

.info-label {
  color: #909399;
  flex-shrink: 0;
}

.info-value {
  color: #303133;
  margin-left: auto;
  text-align: right;
  word-break: break-all;
}

.preview-empty {
  width: 300px;
  flex-shrink: 0;
}

.c-male {
  color: #409eff;
}

.c-female {
  color: #f56c6c;
}

/* 加入学生页签 */
.add-student-tab {
  min-height: 200px;
}

.batch-select-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.batch-select-label {
  font-size: 14px;
  color: #606266;
  flex-shrink: 0;
}

.batch-add-actions {
  margin-left: auto;
  flex-shrink: 0;
}

.adjust-student-area {
  min-height: 120px;
}

.adjust-card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 12px;
}

.adjust-card {
  position: relative;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s;
}

.adjust-card:hover {
  border-color: #c6e2ff;
}

.adjust-card.is-selected {
  border-color: #409eff;
  background: #ecf5ff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.15);
}

.adjust-check {
  position: absolute;
  top: 8px;
  right: 8px;
  font-size: 18px;
  color: #409eff;
}

.adjust-card-header {
  display: flex;
  align-items: center;
  gap: 10px;
}

.adjust-avatar {
  flex-shrink: 0;
  font-weight: 600;
  color: #fff;
}

.adjust-name {
  flex: 1;
  min-width: 0;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.adjust-gender-icon {
  font-size: 18px;
  flex-shrink: 0;
}

.adjust-card-footer {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 12px;
}

.adjust-class-name {
  min-width: 0;
  font-size: 12px;
  color: #606266;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
