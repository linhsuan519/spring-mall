import { createApi } from './client'

const api = createApi(`${import.meta.env.VITE_API_BASE_URL || ''}/api/products`)

export const getProducts = (params = {}) => api.get('', { params }).then((r) => r.data)

export const getProduct = (id) => api.get(`/${id}`).then((r) => r.data)

export const createProduct = (data) => api.post('', data).then((r) => r.data)

export const updateProduct = (id, data) => api.put(`/${id}`, data).then((r) => r.data)

export const deleteProduct = (id) => api.delete(`/${id}`)
