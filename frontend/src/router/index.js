import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: () => import('../views/HomeView.vue') },
    { path: '/activities', component: () => import('../views/ActivitiesView.vue') },
    { path: '/activities/:id', component: () => import('../views/ActivityDetailView.vue') },
    {
      path: '/create-activity',
      component: () => import('../views/CreateActivityView.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/my-activities',
      component: () => import('../views/MyActivitiesView.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/login',
      component: () => import('../views/LoginView.vue'),
      meta: { guestOnly: true },
    },
  ],
  scrollBehavior: () => ({ top: 0, behavior: 'smooth' }),
})

router.beforeEach((to) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }

  if (to.meta.guestOnly && authStore.isAuthenticated) {
    return to.query.redirect?.toString() || '/'
  }

  return true
})

export default router
