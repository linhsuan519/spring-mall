import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

function parseJwt(token) {
  try {
    const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )
    return JSON.parse(jsonPayload)
  } catch {
    return null
  }
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || null)

  const payload = computed(() => (token.value ? parseJwt(token.value) : null))
  const isLoggedIn = computed(() => !!token.value)
  const email = computed(() => payload.value?.email || '')
  const role = computed(() => payload.value?.role || '')
  const isAdmin = computed(() => role.value === 'ROLE_ADMIN')
  const picture = computed(() => payload.value?.picture || null)
  const displayName = computed(() => payload.value?.displayName || null)

  function setToken(t) {
    token.value = t
    localStorage.setItem('token', t)
  }

  function logout() {
    token.value = null
    localStorage.removeItem('token')
  }

  return { token, isLoggedIn, email, role, isAdmin, picture, displayName, setToken, logout }
})
