import axios from 'axios'

const api = axios.create({
  baseURL: `${import.meta.env.VITE_API_BASE_URL || ''}/api/activities`,
  timeout: 10000,
  withCredentials: true,
})

export const getActivities = (params = {}) => api.get('', { params }).then((r) => r.data)
export const getActivityById = (id) => api.get(`/${id}`).then((r) => r.data)
export const createActivity = (data) => api.post('', data).then((r) => r.data)
export const cancelActivity = (id) => api.delete(`/${id}`)
export const joinActivity = (id) => api.post(`/${id}/join`)
export const leaveActivity = (id) => api.delete(`/${id}/leave`)
export const getParticipants = (id) => api.get(`/${id}/participants`).then((r) => r.data)
export const getMyHosted = () => api.get('/my/hosted').then((r) => r.data)
export const getMyJoined = () => api.get('/my/joined').then((r) => r.data)
