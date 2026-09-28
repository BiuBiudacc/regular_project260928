import request from './request'

/**
 * 批次分页查询
 * GET /api/batch/page?pageNum=1&pageSize=5
 * 请求头携带 token(由 request 拦截器统一处理)
 * @param {object} params { pageNum, pageSize }
 * @returns {Promise} { code, msg, data: { current, size, total, pages, records } }
 */
export function getBatchPage(params) {
  return request.get('/batch/page', { params })
}

/**
 * 批次高级查询(组合条件分页)
 * POST /api/batch/advancedPage
 * 后端根据传入的 query 组合条件进行查询,支持分页
 * @param {object} data { pageNum, pageSize, query: { batchId, batchInfo, queryStartTime } }
 * @returns {Promise} { code, msg, data: { current, size, total, pages, records } }
 */
export function advancedPage(data) {
  return request.post('/batch/advancedPage', data)
}

/**
 * 创建批次
 * POST /api/batch/create
 * @param {object} data { batchName, batchDesc, estimateStudentNum, estimateTeacherNum, batchStartTime, batchEndTime }
 * @returns {Promise} { code, msg, data } data 为新创建的批次 ID
 */
export function createBatch(data) {
  return request.post('/batch/create', data)
}

/**
 * 修改批次(基于批次 id)
 * POST /api/batch/update
 * @param {object} data { batchId, batchName, batchDesc, estimateStudentNum, estimateTeacherNum, chargeTeacher, chargePhone, batchStartTime, batchEndTime }
 * @returns {Promise} { code, msg, data }
 */
export function updateBatch(data) {
  return request.post('/batch/update', data)
}

/**
 * 删除批次(仅批次下没有关联数据时才会删除)
 * POST /api/batch/delete/{batchId}
 * @param {number|string} batchId 批次 id
 * @returns {Promise} { code, msg, data }
 */
export function deleteBatch(batchId) {
  return request.post(`/batch/delete/${batchId}`)
}

/**
 * 批次简易查询(仅返回必要信息)
 * 对应 需求文档.md 中的「批次简易查询接口」
 * GET /api/batch/listSimple
 * @returns {Promise} { code, msg, data: [{ batchId, batchName, batchStartTime, batchEndTime }] }
 */
export function getBatchSimpleList() {
  return request.get('/batch/listSimple')
}
