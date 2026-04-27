import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: () => import('../views/HomeView.vue') },
    { path: '/products', component: () => import('../views/ProductsView.vue') },
    { path: '/products/:id', component: () => import('../views/ProductDetailView.vue') },
    { path: '/cart', component: () => import('../views/CartView.vue') },
    {
      path: '/admin',
      component: () => import('../views/AdminView.vue'),
      meta: { requiresAdmin: true },
    },
    {
      path: '/login',
      component: () => import('../views/LoginView.vue'),
      meta: { guestOnly: true },
    },
    {
      path: '/register',
      component: () => import('../views/RegisterView.vue'),
      meta: { guestOnly: true },
    },
    { path: '/oauth2/callback', component: () => import('../views/OAuthCallbackView.vue') },
  ],
  scrollBehavior: () => ({ top: 0, behavior: 'smooth' }),
})

router.beforeEach((to) => {
  const auth = useAuthStore()

  if (to.meta.requiresAdmin && !auth.isAdmin) {
    return auth.isLoggedIn ? '/' : '/login'
  }
  if (to.meta.guestOnly && auth.isLoggedIn) {
    return '/'
  }
})

export default router
