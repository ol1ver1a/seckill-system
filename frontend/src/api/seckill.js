import request from './request'

export function listActivities(params) {
  return request.get('/seckill/activity/list', { params })
}

export function listActiveActivities() {
  return request.get('/seckill/activity/active')
}

export function getActivityDetail(id) {
  return request.get(`/seckill/activity/${id}`)
}

export function createActivity(data) {
  return request.post('/seckill/activity', data)
}

export function updateActivity(data) {
  return request.put('/seckill/activity', data)
}

export function updateActivityStatus(id, status) {
  return request.put(`/seckill/activity/${id}/status`, null, { params: { status } })
}

export function auditActivity(id, status) {
  return request.put(`/seckill/activity/${id}/audit`, null, { params: { status } })
}

export function executeSeckill(activityId) {
  return request.post('/seckill/order/execute', null, { params: { activityId } })
}

export function listOrders(params) {
  return request.get('/seckill/order/list', { params })
}

export function myOrders(params) {
  return request.get('/seckill/order/my', { params })
}

export function getOrderDetail(id) {
  return request.get(`/seckill/order/${id}`)
}

export function payOrder(id) {
  return request.put(`/seckill/order/${id}/pay`)
}

export function cancelOrder(id) {
  return request.put(`/seckill/order/${id}/cancel`)
}

export function shipOrder(id) {
  return request.put(`/seckill/order/${id}/ship`)
}

export function completeOrder(id) {
  return request.put(`/seckill/order/${id}/complete`)
}
