import request from './request'

/**
 * 教室分页查询(支持按教室名称、教室状态条件检索)
 * 对应 temp.md 中的「教室查询接口-分页」
 * POST /api/classRoom/page
 * 请求头携带 token(由 request 拦截器统一处理)
 * @param {object} data { pageNum, pageSize, roomName, roomStatus }
 * @returns {Promise} { code, msg, data: { current, size, total, pages, records: [{ roomId, roomName, roomAddress, modelName, roomStatus }] } }
 */
export function classRoomPage(data) {
  return request.post('/classRoom/page', data)
}

/**
 * 教室创建
 * 对应 temp.md 中的「教室创建接口」
 * POST /api/classRoom/add
 * @param {object} data { roomName, roomAddress, roomTypeId, roomStatus }
 * @returns {Promise} { code, msg, data }
 */
export function addClassRoom(data) {
  return request.post('/classRoom/add', data)
}

/**
 * 教室修改
 * 对应 temp.md 中的「教室修改」
 * POST /api/classRoom/update
 * @param {object} data { id, roomName, roomAddress, roomTypeId, roomStatus }
 * @returns {Promise} { code, msg, data }
 */
export function updateClassRoom(data) {
  return request.post('/classRoom/update', data)
}

/**
 * 教室删除
 * 对应 temp.md 中的「教室删除」
 * POST /api/classRoom/delete?id=xxx
 * @param {number|string} id 教室 id
 * @returns {Promise} { code, msg, data }
 */
export function deleteClassRoom(id) {
  return request.post('/classRoom/delete', null, { params: { id } })
}
