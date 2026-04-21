import request from './request'

export function listGoods(params) {
  return request.get('/goods/list', { params })
}

export function listOnSale() {
  return request.get('/goods/onsale')
}

export function getGoodsDetail(id) {
  return request.get(`/goods/${id}`)
}

export function addGoods(data) {
  return request.post('/goods', data)
}

export function updateGoods(data) {
  return request.put('/goods', data)
}

export function updateGoodsStatus(id, status) {
  return request.put(`/goods/${id}/status`, null, { params: { status } })
}

export function deleteGoods(id) {
  return request.delete(`/goods/${id}`)
}
