<script setup>
import { computed, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const mode = ref('login')
const form = reactive({
  email: '',
  password: '',
})
const touched = reactive({
  email: false,
  password: false,
})

const isRegister = computed(() => mode.value === 'register')
const title = computed(() => (isRegister.value ? 'Create Account' : 'Sign In'))
const subtitle = computed(() =>
  isRegister.value ? 'Create a Vesto Mall account.' : 'Use your account to manage orders.'
)

const emailError = computed(() => {
  if (!touched.email) return ''
  if (!form.email.trim()) return 'Email is required.'
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email) ? '' : 'Please enter a valid Email.'
})

const passwordError = computed(() => {
  if (!touched.password) return ''
  if (!form.password) return 'Password is required.'
  return form.password.length >= 3 ? '' : 'Password must be at least 3 characters.'
})

const formValid = computed(
  () => form.email.trim() && form.password && !emailError.value && !passwordError.value
)

function switchMode(nextMode) {
  mode.value = nextMode
  authStore.error = ''
}

async function submit() {
  touched.email = true
  touched.password = true
  if (!formValid.value || authStore.loading) return

  const payload = {
    email: form.email.trim(),
    password: form.password,
  }

  try {
    if (isRegister.value) {
      await authStore.register(payload)
    } else {
      await authStore.login(payload)
    }
    router.push(route.query.redirect?.toString() || '/')
  } catch {
    // Store exposes the user-facing error.
  }
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-shell container">
      <section class="auth-panel">
        <div class="auth-copy">
          <p class="section-label">Account</p>
          <h1>{{ title }}</h1>
          <p>{{ subtitle }}</p>
        </div>

        <div class="mode-tabs" role="tablist" aria-label="Account mode">
          <button
            type="button"
            :class="['mode-tab', { active: mode === 'login' }]"
            @click="switchMode('login')"
          >
            Sign In
          </button>
          <button
            type="button"
            :class="['mode-tab', { active: mode === 'register' }]"
            @click="switchMode('register')"
          >
            Register
          </button>
        </div>

        <form class="auth-form" @submit.prevent="submit">
          <div class="form-group" :class="{ error: emailError }">
            <label for="email">Email</label>
            <input
              id="email"
              v-model="form.email"
              type="email"
              autocomplete="email"
              placeholder="you@example.com"
              @blur="touched.email = true"
            />
            <span v-if="emailError" class="field-error">{{ emailError }}</span>
          </div>

          <div class="form-group" :class="{ error: passwordError }">
            <label for="password">Password</label>
            <input
              id="password"
              v-model="form.password"
              type="password"
              autocomplete="current-password"
              placeholder="Your password"
              @blur="touched.password = true"
            />
            <span v-if="passwordError" class="field-error">{{ passwordError }}</span>
          </div>

          <p v-if="authStore.error" class="auth-error">{{ authStore.error }}</p>

          <button class="btn btn-primary submit-btn" type="submit" :disabled="authStore.loading">
            {{ authStore.loading ? 'Please wait...' : title }}
          </button>
        </form>
      </section>
    </div>
  </div>
</template>

<style scoped>
.auth-page {
  min-height: calc(100vh - var(--navbar-h));
  background:
    linear-gradient(rgba(13, 13, 16, 0.35), rgba(13, 13, 16, 0.92)),
    url('https://images.unsplash.com/photo-1441984904996-e0b6ba687e04?auto=format&fit=crop&w=1800&q=80')
      center/cover;
}

.auth-shell {
  min-height: calc(100vh - var(--navbar-h));
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-top: 48px;
  padding-bottom: 48px;
}

.auth-panel {
  width: min(100%, 430px);
  background: rgba(26, 26, 36, 0.92);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: 30px;
  box-shadow: var(--shadow-lg);
  backdrop-filter: blur(18px);
}

.auth-copy {
  margin-bottom: 22px;
}

.auth-copy h1 {
  font-size: 2.2rem;
  margin-bottom: 8px;
}

.auth-copy p:last-child {
  color: var(--text-dim);
}

.mode-tabs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4px;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 4px;
  margin-bottom: 22px;
}

.mode-tab {
  border: 0;
  border-radius: 8px;
  background: transparent;
  color: var(--text-dim);
  padding: 9px 12px;
  cursor: pointer;
  transition: var(--transition);
}

.mode-tab.active {
  background: var(--accent);
  color: #0d0d10;
}

.auth-form {
  display: grid;
  gap: 16px;
}

.form-group {
  display: grid;
  gap: 7px;
}

.form-group label {
  font-size: 0.78rem;
  font-weight: 600;
  color: var(--text-dim);
  letter-spacing: 0.05em;
  text-transform: uppercase;
}

.form-group input {
  width: 100%;
  background: var(--surface);
  border: 1px solid var(--border-light);
  border-radius: var(--radius);
  color: var(--text);
  padding: 12px 14px;
  outline: none;
  transition: var(--transition);
}

.form-group input:focus {
  border-color: var(--accent);
  background: var(--bg);
}

.form-group.error input {
  border-color: var(--danger);
}

.field-error,
.auth-error {
  color: var(--danger);
  font-size: 0.78rem;
}

.auth-error {
  background: var(--danger-dim);
  border: 1px solid rgba(239, 68, 68, 0.28);
  border-radius: var(--radius);
  padding: 10px 12px;
}

.submit-btn {
  justify-content: center;
  padding: 13px 18px;
}

.submit-btn:disabled {
  opacity: 0.65;
  cursor: wait;
}

@media (max-width: 760px) {
  .auth-shell {
    justify-content: center;
  }

  .auth-panel {
    padding: 24px;
  }
}
</style>
