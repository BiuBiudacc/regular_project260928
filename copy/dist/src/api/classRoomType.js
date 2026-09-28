import request from './request'

/**
 * 教室型号分页查询(支持按型号名称模糊检索)
 * 对应 temp.md 中的「教室型号列表,支持分页」
 * POST /api/classRoomType/page
 * @param {object} data { pageNum, pageSize, typeName }
 * @returns {Promise} { code, msg, data: { current, size, total, pages, records: [{ id, typeName }] } }
 */
export function classRoomTypePage(data) {
  return request.post('/classRoomType/page', data)
}

/**
 * 教室型号新增
 * 对应 temp.md 中的「教室型号新增接口」
 * POST /api/classRoomType/add
 * @param {object} data { typeName }
 * @returns {Promise} { code, msg, data }
 */
export function addClassRoomType(data) {
  return request.post('/classRoomType/add', data)
}

/**
 * 教室型号修改
 * 对应 temp.md 中的「教室型号修改接口」
 * POST /api/classRoomType/update
 * @param {object} data { id, typeName }
 * @returns {Promise} { code, msg, data }
 */
export function updateClassRoomType(data) {
  return request.post('/classRoomType/update', data)
}

/**
 * 教室型号删除
 * 对应 temp.md 中的「教室型号删除接口」
 * POST /api/classRoomType/remove?id=xxx
 * @param {number|string} id 型号 id
 * @returns {Promise} { code, msg, data }
 */
export function removeClassRoomType(id) {
  return request.post('/classRoomType/remove', null, { params: { id } })
}

/**
 * 教室型号列表(不分页,服务于教室表单型号下拉框)
 * 对应 temp.md 中的「教室型号列表」
 * GET /api/classRoomType/listAll
 * @returns {Promise} { code, msg, data: [{ id, typeName }] }
 */
export function getClassRoomTypeList() {
  return request.get('/classRoomType/listAll')
}
