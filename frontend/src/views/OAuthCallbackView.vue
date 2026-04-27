<script setup>
import { onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

onMounted(() => {
  const token = route.query.token
  if (token) {
    authStore.setToken(token)
    router.replace('/')
  } else {
    router.replace('/login')
  }
})
</script>

<template>
  <div class="callback-page">
    <div class="spinner"></div>
    <p>登入中，請稍候...</p>
  </div>
</template>

<style scoped>
.callback-page {
  min-height: calc(100vh - var(--navbar-h));
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
  color: var(--text-dim);
  font-size: 0.9rem;
}
</style>
