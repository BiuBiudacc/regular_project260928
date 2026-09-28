import request from './request'

/**
 * 可用床位查询(返回房间全部床位:空闲 + 占用)
 * 对应 temp.md 中的「可用床位查询接口」
 * POST /api/bed/available
 * 请求头携带 token(由 request 拦截器统一处理)
 * @param {object} data { checkInDate, checkOutDate, studentGender }
 * @returns {Promise} { code, msg, data: [{ roomId, roomName, roomDesc, typeName, bedId, bedName, bedStatus, studentName }] }
 */
export function getAvailableBed(data) {
  return request.post('/bed/available', data)
}
