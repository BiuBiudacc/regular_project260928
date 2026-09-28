<template>
  <div class="student-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">学生管理</span>
          <div class="header-actions">
            <el-button :loading="loading" @click="handleRefresh">
              <el-icon><Refresh /></el-icon>
              <span>刷新数据</span>
            </el-button>
            <el-button type="primary" @click="handleCreate">
              <el-icon><Plus /></el-icon>
              <span>新增学生</span>
            </el-button>
          </div>
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

      <!-- 工具栏:学生入住(独立于列表) -->
      <div class="toolbar">
        <el-button
          type="primary"
          :disabled="!selectedStudents.length"
          @click="handleCheckIn"
        >
          <el-icon><Key /></el-icon>
          <span>入住设置</span>
        </el-button>
        <el-button
          type="danger"
          plain
          :disabled="!selectedStudents.length"
          @click="handleCheckOut"
        >
          <el-icon><SwitchButton /></el-icon>
          <span>退房</span>
        </el-button>
        <el-button
          :disabled="selectedStudents.length !== 1"
          @click="handleRenew"
        >
          <el-icon><Calendar /></el-icon>
          <span>续住</span>
        </el-button>
        <el-button
          :disabled="selectedStudents.length !== 1"
          @click="handleViewRecord"
        >
          <el-icon><View /></el-icon>
          <span>入住记录查看</span>
        </el-button>
        <span class="selected-tip">
          {{
            selectedStudents.length
              ? `已选择 ${selectedStudents.length} 名学生(${genderLabel(selectedStudents[0].studentGender)})`
              : '请勾选学生(仅支持同性别多选)'
          }}
        </span>
      </div>

      <!-- 数据表格 -->
      <el-table
        ref="tableRef"
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          width="50"
          align="center"
          :selectable="isRowSelectable"
        />
        <el-table-column
          type="index"
          label="序号"
          width="80"
          align="center"
          :index="indexMethod"
        />
        <el-table-column prop="studentName" label="学生姓名" min-width="130">
          <template #default="{ row }">
            <el-tooltip
              v-if="warnStatusLabel(row.warnStatus)"
              :content="`退房提醒:${warnStatusLabel(row.warnStatus)}`"
              placement="top"
              effect="dark"
            >
              <span class="student-name-wrap">
                <span class="student-name-text">{{ row.studentName }}</span>
                <el-icon
                  class="warn-icon"
                  :class="warnStatusLabel(row.warnStatus) === '已超期' ? 'is-overdue' : 'is-today'"
                >
                  <WarningFilled />
                </el-icon>
              </span>
            </el-tooltip>
            <span v-else>{{ row.studentName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="性别" width="80" align="center">
          <template #default="{ row }">
            <span>{{ genderLabel(row.studentGender) }}</span>
          </template>
        </el-table-column>
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

    <!-- 新增/修改学生对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogMode === 'edit' ? '修改学生' : '新增学生'"
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
        <el-form-item label="学生姓名" prop="studentName">
          <el-input
            v-model="form.studentName"
            placeholder="请输入学生姓名"
            clearable
          />
        </el-form-item>
        <el-form-item label="身份证号码" prop="idCard">
          <el-input
            v-model="form.idCard"
            placeholder="请输入身份证号码(选填)"
            clearable
          />
        </el-form-item>
        <el-form-item label="学生性别" prop="studentGender">
          <el-select
            v-model="form.studentGender"
            placeholder="请选择性别(选填)"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="g in genderOptions"
              :key="g.value"
              :label="g.label"
              :value="g.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="学生年龄" prop="studentAge">
          <el-input-number
            v-model="form.studentAge"
            :min="0"
            :max="150"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="联系方式" prop="studentPhone">
          <el-input
            v-model="form.studentPhone"
            placeholder="请输入联系方式(选填)"
            clearable
          />
        </el-form-item>
        <el-form-item label="紧急联系人" prop="emergencyContactName">
          <el-input
            v-model="form.emergencyContactName"
            placeholder="请输入紧急联系人(选填)"
            clearable
          />
        </el-form-item>
        <el-form-item label="紧急联系电话" prop="emergencyContactPhone">
          <el-input
            v-model="form.emergencyContactPhone"
            placeholder="请输入紧急联系电话(选填)"
            clearable
          />
        </el-form-item>
        <el-form-item label="所属批次" prop="batchId">
          <el-select
            v-model="form.batchId"
            placeholder="请选择批次"
            clearable
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="b in batchOptions"
              :key="b.batchId"
              :label="b.batchName"
              :value="b.batchId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属班级" prop="classId">
          <el-select
            v-model="form.classId"
            placeholder="请选择班级(选填)"
            clearable
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="c in classOptions"
              :key="c.id"
              :label="c.className"
              :value="c.id"
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

    <!-- 学生入住对话框 -->
    <el-dialog
      v-model="checkInDialogVisible"
      title="学生入住 - 批量绑定床位"
      width="980px"
      :close-on-click-modal="false"
      @closed="resetCheckInDialog"
    >
      <!-- 入住时间区间表单 -->
      <el-form :inline="true" :model="checkInForm" class="check-in-form" @submit.prevent>
        <el-form-item label="入住时间区间">
          <el-date-picker
            v-model="checkInForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="入住日期"
            end-placeholder="退宿日期"
            value-format="YYYY-MM-DD"
            style="width: 300px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="bedLoading" @click="handleBedQuery">
            <el-icon><Search /></el-icon>
            <span>查询可用床位</span>
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 待分配学生(可拖动到床位) -->
      <div class="student-pool">
        <div class="pool-header">
          <span class="pool-title">待分配学生</span>
          <span class="pool-count">
            {{
              bindings.length
                ? `已绑定 ${bindings.length} / ${checkInStudents.length}`
                : `共 ${checkInStudents.length} 名`
            }}
          </span>
        </div>
        <div class="student-chip-list">
          <div
            v-for="stu in unassignedStudents"
            :key="stu.id"
            class="student-chip"
            :class="{ 'is-dragging': draggingStudentId === stu.id }"
            draggable="true"
            @dragstart="onDragStartStudent($event, stu)"
            @dragend="onDragEndStudent"
          >
            <el-icon class="chip-drag-icon"><Rank /></el-icon>
            <span class="chip-name">{{ stu.studentName }}</span>
            <el-tag
              size="small"
              effect="light"
              :type="stu.studentGender === 2 ? 'danger' : 'primary'"
            >
              {{ genderLabel(stu.studentGender) }}
            </el-tag>
          </div>
          <span v-if="checkInStudents.length && !unassignedStudents.length" class="pool-empty-tip">
            全部学生均已绑定床位
          </span>
        </div>
      </div>

      <!-- 床位查询结果视图 -->
      <div v-loading="bedLoading" class="bed-result-area">
        <el-alert
          v-if="bedErrorMsg"
          :title="bedErrorMsg"
          type="error"
          show-icon
          :closable="false"
          class="error-alert"
        />
        <el-empty
          v-else-if="!bedLoading && !roomGroups.length"
          :description="bedQueried ? '该时间段暂无床位数据' : '请设置入住时间区间后点击查询'"
        />
        <div v-else-if="roomGroups.length" class="room-list">
          <el-card
            v-for="room in roomGroups"
            :key="room.roomId"
            class="room-card"
            shadow="never"
          >
            <template #header>
              <div class="room-header">
                <span class="room-name">{{ room.roomName }}</span>
                <el-tag size="small" effect="plain" type="info">{{ room.typeName }}</el-tag>
                <span class="room-desc">{{ room.roomDesc }}</span>
              </div>
            </template>
            <div class="bed-grid">
              <div
                v-for="bed in room.beds"
                :key="bed.bedId"
                class="bed-item"
                :class="{
                  'is-idle': isBedDroppable(bed),
                  'is-assigned': !!bindingOfBed(bed),
                  'is-dragover': dragOverBedId === bed.bedId
                }"
                @dragover="onDragOverBed($event, bed)"
                @dragleave="onDragLeaveBed(bed)"
                @drop.prevent="onDropOnBed($event, bed)"
              >
                <div class="bed-name">{{ bed.bedName }}</div>
                <el-tag :type="bedStatusTagType(bed.bedStatus)" size="small">
                  {{ bed.bedStatus }}
                </el-tag>
                <div
                  class="bed-occupant"
                  :class="{
                    'is-empty': !bindingOfBed(bed) && !bed.studentName,
                    'is-pending': !!bindingOfBed(bed)
                  }"
                >
                  {{ bindingOfBed(bed)?.studentName || bed.studentName || '空床位' }}
                </div>
              </div>
            </div>
          </el-card>
        </div>
      </div>

      <!-- 已绑定汇总 -->
      <div v-if="bindings.length" class="binding-summary">
        <span class="binding-title">已绑定床位:</span>
        <div class="binding-chip-list">
          <el-tag
            v-for="b in bindings"
            :key="b.studentId"
            type="success"
            closable
            @close="removeBinding(b.studentId)"
          >
            {{ b.studentName }} → {{ b.roomName }} / {{ b.bedName }}
          </el-tag>
        </div>
      </div>

      <template #footer>
        <el-button @click="checkInDialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          :loading="saving"
          :disabled="!bindings.length"
          @click="handleSaveCheckIn"
        >
          保存入住{{ bindings.length ? `(${bindings.length})` : '' }}
        </el-button>
      </template>
    </el-dialog>

    <!-- 入住记录查看对话框 -->
    <el-dialog
      v-model="recordDialogVisible"
      :title="recordStudent ? `入住记录 - ${recordStudent.studentName}` : '入住记录'"
      width="960px"
      :close-on-click-modal="false"
      @closed="resetRecordDialog"
    >
      <div v-loading="recordLoading" class="record-body">
        <el-alert
          v-if="recordErrorMsg"
          :title="recordErrorMsg"
          type="error"
          show-icon
          :closable="false"
          class="error-alert"
        />
        <el-table
          v-else-if="recordList.length"
          :data="recordList"
          border
          stripe
          style="width: 100%"
        >
          <el-table-column type="index" label="序号" width="55" align="center" />
          <el-table-column prop="roomName" label="宿舍名称" min-width="110" show-overflow-tooltip>
            <template #default="{ row }">{{ row.roomName || '-' }}</template>
          </el-table-column>
          <el-table-column prop="bedName" label="床位名称" min-width="110" show-overflow-tooltip>
            <template #default="{ row }">{{ row.bedName || '-' }}</template>
          </el-table-column>
          <el-table-column prop="checkInDate" label="入住日期" min-width="150" show-overflow-tooltip>
            <template #default="{ row }">{{ row.checkInDate || '-' }}</template>
          </el-table-column>
          <el-table-column prop="estimateLeaveTime" label="预计离开日期" min-width="150" show-overflow-tooltip>
            <template #default="{ row }">{{ row.estimateLeaveTime || '-' }}</template>
          </el-table-column>
          <el-table-column prop="realLeaveTime" label="实际离开日期" min-width="150" show-overflow-tooltip>
            <template #default="{ row }">{{ row.realLeaveTime || '-' }}</template>
          </el-table-column>
          <el-table-column label="入住状态" width="100" align="center">
            <template #default="{ row }">
              <el-tag v-if="row.status" :type="recordStatusTagType(row.status)">
                {{ row.status }}
              </el-tag>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="70" align="center">
            <template #default="{ row }">
              <el-button
                type="danger"
                link
                :loading="deletingRecordId === row.id"
                @click="handleDeleteRecord(row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty
          v-else-if="!recordLoading"
          description="该学生暂无入住记录"
        />
      </div>
    </el-dialog>

    <!-- 退房对话框 -->
    <el-dialog
      v-model="checkOutDialogVisible"
      title="退房"
      width="480px"
      :close-on-click-modal="false"
      @closed="resetCheckOutDialog"
    >
      <el-form :model="checkOutForm" label-width="90px" @submit.prevent>
        <el-form-item label="退房学生">
          <div class="check-out-students">
            <el-tag
              v-for="s in checkOutStudents"
              :key="s.id"
              class="check-out-student-tag"
              type="info"
            >
              {{ s.studentName }}
            </el-tag>
          </div>
        </el-form-item>
        <el-form-item label="退房日期">
          <el-date-picker
            v-model="checkOutForm.checkOutDate"
            type="date"
            placeholder="请选择退房日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="checkOutDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="checkingOut" @click="handleCheckOutSubmit">
          确 定
        </el-button>
      </template>
    </el-dialog>

    <!-- 续住对话框 -->
    <el-dialog
      v-model="renewDialogVisible"
      title="续住"
      width="520px"
      :close-on-click-modal="false"
      @closed="resetRenewDialog"
    >
      <div v-loading="renewLoading" class="renew-body">
        <el-alert
          v-if="renewErrorMsg"
          :title="renewErrorMsg"
          type="error"
          show-icon
          :closable="false"
          class="error-alert"
        />
        <template v-else-if="renewInfo">
          <el-descriptions :column="1" border size="small" class="renew-desc">
            <el-descriptions-item label="宿舍">{{ renewInfo.roomName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="床位">{{ renewInfo.bedName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="入住时间">{{ renewInfo.checkInDate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="预计退房时间">{{ renewInfo.estimateLeaveTime || '-' }}</el-descriptions-item>
          </el-descriptions>
          <el-form label-width="110px" class="renew-form" @submit.prevent>
            <el-form-item label="续住至日期">
              <el-date-picker
                v-model="renewForm.newDate"
                type="date"
                placeholder="请选择续住日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-form>
        </template>
        <el-empty v-else-if="!renewLoading" description="该学生暂无入住信息,无法续住" />
      </div>
      <template #footer>
        <el-button @click="renewDialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          :loading="renewing"
          :disabled="!renewInfo"
          @click="handleRenewSubmit"
        >
          确定续住
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, nextTick, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { studentPage, createStudent, updateStudent, deleteStudent } from '@/api/student'
import { getBatchSimpleList } from '@/api/batch'
import { getClassSimpleList } from '@/api/class'
import { getAvailableBed } from '@/api/bed'
import {
  batchCheckIn,
  getStudentRecord,
  deleteRelRecord,
  batchCheckOut,
  getCurrentCheckIn,
  renew
} from '@/api/bedStudentRel'

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

// 批次下拉选项(检索表单 + 新增/修改表单共用)
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

/** 到期提醒文案:1 今日到期,2 已超期 */
function warnStatusLabel(status) {
  const s = Number(status)
  return { 1: '今日到期', 2: '已超期' }[s] || ''
}

// 学生性别枚举:1 男,2 女(与班级学生调整模块保持一致)
const genderOptions = [
  { value: 1, label: '男' },
  { value: 2, label: '女' }
]
const genderMap = {
  1: '男',
  2: '女'
}

/** 学生性别 -> 中文文案 */
function genderLabel(value) {
  return genderMap[value] ?? '-'
}

// 新增/修改学生相关状态
const dialogVisible = ref(false)
const dialogMode = ref('create') // create | edit
const submitting = ref(false)
const formRef = ref()
const classOptions = ref([]) // 班级下拉选项(新增/修改表单)

const defaultForm = () => ({
  id: null,
  studentName: '',
  idCard: '',
  studentGender: null,
  studentAge: null,
  studentPhone: '',
  emergencyContactName: '',
  emergencyContactPhone: '',
  batchId: null,
  classId: null
})

const form = reactive(defaultForm())

// 学生姓名与所属批次为必填(与接口文档 required 一致)
const rules = {
  studentName: [{ required: true, message: '请输入学生姓名', trigger: 'blur' }],
  batchId: [{ required: true, message: '请选择所属批次', trigger: 'change' }]
}

// 学生入住相关状态
const tableRef = ref() // 表格实例(用于控制勾选)
const selectedStudents = ref([]) // 已勾选学生(仅同性别)
const lockedGender = ref(null) // 锁定性别:一旦勾选,后续只能勾选同性别学生
const checkInDialogVisible = ref(false)
const checkInStudents = ref([]) // 进入入住对话框的学生快照
const bindings = ref([]) // 学生-床位绑定关系 [{ studentId, studentName, bedId, bedName, roomId, roomName }]
const saving = ref(false) // 保存入住中
const draggingStudentId = ref(null) // 正在拖动的学生 id
const dragOverBedId = ref(null) // 当前拖拽悬停的床位 id
const checkInForm = reactive({
  dateRange: [] // [入住日期, 退宿日期]
})
const bedLoading = ref(false)
const bedErrorMsg = ref('')
const bedQueried = ref(false) // 是否已执行过查询
const roomGroups = ref([]) // 按宿舍分组的床位信息

// 待分配学生(尚未绑定床位)
const unassignedStudents = computed(() =>
  checkInStudents.value.filter((s) => !bindings.value.some((b) => b.studentId === s.id))
)

// 入住记录查看相关状态
const recordDialogVisible = ref(false)
const recordStudent = ref(null) // 当前查看入住记录的学生
const recordList = ref([])
const recordLoading = ref(false)
const recordErrorMsg = ref('')
const deletingRecordId = ref(null) // 正在删除的入住记录 id

// 退房相关状态
const checkOutDialogVisible = ref(false)
const checkOutStudents = ref([]) // 待退房学生快照
const checkOutForm = reactive({ checkOutDate: '' })
const checkingOut = ref(false)

// 续住相关状态
const renewDialogVisible = ref(false)
const renewStudent = ref(null) // 当前续住学生快照
const renewInfo = ref(null) // 当前入住信息
const renewLoading = ref(false)
const renewErrorMsg = ref('')
const renewForm = reactive({ newDate: '' })
const renewing = ref(false)

onMounted(() => {
  fetchList()
  loadBatchOptions()
})

async function fetchList(options = {}) {
  const { keepSelection = false } = options
  // 需要保留勾选时,先记录当前勾选的学生 id,刷新后恢复
  const prevIds = keepSelection ? selectedStudents.value.map((s) => Number(s.id)) : []
  loading.value = true
  errorMsg.value = ''
  if (!keepSelection) clearSelection()
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
    if (keepSelection && prevIds.length) {
      nextTick(() => {
        const rows = tableData.value.filter((r) => prevIds.includes(Number(r.id)))
        if (!rows.length) {
          clearSelection()
          return
        }
        rows.forEach((r) => tableRef.value?.toggleRowSelection(r, true))
      })
    }
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

/** 加载批次简易下拉选项(检索表单 + 表单对话框) */
async function loadBatchOptions() {
  try {
    const res = await getBatchSimpleList()
    batchOptions.value = Array.isArray(res?.data) ? res.data : []
  } catch (e) {
    batchOptions.value = []
    // 错误提示已由响应拦截器统一处理
  }
}

/** 加载班级简易下拉选项(表单对话框) */
async function loadClassOptions() {
  try {
    const res = await getClassSimpleList()
    classOptions.value = Array.isArray(res?.data) ? res.data : []
  } catch (e) {
    classOptions.value = []
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

function handleCreate() {
  dialogMode.value = 'create'
  dialogVisible.value = true
  loadClassOptions()
}

function handleEdit(row) {
  dialogMode.value = 'edit'
  Object.assign(form, {
    id: row.id,
    studentName: row.studentName || '',
    idCard: row.idCard || '',
    studentGender: row.studentGender ?? null,
    studentAge: row.studentAge ?? null,
    studentPhone: row.studentPhone || '',
    emergencyContactName: row.emergencyContactName || '',
    emergencyContactPhone: row.emergencyContactPhone || '',
    batchId: row.batchId ?? null,
    classId: row.classId ?? null
  })
  dialogVisible.value = true
  loadClassOptions()
}

/** 删除学生:确认后调用删除接口 */
async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(
      `确定要删除学生「${row.studentName}」吗?此操作不可恢复。`,
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
    await deleteStudent(row.id)
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
    const payload = {
      studentName: form.studentName.trim(),
      idCard: form.idCard,
      studentGender: form.studentGender,
      studentAge: form.studentAge,
      studentPhone: form.studentPhone,
      emergencyContactName: form.emergencyContactName,
      emergencyContactPhone: form.emergencyContactPhone,
      batchId: form.batchId,
      classId: form.classId
    }
    if (isEdit) {
      await updateStudent(form.id, payload)
      ElMessage.success('修改学生成功')
    } else {
      await createStudent(payload)
      ElMessage.success('新增学生成功')
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

/** 清空表格勾选并重置锁定性别 */
function clearSelection() {
  selectedStudents.value = []
  lockedGender.value = null
  tableRef.value?.clearSelection()
}

/** 勾选变化:强制同性别,自动取消不同性别选择 */
function handleSelectionChange(selection) {
  const rows = Array.isArray(selection) ? selection : []
  if (!rows.length) {
    lockedGender.value = null
    selectedStudents.value = []
    return
  }
  const target = lockedGender.value ?? rows[0].studentGender
  const same = rows.filter((r) => r.studentGender === target)
  const diff = rows.filter((r) => r.studentGender !== target)
  if (diff.length) {
    nextTick(() => {
      diff.forEach((r) => tableRef.value?.toggleRowSelection(r, false))
    })
    ElMessage.warning('仅支持选择同性别学生,已自动取消不同性别选择')
  }
  lockedGender.value = same.length ? target : null
  selectedStudents.value = same
}

/** 行是否可勾选:锁定性别后仅同性别可勾选 */
function isRowSelectable(row) {
  return lockedGender.value == null || row.studentGender === lockedGender.value
}

/** 打开入住对话框:将勾选学生快照带入,重置时间区间与上次查询结果 */
function handleCheckIn() {
  if (!selectedStudents.value.length) {
    ElMessage.warning('请先在列表中勾选学生')
    return
  }
  checkInStudents.value = selectedStudents.value.map((s) => ({
    id: Number(s.id),
    studentName: s.studentName,
    studentGender: s.studentGender
  }))
  bindings.value = []
  draggingStudentId.value = null
  dragOverBedId.value = null
  checkInForm.dateRange = []
  roomGroups.value = []
  bedErrorMsg.value = ''
  bedQueried.value = false
  checkInDialogVisible.value = true
}

/** 查询可用床位:入参性别取勾选学生(同性别)的性别 */
async function handleBedQuery() {
  const range = checkInForm.dateRange
  if (!range || range.length !== 2) {
    ElMessage.warning('请选择入住时间区间')
    return
  }
  bedLoading.value = true
  bedErrorMsg.value = ''
  bindings.value = []
  try {
    const res = await getAvailableBed({
      checkInDate: range[0],
      checkOutDate: range[1],
      studentGender: checkInStudents.value[0]?.studentGender ?? null
    })
    roomGroups.value = groupBedsByRoom(Array.isArray(res?.data) ? res.data : [])
    bedQueried.value = true
  } catch (e) {
    bedErrorMsg.value = e?.message || '查询失败,请检查后端服务是否启动'
    roomGroups.value = []
    bedQueried.value = true
  } finally {
    bedLoading.value = false
  }
}

/** 将扁平床位列表按宿舍 id 分组 */
function groupBedsByRoom(list) {
  const map = new Map()
  for (const bed of list) {
    const key = bed.roomId
    if (!map.has(key)) {
      map.set(key, {
        roomId: bed.roomId,
        roomName: bed.roomName,
        roomDesc: bed.roomDesc,
        typeName: bed.typeName,
        beds: []
      })
    }
    map.get(key).beds.push(bed)
  }
  return Array.from(map.values())
}

/** 床位状态 -> 标签颜色(空闲绿色,其余红色) */
function bedStatusTagType(status) {
  return status === '空闲' ? 'success' : 'danger'
}

/** 某床位是否已存在待保存的绑定关系 */
function bindingOfBed(bed) {
  return bindings.value.find((b) => b.bedId === bed.bedId)
}

/** 床位是否可接收拖放:仅空闲且尚未绑定 */
function isBedDroppable(bed) {
  return bed.bedStatus === '空闲' && !bindingOfBed(bed)
}

/** 开始拖动学生:记录拖动的学生 id */
function onDragStartStudent(event, stu) {
  event.dataTransfer.setData('text/plain', String(stu.id))
  event.dataTransfer.effectAllowed = 'move'
  draggingStudentId.value = stu.id
}

/** 结束拖动学生 */
function onDragEndStudent() {
  draggingStudentId.value = null
  dragOverBedId.value = null
}

/** 拖拽悬停在可投放床位上方:允许放置 */
function onDragOverBed(event, bed) {
  if (!isBedDroppable(bed)) return
  event.preventDefault()
  dragOverBedId.value = bed.bedId
}

/** 拖拽离开床位 */
function onDragLeaveBed(bed) {
  if (dragOverBedId.value === bed.bedId) dragOverBedId.value = null
}

/** 学生放置到床位:建立(或移动)绑定关系 */
function onDropOnBed(event, bed) {
  const studentId = Number(event.dataTransfer?.getData('text/plain'))
  if (!studentId) return
  const stu = checkInStudents.value.find((s) => s.id === studentId)
  if (!stu) return

  if (bed.bedStatus !== '空闲') {
    ElMessage.warning('该床位不可用')
    return
  }
  const existing = bindingOfBed(bed)
  if (existing) {
    if (existing.studentId === studentId) return
    ElMessage.warning('该床位已分配学生')
    return
  }

  // 同一学生重复拖动到其他床位时,视为移动:先移除旧绑定
  const oldIdx = bindings.value.findIndex((b) => b.studentId === studentId)
  if (oldIdx > -1) bindings.value.splice(oldIdx, 1)

  bindings.value.push({
    studentId: stu.id,
    studentName: stu.studentName,
    bedId: bed.bedId,
    bedName: bed.bedName,
    roomId: bed.roomId,
    roomName: bed.roomName
  })
  dragOverBedId.value = null
}

/** 移除某学生的绑定关系 */
function removeBinding(studentId) {
  const idx = bindings.value.findIndex((b) => b.studentId === studentId)
  if (idx > -1) bindings.value.splice(idx, 1)
}

/** 保存入住:调用批量入住床位接口 */
async function handleSaveCheckIn() {
  if (saving.value) return
  const range = checkInForm.dateRange
  if (!range || range.length !== 2) {
    ElMessage.warning('请选择入住时间区间')
    return
  }
  if (!bindings.value.length) {
    ElMessage.warning('请先将学生拖动到床位完成绑定')
    return
  }
  saving.value = true
  try {
    await batchCheckIn({
      itemList: bindings.value.map((b) => ({ studentId: b.studentId, bedId: b.bedId })),
      // 批量入住接口要求日期带时分秒,时间传 00:00:00
      checkInDate: `${range[0]} 00:00:00`,
      estimateCheckOutDate: `${range[1]} 00:00:00`
    })
    ElMessage.success('入住设置成功')
    checkInDialogVisible.value = false
    // 清除勾选并刷新列表
    clearSelection()
    fetchList()
  } catch (e) {
    // 错误提示已由响应拦截器统一处理
  } finally {
    saving.value = false
  }
}

function resetCheckInDialog() {
  checkInForm.dateRange = []
  roomGroups.value = []
  bedErrorMsg.value = ''
  bedQueried.value = false
  checkInStudents.value = []
  bindings.value = []
  draggingStudentId.value = null
  dragOverBedId.value = null
}

/** 打开入住记录查看:需恰好勾选一名学生 */
async function handleViewRecord() {
  if (selectedStudents.value.length !== 1) {
    ElMessage.warning('请勾选一名学生后查看入住记录')
    return
  }
  recordStudent.value = selectedStudents.value[0]
  recordDialogVisible.value = true
  await loadStudentRecord()
}

/** 加载选中学生的入住记录 */
async function loadStudentRecord() {
  if (!recordStudent.value) return
  recordLoading.value = true
  recordErrorMsg.value = ''
  recordList.value = []
  try {
    const res = await getStudentRecord(recordStudent.value.id)
    recordList.value = Array.isArray(res?.data) ? res.data : []
  } catch (e) {
    recordErrorMsg.value = e?.message || '查询失败,请检查后端服务是否启动'
    recordList.value = []
  } finally {
    recordLoading.value = false
  }
}

/** 入住记录状态 -> 标签颜色 */
function recordStatusTagType(status) {
  const map = {
    正在入住: 'success',
    待入住: 'warning',
    已结束: 'info'
  }
  return map[status] || 'info'
}

/** 删除入住记录:确认后调用删除接口并刷新 */
async function handleDeleteRecord(row) {
  const id = row.id
  if (id == null) {
    ElMessage.warning('无法获取该入住记录的 id,暂不支持删除')
    return
  }
  try {
    await ElMessageBox.confirm('确定要删除该条入住记录吗?此操作不可恢复。', '删除确认', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      confirmButtonClass: 'el-button--danger'
    })
  } catch {
    return
  }

  deletingRecordId.value = id
  try {
    await deleteRelRecord(id)
    ElMessage.success('删除成功')
    await loadStudentRecord()
    // 删除入住记录会影响学生的入住状态,刷新父页面列表并保留当前勾选
    fetchList({ keepSelection: true })
  } catch (e) {
    // 错误提示已由响应拦截器统一处理
  } finally {
    deletingRecordId.value = null
  }
}

function resetRecordDialog() {
  recordStudent.value = null
  recordList.value = []
  recordErrorMsg.value = ''
  deletingRecordId.value = null
}

/** 当天日期字符串 YYYY-MM-DD */
function todayString() {
  const d = new Date()
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
}

/** 打开退房对话框:带入勾选学生,默认退房日期为今天 */
function handleCheckOut() {
  if (!selectedStudents.value.length) {
    ElMessage.warning('请先勾选学生')
    return
  }
  checkOutStudents.value = selectedStudents.value.map((s) => ({
    id: Number(s.id),
    studentName: s.studentName
  }))
  checkOutForm.checkOutDate = todayString()
  checkOutDialogVisible.value = true
}

/** 提交退房:调用批量退房接口 */
async function handleCheckOutSubmit() {
  if (checkingOut.value) return
  if (!checkOutStudents.value.length) return
  checkingOut.value = true
  try {
    await batchCheckOut({
      studentIdList: checkOutStudents.value.map((s) => s.id),
      checkOutDate: checkOutForm.checkOutDate || undefined
    })
    ElMessage.success('退房成功')
    checkOutDialogVisible.value = false
    clearSelection()
    fetchList()
  } catch (e) {
    // 错误提示已由响应拦截器统一处理
  } finally {
    checkingOut.value = false
  }
}

function resetCheckOutDialog() {
  checkOutStudents.value = []
  checkOutForm.checkOutDate = ''
}

/** 打开续住对话框:查询当前入住信息 */
async function handleRenew() {
  if (selectedStudents.value.length !== 1) {
    ElMessage.warning('请勾选一名学生后办理续住')
    return
  }
  renewStudent.value = {
    id: Number(selectedStudents.value[0].id),
    studentName: selectedStudents.value[0].studentName
  }
  renewInfo.value = null
  renewErrorMsg.value = ''
  renewForm.newDate = ''
  renewDialogVisible.value = true
  await loadRenewInfo()
}

/** 查询续住学生的当前入住信息 */
async function loadRenewInfo() {
  if (!renewStudent.value) return
  renewLoading.value = true
  renewErrorMsg.value = ''
  renewInfo.value = null
  try {
    const res = await getCurrentCheckIn(renewStudent.value.id)
    const data = res?.data ?? null
    // 空对象视为无入住信息
    renewInfo.value = data && typeof data === 'object' && Object.keys(data).length ? data : null
  } catch (e) {
    renewErrorMsg.value = e?.message || '查询失败,请检查后端服务是否启动'
    renewInfo.value = null
  } finally {
    renewLoading.value = false
  }
}

/** 提交续住:调用续住接口 */
async function handleRenewSubmit() {
  if (renewing.value) return
  if (!renewStudent.value || !renewInfo.value) return
  const newDate = renewForm.newDate
  if (!newDate) {
    ElMessage.warning('请选择续住日期')
    return
  }
  // 床位学生关联主键 id(key 名称为 id)
  const bedRelId = renewInfo.value.id
  if (bedRelId == null) {
    ElMessage.warning('缺少床位关联记录 id,无法续住')
    return
  }
  renewing.value = true
  try {
    await renew({
      studentId: renewStudent.value.id,
      bedRelId,
      bedId: renewInfo.value.bedId,
      oldEstimateLeaveDate: renewInfo.value.estimateLeaveTime,
      newEstimateLeaveDate: `${newDate} 00:00:00`
    })
    ElMessage.success('续住成功')
    renewDialogVisible.value = false
    clearSelection()
    fetchList()
  } catch (e) {
    // 错误提示已由响应拦截器统一处理
  } finally {
    renewing.value = false
  }
}

function resetRenewDialog() {
  renewStudent.value = null
  renewInfo.value = null
  renewErrorMsg.value = ''
  renewForm.newDate = ''
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

.student-name-wrap {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  max-width: 100%;
}

.student-name-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.warn-icon {
  font-size: 14px;
  flex-shrink: 0;
}

.warn-icon.is-today {
  color: #e6a23c;
}

.warn-icon.is-overdue {
  color: #f56c6c;
}

.check-in-form {
  margin-bottom: 4px;
}

/* 待分配学生池 */
.student-pool {
  margin-bottom: 12px;
  padding: 10px 12px;
  border: 1px dashed #dcdfe6;
  border-radius: 8px;
  background: #fafafa;
}

.pool-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}

.pool-title {
  font-weight: 600;
  font-size: 14px;
  color: #303133;
}

.pool-count {
  font-size: 12px;
  color: #909399;
}

.student-chip-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  min-height: 32px;
}

.student-chip {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 5px 10px;
  background: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 16px;
  cursor: grab;
  user-select: none;
  transition: all 0.15s;
}

.student-chip:hover {
  border-color: #409eff;
  box-shadow: 0 2px 6px rgba(64, 158, 255, 0.2);
}

.student-chip.is-dragging {
  opacity: 0.5;
  border-style: dashed;
}

.chip-drag-icon {
  color: #c0c4cc;
  font-size: 14px;
}

.chip-name {
  font-size: 13px;
  color: #303133;
}

.pool-empty-tip {
  font-size: 13px;
  color: #67c23a;
}

.bed-result-area {
  min-height: 120px;
}

.room-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.room-card {
  border-radius: 8px;
}

.room-header {
  display: flex;
  align-items: center;
  gap: 8px;
}

.room-name {
  font-weight: 600;
  font-size: 14px;
}

.room-desc {
  color: #909399;
  font-size: 12px;
}

.bed-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 10px;
}

.bed-item {
  padding: 10px 12px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  background: #fafbfc;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.bed-item.is-idle {
  background: #f0f9eb;
  border-color: #e1f3d8;
}

.bed-name {
  font-weight: 600;
  font-size: 14px;
  color: #303133;
}

.bed-occupant {
  font-size: 12px;
  color: #606266;
  word-break: break-all;
}

.bed-occupant.is-empty {
  color: #c0c4cc;
}

.bed-occupant.is-pending {
  color: #409eff;
  font-weight: 600;
}

.bed-item.is-assigned {
  background: #ecf5ff;
  border-color: #b3d8ff;
}

.bed-item.is-dragover {
  border-color: #409eff;
  background: #ecf5ff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

/* 已绑定汇总 */
.binding-summary {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  margin-top: 14px;
  padding: 10px 12px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  background: #f5f7fa;
}

.binding-title {
  flex-shrink: 0;
  font-size: 13px;
  color: #606266;
  line-height: 24px;
}

.binding-chip-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.record-body {
  min-height: 160px;
}

.check-out-students {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.check-out-student-tag {
  margin-right: 0;
}

.renew-body {
  min-height: 120px;
}

.renew-desc {
  margin-bottom: 16px;
}

.renew-form {
  margin-top: 4px;
}
</style>
