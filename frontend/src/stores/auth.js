import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { login as loginApi, register as registerApi } from '../api/auth'
import { AUTH_STORAGE_KEY, getStoredAuthUser, isAuthExpired } from '../api/client'

const MAX_TIMEOUT_DELAY = 2_147_483_647

export const useAuthStore = defineStore('auth', () => {
  const user = ref(getStoredAuthUser())
  const loading = ref(false)
  const error = ref('')
  let autoLogoutTimer = null

  const isAuthenticated = computed(() => Boolean(user.value?.userId) && !isAuthExpired(user.value))
  const userId = computed(() => user.value?.userId || null)

  function clearAutoLogoutTimer() {
    if (autoLogoutTimer) {
      clearTimeout(autoLogoutTimer)
      autoLogoutTimer = null
    }
  }

  function scheduleAutoLogout() {
    clearAutoLogoutTimer()

    if (!user.value?.expiresAt) return

    const delay = new Date(user.value.expiresAt).getTime() - Date.now()

    if (!Number.isFinite(delay) || delay <= 0) {
      setUser(null)
      return
    }

    autoLogoutTimer = setTimeout(
      () => {
        setUser(null)
        error.value = 'Login expired. Please sign in again.'
      },
      Math.min(delay, MAX_TIMEOUT_DELAY)
    )
  }

  function setUser(nextUser) {
    const authUser = nextUser && !isAuthExpired(nextUser) ? nextUser : null

    user.value = authUser

    if (authUser) {
      localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(authUser))
    } else {
      localStorage.removeItem(AUTH_STORAGE_KEY)
    }

    scheduleAutoLogout()
  }

  async function login(credentials) {
    loading.value = true
    error.value = ''
    try {
      const loggedInUser = await loginApi(credentials)
      setUser(loggedInUser)
      return loggedInUser
    } catch (e) {
      error.value = e.response?.status === 400 ? 'Email or password is incorrect.' : 'Login failed.'
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
        e.response?.status === 400 ? 'Email is invalid or already in use.' : 'Register failed.'
      throw e
    } finally {
      loading.value = false
    }
  }

  function logout() {
    setUser(null)
    error.value = ''
  }

  window.addEventListener('auth-expired', () => {
    setUser(null)
    error.value = 'Login expired. Please sign in again.'
  })

  scheduleAutoLogout()

  return { user, userId, loading, error, isAuthenticated, login, register, logout }
})
