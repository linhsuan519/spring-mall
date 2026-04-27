import axios from 'axios'

const api = axios.create({
  baseURL: '/users',
  timeout: 10000,
})

export const register = (email, password) =>
  api.post('/register', { email, password }).then((r) => r.data)

export const login = (email, password) =>
  api.post('/login', { email, password }).then((r) => r.data)
