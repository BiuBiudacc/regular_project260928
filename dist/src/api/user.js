import request from './request'

/**
 * 登录接口
 * POST /api/user/login
 * @param {object} data { username, password }
 * @returns {Promise} { code, msg, data: { token } }
 */
export function login(data) {
  return request.post('/user/login', data)
}

/**
 * 用户列表接口(房间管理页面用于验证 token 携带)
 * GET /api/user/user/list
 * @returns {Promise} 后端返回信息
 */
export function getUserList() {
  return request.get('/user/user/list')
}
