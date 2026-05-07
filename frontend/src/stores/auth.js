import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { login as loginApi, logout as logoutApi, register as registerApi } from '../api/auth'

const STORAGE_KEY = 'auth_user'

function readStoredUser() {
  try {
    return JSON.parse(localStorage.getItem(STORAGE_KEY) || 'null')
  } catch {
    localStorage.removeItem(STORAGE_KEY)
    return null
  }
}

export const useAuthStore = defineStore('auth', () => {
  const user = ref(readStoredUser())
  const loading = ref(false)
  const error = ref('')

  const isAuthenticated = computed(() => Boolean(user.value?.userId))
  const userId = computed(() => user.value?.userId || null)

  function setUser(nextUser) {
    user.value = nextUser
    if (nextUser) {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(nextUser))
    } else {
      localStorage.removeItem(STORAGE_KEY)
    }
  }

  async function login(credentials) {
    loading.value = true
    error.value = ''
    try {
      const loggedInUser = await loginApi(credentials)
      setUser(loggedInUser)
      return loggedInUser
    } catch (e) {
      error.value = e.response?.status === 400 ? 'Email 或密碼不正確。' : '登入失敗，請稍後再試。'
      throw e
    } finally {
      loading.value = false
    }
  }

  async function register(credentials) {
    loading.value = true
    error.value = ''
    try {
      await registerApi(credentials)
      const loggedInUser = await loginApi(credentials)
      setUser(loggedInUser)
      return loggedInUser
    } catch (e) {
      error.value =
        e.response?.status === 400
          ? '註冊資料無法使用，請確認 Email 或密碼。'
          : '註冊失敗，請稍後再試。'
      throw e
    } finally {
      loading.value = false
    }
  }

  async function logout() {
    try {
      await logoutApi()
    } catch {
      // The local session still needs to be cleared if the server session is already gone.
    }
    setUser(null)
    error.value = ''
  }

  return { user, userId, loading, error, isAuthenticated, login, register, logout }
})
