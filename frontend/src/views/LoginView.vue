<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../api/auth'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)

async function handleLogin() {
  error.value = ''
  loading.value = true
  try {
    const data = await login(email.value, password.value)
    authStore.setToken(data.token)
    router.push('/')
  } catch (e) {
    error.value = e.response?.data?.message || '登入失敗，請確認帳號密碼'
  } finally {
    loading.value = false
  }
}

function loginWithGoogle() {
  window.location.href = '/oauth2/authorization/google'
}

function loginWithLine() {
  window.location.href = '/oauth2/authorization/line'
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-card">
      <div class="auth-header">
        <div class="section-label">Welcome back</div>
        <h1 class="section-title">登入帳號</h1>
      </div>

      <form class="auth-form" @submit.prevent="handleLogin">
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
          <label class="label">密碼</label>
          <input
            v-model="password"
            type="password"
            class="input"
            placeholder="••••••••"
            required
            autocomplete="current-password"
          />
        </div>

        <p v-if="error" class="error-msg">{{ error }}</p>

        <button type="submit" class="btn btn-primary btn-full" :disabled="loading">
          <span v-if="loading" class="spinner-sm"></span>
          {{ loading ? '登入中...' : '登入' }}
        </button>
      </form>

      <div class="divider-text"><span>或使用第三方登入</span></div>

      <div class="oauth-buttons">
        <button class="btn-oauth" @click="loginWithGoogle">
          <svg width="20" height="20" viewBox="0 0 48 48">
            <path
              fill="#EA4335"
              d="M24 9.5c3.54 0 6.71 1.22 9.21 3.6l6.85-6.85C35.9 2.38 30.47 0 24 0 14.62 0 6.51 5.38 2.56 13.22l7.98 6.19C12.43 13.72 17.74 9.5 24 9.5z"
            />
            <path
              fill="#4285F4"
              d="M46.98 24.55c0-1.57-.15-3.09-.38-4.55H24v9.02h12.94c-.58 2.96-2.26 5.48-4.78 7.18l7.73 6c4.51-4.18 7.09-10.36 7.09-17.65z"
            />
            <path
              fill="#FBBC05"
              d="M10.53 28.59c-.48-1.45-.76-2.99-.76-4.59s.27-3.14.76-4.59l-7.98-6.19C.92 16.46 0 20.12 0 24c0 3.88.92 7.54 2.56 10.78l7.97-6.19z"
            />
            <path
              fill="#34A853"
              d="M24 48c6.48 0 11.93-2.13 15.89-5.81l-7.73-6c-2.18 1.48-4.97 2.31-8.16 2.31-6.26 0-11.57-4.22-13.47-9.91l-7.98 6.19C6.51 42.62 14.62 48 24 48z"
            />
          </svg>
          使用 Google 登入
        </button>

        <button class="btn-oauth btn-line" @click="loginWithLine">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
            <path
              d="M19.365 9.863c.349 0 .63.285.63.631 0 .345-.281.63-.63.63H17.61v1.125h1.755c.349 0 .63.283.63.63 0 .344-.281.629-.63.629h-2.386c-.345 0-.627-.285-.627-.629V8.108c0-.345.282-.63.63-.63h2.386c.346 0 .627.285.627.63 0 .349-.281.63-.63.63H17.61v1.125h1.755zm-3.855 3.016c0 .27-.174.51-.432.596-.064.021-.133.031-.199.031-.211 0-.391-.09-.51-.25l-2.443-3.317v2.94c0 .344-.279.629-.631.629-.346 0-.626-.285-.626-.629V8.108c0-.27.173-.51.43-.595.06-.023.136-.033.194-.033.195 0 .375.104.495.254l2.462 3.33V8.108c0-.345.282-.63.63-.63.345 0 .63.285.63.63v4.771zm-5.741 0c0 .344-.282.629-.631.629-.345 0-.627-.285-.627-.629V8.108c0-.345.282-.63.627-.63.349 0 .631.285.631.63v4.771zm-2.466.629H4.917c-.345 0-.63-.285-.63-.629V8.108c0-.345.285-.63.63-.63.348 0 .63.285.63.63v4.141h1.756c.348 0 .629.283.629.63 0 .344-.281.629-.629.629M24 10.314C24 4.943 18.615.572 12 .572S0 4.943 0 10.314c0 4.811 4.27 8.842 10.035 9.608.391.082.923.258 1.058.59.12.301.079.766.038 1.08l-.164 1.02c-.045.301-.24 1.186 1.049.645 1.291-.539 6.916-4.078 9.436-6.975C23.176 14.393 24 12.458 24 10.314"
            />
          </svg>
          使用 LINE 登入
        </button>
      </div>

      <p class="auth-switch">
        還沒有帳號？
        <router-link to="/register" class="auth-link">立即註冊</router-link>
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

.divider-text {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 24px 0;
  color: var(--text-muted);
  font-size: 0.78rem;
}

.divider-text::before,
.divider-text::after {
  content: '';
  flex: 1;
  height: 1px;
  background: var(--border);
}

.oauth-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.btn-oauth {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  width: 100%;
  padding: 11px;
  border-radius: var(--radius);
  font-size: 0.88rem;
  font-weight: 500;
  cursor: pointer;
  transition: var(--transition);
  background: var(--surface);
  border: 1px solid var(--border-light);
  color: var(--text);
}

.btn-oauth:hover {
  border-color: var(--accent);
  background: var(--accent-dim);
}

.btn-line {
  color: #06c755;
}

.btn-line:hover {
  border-color: #06c755;
  background: rgba(6, 199, 85, 0.08);
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
