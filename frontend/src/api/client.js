import axios from 'axios'

export const AUTH_STORAGE_KEY = 'auth_user'

export function isAuthExpired(authUser) {
  if (!authUser?.expiresAt) return false

  const expiresAt = new Date(authUser.expiresAt).getTime()
  return Number.isFinite(expiresAt) && expiresAt <= Date.now()
}

export function getStoredAuthUser() {
  try {
    const authUser = JSON.parse(localStorage.getItem(AUTH_STORAGE_KEY) || 'null')

    if (isAuthExpired(authUser)) {
      localStorage.removeItem(AUTH_STORAGE_KEY)
      return null
    }

    return authUser
  } catch {
    localStorage.removeItem(AUTH_STORAGE_KEY)
    return null
  }
}

export function getAuthToken() {
  return getStoredAuthUser()?.token || ''
}

export function createApi(baseURL) {
  const api = axios.create({
    baseURL,
    timeout: 10000,
  })

  api.interceptors.request.use((config) => {
    const token = getAuthToken()

    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }

    return config
  })

  api.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 401) {
        localStorage.removeItem(AUTH_STORAGE_KEY)
        window.dispatchEvent(new CustomEvent('auth-expired'))
      }

      return Promise.reject(error)
    }
  )

  return api
}
