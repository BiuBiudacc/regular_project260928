import request from './request'

/**
 * 学生信息分页查询(支持按学生姓名、批次、入住状态检索)
 * 对应 temp.md 中的「学生信息接口,支持分页」
 * POST /api/student/page
 * 请求头携带 token(由 request 拦截器统一处理)
 * @param {object} data { pageNum, pageSize, studentName, batchId, checkInStatus }
 * @returns {Promise} { code, msg, data: { current, size, total, pages, records } }
 */
export function studentPage(data) {
  return request.post('/student/page', data)
}

/**
 * 创建学生
 * 对应 temp.md 中的「学生信息创建接口」
 * POST /api/student/create
 * @param {object} data { studentName, idCard, studentGender, studentAge, studentPhone, emergencyContactName, emergencyContactPhone, batchId, classId }
 * @returns {Promise} { code, msg, data }
 */
export function createStudent(data) {
  return request.post('/student/create', data)
}

/**
 * 修改学生信息(基于学生 id,id 拼接到地址栏 query 参数)
 * 对应 temp.md 中的「学生修改信息接口」
 * POST /api/student/update?id={id}
 * @param {number|string} id 学生 id
 * @param {object} data { studentName, idCard, studentGender, studentAge, studentPhone, emergencyContactName, emergencyContactPhone, batchId, classId }
 * @returns {Promise} { code, msg, data }
 */
export function updateStudent(id, data) {
  return request.post(`/student/update?id=${id}`, data)
}

/**
 * 删除学生
 * POST /api/student/delete/{id}
 * @param {number|string} id 学生 id
 * @returns {Promise} { code, msg, data }
 */
export function deleteStudent(id) {
  return request.post(`/student/delete/${id}`)
}
