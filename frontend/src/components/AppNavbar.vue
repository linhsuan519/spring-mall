<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { useTheme } from '../composables/useTheme'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const menuOpen = ref(false)
const { theme, toggle: toggleTheme } = useTheme()

const isActive = (path) => (path === '/' ? route.path === '/' : route.path.startsWith(path))

async function logout() {
  await authStore.logout()
  menuOpen.value = false
  router.push('/login')
}
</script>

<template>
  <nav class="navbar">
    <div class="nav-inner container">
      <router-link to="/" class="logo">
        <!-- Pickleball icon: circle with holes pattern -->
        <svg width="28" height="28" viewBox="0 0 28 28" fill="none" class="logo-icon">
          <circle cx="14" cy="14" r="13" fill="#84cc16" />
          <circle cx="14" cy="14" r="13" stroke="rgba(0,0,0,0.15)" stroke-width="1" />
          <!-- holes -->
          <circle cx="9"  cy="10" r="1.6" fill="rgba(8,14,26,0.55)" />
          <circle cx="14" cy="8"  r="1.6" fill="rgba(8,14,26,0.55)" />
          <circle cx="19" cy="10" r="1.6" fill="rgba(8,14,26,0.55)" />
          <circle cx="7"  cy="15" r="1.6" fill="rgba(8,14,26,0.55)" />
          <circle cx="12" cy="14" r="1.6" fill="rgba(8,14,26,0.55)" />
          <circle cx="17" cy="14" r="1.6" fill="rgba(8,14,26,0.55)" />
          <circle cx="21" cy="15" r="1.6" fill="rgba(8,14,26,0.55)" />
          <circle cx="9"  cy="19" r="1.6" fill="rgba(8,14,26,0.55)" />
          <circle cx="14" cy="20" r="1.6" fill="rgba(8,14,26,0.55)" />
          <circle cx="19" cy="19" r="1.6" fill="rgba(8,14,26,0.55)" />
        </svg>
        <span class="logo-text">Pickle<span class="logo-accent">Zone</span></span>
      </router-link>

      <div class="nav-links">
        <router-link to="/" :class="['nav-link', { active: isActive('/') }]">首頁</router-link>
        <router-link to="/activities" :class="['nav-link', { active: isActive('/activities') }]">揪球活動</router-link>
        <router-link
          v-if="authStore.isAuthenticated"
          to="/my-activities"
          :class="['nav-link', { active: isActive('/my-activities') }]"
        >我的揪球</router-link>
      </div>

      <div class="nav-right">
        <!-- Theme toggle -->
        <button class="theme-btn" @click="toggleTheme" :title="theme === 'dark' ? '切換為亮色模式' : '切換為深色模式'" aria-label="切換主題">
          <!-- Moon icon (shown in dark mode) -->
          <svg v-if="theme === 'dark'" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M21 12.79A9 9 0 1111.21 3 7 7 0 0021 12.79z"/>
          </svg>
          <!-- Sun icon (shown in light mode) -->
          <svg v-else width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="5"/>
            <line x1="12" y1="1" x2="12" y2="3"/><line x1="12" y1="21" x2="12" y2="23"/>
            <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/><line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/>
            <line x1="1" y1="12" x2="3" y2="12"/><line x1="21" y1="12" x2="23" y2="12"/>
            <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/><line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/>
          </svg>
        </button>

        <span v-if="authStore.isAuthenticated" class="user-chip">
          {{ authStore.user?.email }}
        </span>
        <router-link v-else to="/login" class="auth-link">登入</router-link>
        <button
          v-if="authStore.isAuthenticated"
          class="logout-btn"
          type="button"
          aria-label="登出"
          title="登出"
          @click="logout"
        >
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4" />
            <polyline points="16,17 21,12 16,7" />
            <line x1="21" y1="12" x2="9" y2="12" />
          </svg>
        </button>
        <button class="menu-toggle" @click="menuOpen = !menuOpen" aria-label="選單">
          <span :class="{ open: menuOpen }"></span>
          <span :class="{ open: menuOpen }"></span>
          <span :class="{ open: menuOpen }"></span>
        </button>
      </div>
    </div>

    <transition name="fade">
      <div v-if="menuOpen" class="mobile-menu" @click="menuOpen = false">
        <router-link to="/" class="mobile-link">首頁</router-link>
        <router-link to="/activities" class="mobile-link">揪球活動</router-link>
        <router-link v-if="authStore.isAuthenticated" to="/my-activities" class="mobile-link">我的揪球</router-link>
        <router-link v-if="authStore.isAuthenticated" to="/create-activity" class="mobile-link">發起揪球</router-link>
        <router-link v-if="!authStore.isAuthenticated" to="/login" class="mobile-link">登入</router-link>
        <button v-else class="mobile-link mobile-button" type="button" @click.stop="logout">登出</button>
      </div>
    </transition>
  </nav>
