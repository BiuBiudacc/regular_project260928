import request from './request'

/**
 * 班级分页查询(支持通用查询 + 条件检索)
 * 对应 需求文档.md 中的「班级分页查询接口」
 * POST /api/class/pageQuery
 * 请求头携带 token(由 request 拦截器统一处理)
 * @param {object} data { pageNum, pageSize, query: { className, classStatus, classRoomId } }
 * @returns {Promise} { code, msg, data: { current, size, total, pages, records } }
 */
export function classPageQuery(data) {
  return request.post('/class/pageQuery', data)
}

/**
 * 班级创建
 * 对应 需求文档.md 中的「班级创建接口」
 * POST /api/class/create
 * @param {object} data { className, classDesc, manageTeacherName, teacherPhone, classStatus, classRoomId }
 * @returns {Promise} { code, msg, data }
 */
export function createClass(data) {
  return request.post('/class/create', data)
}

/**
 * 班级信息修改
 * 对应 需求文档.md 中的「班级信息修改接口」
 * POST /api/class/update
 * @param {object} data { id, className, classDesc, manageTeacherName, teacherPhone, classStatus, classRoomId }
 * @returns {Promise} { code, msg, data }
 */
export function updateClass(data) {
  return request.post('/class/update', data)
}

/**
 * 班级学生列表
 * 对应 需求文档.md 中的「班级学生列表接口」
 * GET /api/class/listStudent
 * @param {number|string} classId 班级 id
 * @returns {Promise} { code, msg, data: [ StudentClassVO ] }
 */
export function getClassStudentList(classId) {
  return request.get('/class/listStudent', { params: { classId } })
}

/**
 * 教室信息查询(服务于表单下拉组件)
 * 对应 需求文档.md 中的「教室信息查询接口-表单」
 * GET /api/classRoom/selectList
 * @returns {Promise} { code, msg, data: [{ id, roomName }] }
 */
export function getClassRoomSelectList() {
  return request.get('/classRoom/selectList')
}

/**
 * 班级删除(仅删除没有学生的班级)
 * 对应 需求文档.md 中的「班级删除接口」
 * POST /api/class/remove
 * @param {number|string} classId 班级 id
 * @returns {Promise} { code, msg, data }
 */
export function deleteClass(classId) {
  return request.post('/class/remove', null, { params: { classId } })
}

/**
 * 指定批次学生学习查询(用于班级学生调整-加入学生)
 * 对应 需求文档.md 中的「指定批次学生学习查询接口」
 * GET /api/class/adjustStudentList
 * @param {number|string} batchId 批次 id
 * @returns {Promise} { code, msg, data: [{ studentId, studentName, studentGender, hasClass, className }] }
 */
export function getAdjustStudentList(batchId) {
  return request.get('/class/adjustStudentList', { params: { batchId } })
}

/**
 * 批量设置班级学生(批量加入学生到班级)
 * 对应 temp.md 中的「批量设置班级学生接口」
 * POST /api/class/batchAddStudent
 * @param {object} data { classId, studentIdList: number[] }
 * @returns {Promise} { code, msg, data }
 */
export function batchAddStudent(data) {
  return request.post('/class/batchAddStudent', data)
}

/**
 * 批量退出班级(批量将学生移出班级)
 * 对应 temp.md 中的「批量退出班级接口」
 * POST /api/class/batchQuitStudent
 * @param {object} data { studentIdList: number[] }
 * @returns {Promise} { code, msg, data }
 */
export function batchQuitStudent(data) {
  return request.post('/class/batchQuitStudent', data)
}
