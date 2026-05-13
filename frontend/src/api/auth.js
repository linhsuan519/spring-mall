import { createApi } from './client'

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || ''

const userApi = createApi(`${apiBaseUrl}/api/users`)

export const login = (data) => userApi.post('/login', data).then((r) => r.data)

export const register = (data) => userApi.post('/register', data).then((r) => r.data)
