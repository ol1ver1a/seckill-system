import request from './request'

export function listUsers(params) {
  return request.get('/admin/users', { params })
}

export function updateUserStatus(id, status) {
  return request.put(`/admin/user/${id}/status`, null, { params: { status } })
}

export function resetPassword(id) {
  return request.put(`/admin/user/${id}/reset-password`)
}
