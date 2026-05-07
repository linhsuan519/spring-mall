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
        <span class="logo-word">Vesto</span>
        <span class="logo-sub">Mall</span>
      </router-link>

      <div class="nav-links">
        <router-link to="/" :class="['nav-link', { active: isActive('/') }]">Home</router-link>
        <router-link to="/products" :class="['nav-link', { active: isActive('/products') }]">
          Products
        </router-link>
        <router-link to="/orders" :class="['nav-link', { active: isActive('/orders') }]">
          Orders
        </router-link>
        <router-link to="/admin" :class="['nav-link', { active: isActive('/admin') }]">
          Admin
        </router-link>
      </div>

      <div class="nav-right">
        <span v-if="authStore.isAuthenticated" class="user-chip">
          {{ authStore.user.email }}
        </span>
        <router-link v-else to="/login" class="auth-link">Sign In</router-link>
        <router-link to="/cart" class="cart-link" aria-label="Cart">
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
        <button
          v-if="authStore.isAuthenticated"
          class="logout-btn"
          type="button"
          aria-label="Logout"
          title="Logout"
          @click="logout"
        >
          <svg
            width="19"
            height="19"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1.8"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4" />
            <polyline points="16,17 21,12 16,7" />
            <line x1="21" y1="12" x2="9" y2="12" />
          </svg>
        </button>
        <button class="menu-toggle" @click="menuOpen = !menuOpen" aria-label="Menu">
          <span></span>
          <span></span>
          <span></span>
        </button>
      </div>
    </div>

    <transition name="fade">
      <div v-if="menuOpen" class="mobile-menu" @click="menuOpen = false">
        <router-link to="/" class="mobile-link">Home</router-link>
        <router-link to="/products" class="mobile-link">Products</router-link>
        <router-link to="/cart" class="mobile-link">Cart</router-link>
        <router-link to="/orders" class="mobile-link">Orders</router-link>
        <router-link to="/admin" class="mobile-link">Admin</router-link>
        <router-link v-if="!authStore.isAuthenticated" to="/login" class="mobile-link">
          Sign In
        </router-link>
        <button v-else class="mobile-link mobile-button" type="button" @click.stop="logout">
          Logout
        </button>
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

.logo-sub {
  font-size: 0.75rem;
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

.auth-link,
.user-chip,
.logout-btn {
  height: 40px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
}

.auth-link {
  padding: 0 14px;
  color: #0d0d10;
  background: var(--accent);
  font-size: 0.84rem;
  font-weight: 600;
}

.auth-link:hover {
  background: var(--accent-hover);
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
}

.logout-btn {
  width: 40px;
  border: 0;
  background: transparent;
  color: var(--text-dim);
  cursor: pointer;
  transition: var(--transition);
}

.logout-btn:hover {
  color: var(--danger);
  background: var(--danger-dim);
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
}

.menu-toggle span {
  display: block;
  width: 20px;
  height: 1.5px;
  background: var(--text-dim);
  border-radius: 2px;
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
}

.mobile-link:hover {
  color: var(--text);
  background: rgba(255, 255, 255, 0.04);
}

.mobile-button {
  border: 0;
  text-align: left;
  background: transparent;
  cursor: pointer;
}

@media (max-width: 700px) {
  .nav-links {
    display: none;
  }

  .user-chip,
  .auth-link,
  .logout-btn {
    display: none;
  }

  .menu-toggle {
    display: flex;
  }
}
</style>
