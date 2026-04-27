<script setup>
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useCartStore } from '../stores/cart'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()
const authStore = useAuthStore()

const cartCount = computed(() => cartStore.totalItems)
const menuOpen = ref(false)

const isActive = (path) => (path === '/' ? route.path === '/' : route.path.startsWith(path))

function handleLogout() {
  authStore.logout()
  router.push('/login')
}
</script>

<template>
  <nav class="navbar">
    <div class="nav-inner container">
      <router-link to="/" class="logo">
        <span class="logo-word">Vesto</span>
        <span class="logo-divider">·</span>
        <span class="logo-sub">精選商城</span>
      </router-link>

      <div class="nav-links">
        <router-link to="/" :class="['nav-link', { active: isActive('/') }]">首頁</router-link>
        <router-link to="/products" :class="['nav-link', { active: isActive('/products') }]"
          >商品</router-link
        >
        <router-link
          v-if="authStore.isAdmin"
          to="/admin"
          :class="['nav-link', { active: isActive('/admin') }]"
          >管理</router-link
        >
      </div>

      <div class="nav-right">
        <router-link to="/cart" class="cart-link">
          <svg
            width="22"
            height="22"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1.8"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M6 2L3 6v14a2 2 0 002 2h14a2 2 0 002-2V6l-3-4z" />
            <line x1="3" y1="6" x2="21" y2="6" />
            <path d="M16 10a4 4 0 01-8 0" />
          </svg>
          <span v-if="cartCount > 0" class="cart-badge">{{
            cartCount > 99 ? '99+' : cartCount
          }}</span>
        </router-link>

        <template v-if="authStore.isLoggedIn">
          <div class="user-avatar">
            <img
              v-if="authStore.picture"
              :src="authStore.picture"
              class="avatar-img"
              referrerpolicy="no-referrer"
            />
            <span v-else class="avatar-fallback">{{ authStore.email[0]?.toUpperCase() }}</span>
          </div>
          <span class="user-email">{{ authStore.displayName || authStore.email }}</span>
          <button class="btn btn-ghost btn-sm" @click="handleLogout">登出</button>
        </template>
        <template v-else>
          <router-link to="/login" class="btn btn-primary btn-sm">登入</router-link>
        </template>

        <button class="menu-toggle" @click="menuOpen = !menuOpen" aria-label="選單">
          <span :class="['bar', { open: menuOpen }]"></span>
          <span :class="['bar', { open: menuOpen }]"></span>
          <span :class="['bar', { open: menuOpen }]"></span>
        </button>
      </div>
    </div>

    <transition name="fade">
      <div v-if="menuOpen" class="mobile-menu" @click="menuOpen = false">
        <router-link to="/" class="mobile-link">首頁</router-link>
        <router-link to="/products" class="mobile-link">商品</router-link>
        <router-link to="/cart" class="mobile-link">購物車</router-link>
        <router-link v-if="authStore.isAdmin" to="/admin" class="mobile-link">管理後台</router-link>
        <template v-if="authStore.isLoggedIn">
          <button class="mobile-link mobile-logout" @click="handleLogout">登出</button>
        </template>
        <template v-else>
          <router-link to="/login" class="mobile-link">登入 / 註冊</router-link>
        </template>
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
  background: rgba(13, 13, 16, 0.88);
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  border-bottom: 1px solid var(--border);
  height: var(--navbar-h);
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
  align-items: baseline;
  gap: 8px;
  flex-shrink: 0;
}

.logo-word {
  font-family: 'Playfair Display', serif;
  font-size: 1.5rem;
  font-weight: 600;
  letter-spacing: 0.08em;
  color: var(--accent);
}

.logo-divider {
  color: var(--text-muted);
  font-size: 0.9rem;
}

.logo-sub {
  font-size: 0.75rem;
  font-weight: 400;
  color: var(--text-dim);
  letter-spacing: 0.06em;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 4px;
}

.nav-link {
  padding: 6px 14px;
  border-radius: 8px;
  font-size: 0.88rem;
  font-weight: 400;
  color: var(--text-dim);
  transition: var(--transition);
}

.nav-link:hover {
  color: var(--text);
  background: rgba(255, 255, 255, 0.05);
}

.nav-link.active {
  color: var(--accent);
  background: var(--accent-dim);
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.cart-link {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  color: var(--text-dim);
  transition: var(--transition);
}

.cart-link:hover {
  color: var(--text);
  background: rgba(255, 255, 255, 0.05);
}

.cart-badge {
  position: absolute;
  top: 4px;
  right: 4px;
  background: var(--accent);
  color: #0d0d10;
  font-size: 0.6rem;
  font-weight: 700;
  line-height: 1;
  padding: 2px 5px;
  border-radius: 99px;
  min-width: 16px;
  text-align: center;
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  background: var(--accent-dim);
  border: 1px solid var(--border);
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-fallback {
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--accent);
}

.user-email {
  font-size: 0.8rem;
  color: var(--text-dim);
  max-width: 140px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.menu-toggle {
  display: none;
  flex-direction: column;
  gap: 4px;
  width: 40px;
  height: 40px;
  align-items: center;
  justify-content: center;
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
}

.bar {
  display: block;
  width: 20px;
  height: 1.5px;
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
  padding: 12px 24px;
  font-size: 0.95rem;
  color: var(--text-dim);
  transition: var(--transition);
  text-align: left;
}

.mobile-link:hover {
  color: var(--text);
  background: rgba(255, 255, 255, 0.04);
}

.mobile-logout {
  background: none;
  border: none;
  cursor: pointer;
  width: 100%;
  color: var(--danger);
}

@media (max-width: 640px) {
  .nav-links {
    display: none;
  }
  .user-email {
    display: none;
  }
  .menu-toggle {
    display: flex;
  }
}
</style>