</template>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: color-mix(in srgb, var(--bg) 88%, transparent);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-bottom: 1px solid var(--border);
  height: var(--navbar-h);
  transition: background 0.3s ease, border-color 0.3s ease;
}

.nav-inner {
  height: var(--navbar-h);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}

.logo-icon {
  flex-shrink: 0;
  filter: drop-shadow(0 0 8px rgba(132, 204, 22, 0.4));
}

.logo-text {
  font-family: 'Rajdhani', sans-serif;
  font-size: 1.4rem;
  font-weight: 700;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  color: var(--text);
}

.logo-accent {
  color: var(--accent);
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 2px;
}

.nav-link {
  padding: 6px 14px;
  border-radius: 8px;
  font-family: 'Rajdhani', sans-serif;
  font-size: 0.95rem;
  font-weight: 600;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  color: var(--text-dim);
  transition: var(--transition);
}

.nav-link:hover {
  color: var(--text);
  background: rgba(255, 255, 255, 0.04);
}

.nav-link.active {
  color: var(--accent);
  background: var(--accent-dim);
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.auth-link,
.user-chip,
.logout-btn {
  height: 38px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
}

.auth-link {
  padding: 0 18px;
  color: #080e1a;
  background: var(--accent);
  font-family: 'Rajdhani', sans-serif;
  font-size: 0.9rem;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  transition: var(--transition);
}

.auth-link:hover {
  background: var(--accent-hover);
  box-shadow: 0 0 16px rgba(132, 204, 22, 0.3);
}

.user-chip {
  max-width: 180px;
  padding: 0 12px;
  color: var(--text-dim);
  background: rgba(255, 255, 255, 0.04);
  font-size: 0.78rem;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  border: 1px solid var(--border);
}

.logout-btn {
  width: 38px;
  border: 0;
  background: transparent;
  color: var(--text-muted);
  cursor: pointer;
  transition: var(--transition);
}

.logout-btn:hover {
  color: var(--danger);
  background: var(--danger-dim);
}

.theme-btn {
  width: 38px;
  height: 38px;
  border: 1px solid var(--border-light);
  background: transparent;
  color: var(--text-dim);
  border-radius: 8px;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition: var(--transition);
  flex-shrink: 0;
}

.theme-btn:hover {
  color: var(--accent);
  border-color: var(--accent);
  background: var(--accent-dim);
}

.menu-toggle {
  display: none;
  flex-direction: column;
  gap: 4px;
  width: 38px;
  height: 38px;
  align-items: center;
  justify-content: center;
  background: none;
  border: none;
  cursor: pointer;
}

.menu-toggle span {
  display: block;
  width: 20px;
  height: 2px;
  background: var(--text-dim);
  border-radius: 2px;
  transition: var(--transition);
}

.mobile-menu {
  display: flex;
  flex-direction: column;
  background: var(--surface);
  border-bottom: 1px solid var(--border);
  padding: 8px 0;
}

.mobile-link {
  padding: 13px 24px;
  font-family: 'Rajdhani', sans-serif;
  font-size: 1rem;
  font-weight: 600;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  color: var(--text-dim);
  transition: var(--transition);
}

.mobile-link:hover {
  color: var(--accent);
  background: var(--accent-dim);
}

.mobile-button {
  border: 0;
  text-align: left;
  background: transparent;
  cursor: pointer;
  width: 100%;
}

@media (max-width: 700px) {
  .nav-links { display: none; }
  .user-chip,
  .auth-link,
  .logout-btn { display: none; }
  .menu-toggle { display: flex; }
}
</style>
