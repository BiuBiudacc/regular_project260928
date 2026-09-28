import request from './request'

/**
 * 批量入住床位(批量为学生与床位建立绑定关系,完成入住)
 * 对应 temp.md 中的「批量入住床位接口」
 * POST /api/bedStudentRel/batchCheckIn
 * 请求头携带 token(由 request 拦截器统一处理)
 * @param {object} data {
 *   itemList: [{ studentId, bedId }],
 *   checkInDate,          // 入住日期
 *   estimateCheckOutDate  // 预估退宿日期
 * }
 * @returns {Promise} { code, msg, data }
 */
export function batchCheckIn(data) {
  return request.post('/bedStudentRel/batchCheckIn', data)
}

/**
 * 入住记录查询(根据学生 id 查询入住记录)
 * 对应 temp.md 中的「入住记录查询接口」
 * GET /api/bedStudentRel/studentRecord
 * 请求头携带 token(由 request 拦截器统一处理)
 * @param {number|string} studentId 学生 id
 * @returns {Promise} { code, msg, data: [ { roomId, roomName, bedId, bedName, checkInDate, estimateLeaveTime, realLeaveTime, status } ] }
 */
export function getStudentRecord(studentId) {
  return request.get('/bedStudentRel/studentRecord', { params: { studentId } })
}

/**
 * 删除入住记录
 * 对应 temp.md 中的「入住记录删除接口」
 * POST /api/bedStudentRel/deleteRelRecord
 * @param {number|string} id 入住记录 id
 * @returns {Promise} { code, msg, data }
 */
export function deleteRelRecord(id) {
  return request.post('/bedStudentRel/deleteRelRecord', { id })
}

/**
 * 批量退房(接收学生 id 集合以及退房日期)
 * 对应 temp.md 中的「退房接口」
 * POST /api/bedStudentRel/batchCheckOut
 * @param {object} data { studentIdList: number[], checkOutDate?: string }
 * @returns {Promise} { code, msg, data }
 */
export function batchCheckOut(data) {
  return request.post('/bedStudentRel/batchCheckOut', data)
}

/**
 * 学生当前入住信息查询
 * 对应 temp.md 中的「学生当前入住信息查询接口」
 * GET /api/bedStudentRel/getCurrentCheckIn
 * @param {number|string} studentId 学生 id
 * @returns {Promise} { code, msg, data: { roomId, roomName, bedId, bedName, checkInDate, estimateLeaveTime } }
 */
export function getCurrentCheckIn(studentId) {
  return request.get('/bedStudentRel/getCurrentCheckIn', { params: { studentId } })
}

/**
 * 学生续住(同床位延长入住时间)
 * 对应 temp.md 中的「学生续住接口」
 * POST /api/bedStudentRel/renew
 * @param {object} data { studentId, bedRelId, bedId, oldEstimateLeaveDate, newEstimateLeaveDate }
 * @returns {Promise} { code, msg, data }
 */
export function renew(data) {
  return request.post('/bedStudentRel/renew', data)
}
