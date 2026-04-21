import request from './request'

export function listNotices(params) {
  return request.get('/notice/list', { params })
}

export function listPublishedNotices() {
  return request.get('/notice/published')
}

export function getNoticeDetail(id) {
  return request.get(`/notice/${id}`)
}

export function addNotice(data) {
  return request.post('/notice', data)
}

export function updateNotice(data) {
  return request.put('/notice', data)
}

export function deleteNotice(id) {
  return request.delete(`/notice/${id}`)
}
