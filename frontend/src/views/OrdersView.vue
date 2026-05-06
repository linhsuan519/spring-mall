<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { getOrders } from '../api/orders'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const authStore = useAuthStore()

const orders = ref([])
const loading = ref(true)
const error = ref('')

const createdOrderId = computed(() => route.query.created)

function money(value) {
  return new Intl.NumberFormat('zh-TW').format(value || 0)
}

function formatDate(value) {
  return value ? new Date(value).toLocaleString('zh-TW') : '-'
}

async function fetchOrders() {
  loading.value = true
  error.value = ''
  try {
    const data = await getOrders(authStore.userId)
    orders.value = data.results || []
  } catch {
    error.value = 'Unable to load orders.'
  } finally {
    loading.value = false
  }
}

onMounted(fetchOrders)
</script>

<template>
  <div class="orders-page">
    <div class="page-header">
      <div class="container">
        <p class="section-label">Orders</p>
        <h1 class="page-title">My Orders</h1>
      </div>
    </div>

    <div class="container orders-layout">
      <div v-if="createdOrderId" class="success-banner">
        Order #{{ createdOrderId }} was created successfully.
      </div>

      <div v-if="loading" class="state-box">
        <div class="spinner"></div>
        <p>Loading orders...</p>
      </div>

      <div v-else-if="error" class="state-box error">
        <p>{{ error }}</p>
        <button class="btn btn-ghost btn-sm" @click="fetchOrders">Retry</button>
      </div>

      <div v-else-if="!orders.length" class="state-box">
        <p>No orders yet.</p>
        <router-link to="/products" class="btn btn-primary">Browse Products</router-link>
      </div>

      <div v-else class="order-list">
        <article v-for="order in orders" :key="order.orderId" class="order-card">
          <div class="order-head">
            <div>
              <h2>Order #{{ order.orderId }}</h2>
              <p>{{ formatDate(order.createdDate) }}</p>
            </div>
            <strong>NT$ {{ money(order.totalAmount) }}</strong>
          </div>

          <div class="order-items">
            <div
              v-for="item in order.orderItemList || []"
              :key="item.orderItemId"
              class="order-item"
            >
              <img v-if="item.imageUrl" :src="item.imageUrl" :alt="item.productName" />
              <div v-else class="image-fallback">{{ item.productName?.charAt(0) }}</div>

              <div class="item-main">
                <span>{{ item.productName || `Product #${item.productId}` }}</span>
                <small>Product #{{ item.productId }} x {{ item.quantity }}</small>
              </div>

              <span class="item-amount">NT$ {{ money(item.amount) }}</span>
            </div>
          </div>
        </article>
      </div>
    </div>
  </div>
</template>

<style scoped>
.orders-page {
  min-height: 80vh;
}

.page-header {
  padding: 48px 0 36px;
  border-bottom: 1px solid var(--border);
  background: var(--surface);
}

.page-title {
  font-size: clamp(2rem, 4vw, 3rem);
}

.orders-layout {
  padding-top: 40px;
  padding-bottom: 80px;
}

.success-banner {
  border: 1px solid rgba(34, 197, 94, 0.35);
  background: rgba(34, 197, 94, 0.12);
  color: #22c55e;
  border-radius: var(--radius);
  padding: 14px 18px;
  margin-bottom: 24px;
}

.state-box {
  min-height: 260px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
  color: var(--text-muted);
  text-align: center;
}

.state-box.error {
  color: var(--danger);
}

.order-list {
  display: grid;
  gap: 18px;
}

.order-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 22px;
}

.order-head {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  padding-bottom: 18px;
  border-bottom: 1px solid var(--border);
}

.order-head h2 {
  font-size: 1.25rem;
}

.order-head p {
  color: var(--text-muted);
  font-size: 0.82rem;
  margin-top: 4px;
}

.order-head strong {
  color: var(--accent);
  font-size: 1.25rem;
  white-space: nowrap;
}

.order-items {
  display: grid;
  gap: 12px;
  padding-top: 18px;
}

.order-item {
  display: grid;
  grid-template-columns: 52px 1fr auto;
  gap: 14px;
  align-items: center;
}

.order-item img,
.image-fallback {
  width: 52px;
  height: 52px;
  border-radius: 8px;
  background: var(--surface);
}

.order-item img {
  object-fit: cover;
}

.image-fallback {
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-muted);
}

.item-main {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.item-main small {
  color: var(--text-muted);
}

.item-amount {
  color: var(--text-dim);
  white-space: nowrap;
}

@media (max-width: 640px) {
  .order-head,
  .order-item {
    display: grid;
    grid-template-columns: 1fr;
  }
}
</style>
