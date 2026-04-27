<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/auth'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const error = ref('')
const loading = ref(false)

async function handleRegister() {
  error.value = ''
  if (password.value !== confirmPassword.value) {
    error.value = '兩次密碼輸入不一致'
    return
  }
  loading.value = true
  try {
    const data = await register(email.value, password.value)
    authStore.setToken(data.token)
    router.push('/')
  } catch (e) {
    error.value = e.response?.data?.message || '註冊失敗，請稍後再試'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-card">
      <div class="auth-header">
        <div class="section-label">Get started</div>
        <h1 class="section-title">建立帳號</h1>
      </div>

      <form class="auth-form" @submit.prevent="handleRegister">
        <div class="field">
          <label class="label">電子郵件</label>
          <input
            v-model="email"
            type="email"
            class="input"
            placeholder="your@email.com"
            required
            autocomplete="email"
          />
        </div>
        <div class="field">
          <label class="label">密碼 <span class="hint">（至少 8 碼）</span></label>
          <input
            v-model="password"
            type="password"
            class="input"
            placeholder="••••••••"
            required
            minlength="8"
            autocomplete="new-password"
          />
        </div>
        <div class="field">
          <label class="label">確認密碼</label>
          <input
            v-model="confirmPassword"
            type="password"
            class="input"
            placeholder="••••••••"
            required
            autocomplete="new-password"
          />
        </div>

        <p v-if="error" class="error-msg">{{ error }}</p>

        <button type="submit" class="btn btn-primary btn-full" :disabled="loading">
          <span v-if="loading" class="spinner-sm"></span>
          {{ loading ? '註冊中...' : '建立帳號' }}
        </button>
      </form>

      <p class="auth-switch">
        已有帳號？
        <router-link to="/login" class="auth-link">立即登入</router-link>
      </p>
    </div>
  </div>
</template>

<style scoped>
.auth-page {
  min-height: calc(100vh - var(--navbar-h));
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48px 16px;
}

.auth-card {
  width: 100%;
  max-width: 420px;
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 40px;
}

.auth-header {
  margin-bottom: 28px;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.label {
  font-size: 0.82rem;
  font-weight: 500;
  color: var(--text-dim);
  letter-spacing: 0.02em;
}

.hint {
  color: var(--text-muted);
  font-weight: 400;
}

.input {
  background: var(--surface);
  border: 1px solid var(--border-light);
  border-radius: var(--radius);
  padding: 10px 14px;
  color: var(--text);
  font-size: 0.9rem;
  transition: var(--transition);
  outline: none;
}

.input:focus {
  border-color: var(--accent);
  box-shadow: 0 0 0 3px var(--accent-dim);
}

.input::placeholder {
  color: var(--text-muted);
}

.error-msg {
  font-size: 0.82rem;
  color: var(--danger);
  background: var(--danger-dim);
  padding: 8px 12px;
  border-radius: var(--radius);
}

.btn-full {
  width: 100%;
  justify-content: center;
  padding: 12px;
  font-size: 0.95rem;
}

.spinner-sm {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(13, 13, 16, 0.3);
  border-top-color: #0d0d10;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.auth-switch {
  margin-top: 24px;
  text-align: center;
  font-size: 0.85rem;
  color: var(--text-muted);
}

.auth-link {
  color: var(--accent);
  font-weight: 500;
  transition: var(--transition);
}

.auth-link:hover {
  color: var(--accent-hover);
}
</style>
