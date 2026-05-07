import axios from 'axios'

const api = axios.create({
  baseURL: `${import.meta.env.VITE_API_BASE_URL || ''}/api`,
  timeout: 10000,
  withCredentials: true,
})

export const createOrder = (userId, data) =>
  api.post(`/users/${userId}/orders`, data).then((r) => r.data)

export const getOrders = (userId, params = {}) =>
  api.get(`/users/${userId}/orders`, { params }).then((r) => r.data)
