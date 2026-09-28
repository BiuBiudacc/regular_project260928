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
