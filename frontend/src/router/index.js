import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: () => import('../views/HomeView.vue') },
    { path: '/products', component: () => import('../views/ProductsView.vue') },
    { path: '/products/:id', component: () => import('../views/ProductDetailView.vue') },
    { path: '/cart', component: () => import('../views/CartView.vue') },
    { path: '/orders', component: () => import('../views/OrdersView.vue') },
    { path: '/admin', component: () => import('../views/AdminView.vue') },
  ],
  scrollBehavior: () => ({ top: 0, behavior: 'smooth' }),
})

export default router
