import axios from 'axios'

const api = axios.create({
  baseURL: `${import.meta.env.VITE_API_BASE_URL || ''}/api/users`,
  timeout: 10000,
  withCredentials: true,
})

export const login = (data) => api.post('/login', data).then((r) => r.data)

export const register = (data) => api.post('/register', data).then((r) => r.data)

export const logout = () =>
  axios.post(`${import.meta.env.VITE_API_BASE_URL || ''}/api/logout`, null, {
    timeout: 10000,
    withCredentials: true,
  })
