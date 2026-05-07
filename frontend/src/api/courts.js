import axios from 'axios'

const api = axios.create({
  baseURL: `${import.meta.env.VITE_API_BASE_URL || ''}/api/courts`,
  timeout: 10000,
  withCredentials: true,
})

export const getCourts = (params = {}) => api.get('', { params }).then((r) => r.data)
export const getCourtById = (id) => api.get(`/${id}`).then((r) => r.data)
export const createCourt = (data) => api.post('', data).then((r) => r.data)
export const updateCourt = (id, data) => api.put(`/${id}`, data).then((r) => r.data)
export const deleteCourt = (id) => api.delete(`/${id}`)
