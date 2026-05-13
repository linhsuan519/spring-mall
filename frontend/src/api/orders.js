import { createApi } from './client'

const api = createApi(`${import.meta.env.VITE_API_BASE_URL || ''}/api`)

export const createOrder = (userId, data) =>
  api.post(`/users/${userId}/orders`, data).then((r) => r.data)

export const getOrders = (userId, params = {}) =>
  api.get(`/users/${userId}/orders`, { params }).then((r) => r.data)
