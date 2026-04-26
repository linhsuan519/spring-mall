<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProduct } from '../api/products'
import { useCartStore } from '../stores/cart'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()

const product = ref(null)
const loading = ref(true)
const error = ref('')
const qty = ref(1)
const addedMsg = ref(false)

const CATEGORY_MAP = {
  SP: { label: '股票', color: '#22c55e', bg: 'rgba(34,197,94,0.12)' },
  FUND: { label: '基金', color: '#60a5fa', bg: 'rgba(96,165,250,0.12)' },
  BOND: { label: '債券', color: '#a78bfa', bg: 'rgba(167,139,250,0.12)' },
  ETF: { label: 'ETF', color: '#34d399', bg: 'rgba(52,211,153,0.12)' },
  SAVING: { label: '儲蓄', color: '#fbbf24', bg: 'rgba(251,191,36,0.12)' },
  INSURANCE: { label: '保險', color: '#fb7185', bg: 'rgba(251,113,133,0.12)' },
  FOOD: { label: '食品', color: '#fb923c', bg: 'rgba(251,146,60,0.12)' },
  CAR: { label: '汽車', color: '#94a3b8', bg: 'rgba(148,163,184,0.12)' },
}

const cat = computed(() =>
  product.value
    ? CATEGORY_MAP[product.value.category] || {
        label: product.value.category,
        color: '#888',
        bg: 'rgba(136,136,136,0.12)',
      }
    : null
)

const stockStatus = computed(() => {
  if (!product.value) return null
  const s = product.value.stock
  if (s === 0) return { text: '已售完', cls: 'out' }
  if (s <= 10) return { text: `僅剩 ${s} 件`, cls: 'low' }
  return { text: '現貨供應', cls: 'in' }
})

const price = computed(() =>
  product.value ? new Intl.NumberFormat('zh-TW').format(product.value.price) : ''
)

const totalPrice = computed(() =>
  product.value ? new Intl.NumberFormat('zh-TW').format(product.value.price * qty.value) : ''
)

function changeQty(delta) {
  const max = product.value?.stock || 1
  qty.value = Math.min(Math.max(1, qty.value + delta), max)
}

function addToCart() {
  for (let i = 0; i < qty.value; i++) cartStore.addItem(product.value)
  addedMsg.value = true
  setTimeout(() => (addedMsg.value = false), 2000)
}

function formatDate(d) {
  return d ? new Date(d).toLocaleDateString('zh-TW') : '—'
}

onMounted(async () => {
  try {
    product.value = await getProduct(route.params.id)
  } catch {
    error.value = '商品不存在或無法取得資料。'
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="detail-page container">
    <!-- Breadcrumb -->
    <nav class="breadcrumb">
      <router-link to="/" class="bc-link">首頁</router-link>
      <span class="bc-sep">/</span>
      <router-link to="/products" class="bc-link">商品</router-link>
      <span class="bc-sep">/</span>
      <span class="bc-current">{{ product?.productName || '商品詳情' }}</span>
    </nav>

    <div v-if="loading" class="loading-layout">
      <div class="skeleton-img"></div>
      <div class="skeleton-info">
        <div class="sk" style="width: 30%; height: 20px; margin-bottom: 16px"></div>
        <div class="sk" style="width: 70%; height: 36px; margin-bottom: 12px"></div>
        <div class="sk" style="width: 40%; height: 24px; margin-bottom: 24px"></div>
        <div class="sk" style="width: 100%; height: 80px; margin-bottom: 24px"></div>
        <div class="sk" style="width: 50%; height: 48px"></div>
      </div>
    </div>

    <div v-else-if="error" class="error-state">
      <p>{{ error }}</p>
      <button class="btn btn-ghost" @click="router.back()">← 返回</button>
    </div>

    <div v-else-if="product" class="detail-layout">
      <!-- Image -->
      <div class="image-col">
        <div class="image-wrap">
          <img
            v-if="product.imageUrl"
            :src="product.imageUrl"
            :alt="product.productName"
            class="product-img"
          />
          <div v-else class="img-fallback">
            <span>{{ product.productName?.charAt(0) }}</span>
          </div>
          <span class="cat-badge" :style="{ color: cat.color, background: cat.bg }">{{
            cat.label
          }}</span>
        </div>
      </div>

      <!-- Info -->
      <div class="info-col">
        <div class="info-top">
          <h1 class="product-name">{{ product.productName }}</h1>
          <div class="price-row">
            <span class="price">NT$ {{ price }}</span>
            <span :class="['stock-status', stockStatus.cls]">{{ stockStatus.text }}</span>
          </div>
        </div>

        <div class="divider"></div>

        <div v-if="product.description" class="description">
          <h3 class="desc-label">商品說明</h3>
          <p>{{ product.description }}</p>
        </div>

        <div class="meta-grid">
          <div class="meta-item">
            <span class="meta-label">商品編號</span>
            <span class="meta-value">#{{ product.productId }}</span>
          </div>
          <div class="meta-item">
            <span class="meta-label">庫存數量</span>
            <span class="meta-value">{{ product.stock }} 件</span>
          </div>
          <div class="meta-item">
            <span class="meta-label">建立日期</span>
            <span class="meta-value">{{ formatDate(product.createdDate) }}</span>
          </div>
          <div class="meta-item">
            <span class="meta-label">更新日期</span>
            <span class="meta-value">{{ formatDate(product.lastModifiedDate) }}</span>
          </div>
        </div>

        <div class="divider"></div>

        <!-- Cart controls -->
        <div v-if="product.stock > 0" class="cart-controls">
          <div class="qty-wrap">
            <span class="qty-label">數量</span>
            <div class="qty-row">
              <button class="qty-btn" @click="changeQty(-1)" :disabled="qty <= 1">−</button>
              <span class="qty-val">{{ qty }}</span>
              <button class="qty-btn" @click="changeQty(1)" :disabled="qty >= product.stock">
                +
              </button>
            </div>
          </div>
          <div class="total-row">
            <span class="total-label">小計</span>
            <span class="total-price">NT$ {{ totalPrice }}</span>
          </div>
        </div>

        <div class="actions">
          <transition name="fade">
            <div v-if="addedMsg" class="added-toast">
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2.5"
              >
                <polyline points="20,6 9,17 4,12" />
              </svg>
              已加入購物車！
            </div>
          </transition>
          <button
            class="btn btn-primary add-cart-btn"
            :disabled="product.stock === 0"
            @click="addToCart"
          >
            <svg
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <path d="M6 2L3 6v14a2 2 0 002 2h14a2 2 0 002-2V6l-3-4z" />
              <line x1="3" y1="6" x2="21" y2="6" />
              <path d="M16 10a4 4 0 01-8 0" />
            </svg>
            加入購物車
          </button>
          <router-link to="/cart" class="btn btn-ghost view-cart-btn">查看購物車</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.detail-page {
  padding-top: 32px;
  padding-bottom: 80px;
  min-height: 80vh;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.82rem;
  color: var(--text-muted);
  margin-bottom: 40px;
}

.bc-link {
  transition: var(--transition);
}
.bc-link:hover {
  color: var(--text-dim);
}
.bc-sep {
  color: var(--border-light);
}
.bc-current {
  color: var(--text-dim);
}

/* Loading */
.loading-layout {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
}

.skeleton-img {
  aspect-ratio: 1;
  border-radius: var(--radius-lg);
  background: var(--card);
  animation: pulse 1.6s ease-in-out infinite;
}

.skeleton-info {
  display: flex;
  flex-direction: column;
}

.sk {
  border-radius: 6px;
  background: var(--card);
  animation: pulse 1.6s ease-in-out infinite;
}

@keyframes pulse {
  0%,
  100% {
    opacity: 0.5;
  }
  50% {
    opacity: 1;
  }
}

/* Error */
.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 80px 20px;
  color: var(--text-muted);
  text-align: center;
}

/* Main layout */
.detail-layout {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: start;
}

.image-col {
}

.image-wrap {
  position: relative;
  border-radius: var(--radius-lg);
  overflow: hidden;
  background: var(--card);
  aspect-ratio: 1;
}

.product-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.img-fallback {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--surface) 0%, var(--card-hover) 100%);
}

.img-fallback span {
  font-family: 'Playfair Display', serif;
  font-size: 5rem;
  color: var(--text-muted);
  font-weight: 300;
}

.cat-badge {
  position: absolute;
  top: 16px;
  left: 16px;
  padding: 6px 14px;
  border-radius: 99px;
  font-size: 0.78rem;
  font-weight: 600;
  letter-spacing: 0.04em;
}

/* Info col */
.info-top {
  margin-bottom: 24px;
}

.product-name {
  font-size: clamp(1.6rem, 3vw, 2.2rem);
  margin-bottom: 16px;
  line-height: 1.2;
}

.price-row {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.price {
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--accent);
  letter-spacing: -0.01em;
}

.stock-status {
  font-size: 0.82rem;
  font-weight: 500;
  padding: 4px 12px;
  border-radius: 99px;
}

.stock-status.in {
  color: #22c55e;
  background: rgba(34, 197, 94, 0.12);
}
.stock-status.low {
  color: #fbbf24;
  background: rgba(251, 191, 36, 0.12);
}
.stock-status.out {
  color: #ef4444;
  background: rgba(239, 68, 68, 0.12);
}

.divider {
  height: 1px;
  background: var(--border);
  margin: 24px 0;
}

.description {
  margin-bottom: 24px;
}

.desc-label {
  font-family: 'DM Sans', sans-serif;
  font-size: 0.72rem;
  font-weight: 600;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  color: var(--text-muted);
  margin-bottom: 10px;
}

.description p {
  font-size: 0.92rem;
  color: var(--text-dim);
  line-height: 1.7;
}

.meta-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
  background: var(--card);
  border-radius: var(--radius);
  padding: 16px;
  border: 1px solid var(--border);
}

.meta-item {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.meta-label {
  font-size: 0.68rem;
  font-weight: 600;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--text-muted);
}

.meta-value {
  font-size: 0.88rem;
  color: var(--text-dim);
}

/* Cart controls */
.cart-controls {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 20px;
}

.qty-label,
.total-label {
  font-size: 0.72rem;
  font-weight: 600;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--text-muted);
  margin-bottom: 8px;
  display: block;
}

.qty-row {
  display: flex;
  align-items: center;
  gap: 0;
  width: fit-content;
  border: 1px solid var(--border-light);
  border-radius: var(--radius);
  overflow: hidden;
}

.qty-btn {
  width: 38px;
  height: 38px;
  background: var(--surface);
  border: none;
  color: var(--text-dim);
  font-size: 1.1rem;
  cursor: pointer;
  transition: var(--transition);
  display: flex;
  align-items: center;
  justify-content: center;
}

.qty-btn:hover:not(:disabled) {
  background: var(--card-hover);
  color: var(--text);
}

.qty-btn:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}

.qty-val {
  min-width: 44px;
  text-align: center;
  font-size: 0.95rem;
  font-weight: 500;
  color: var(--text);
  background: var(--card);
  padding: 0 8px;
  line-height: 38px;
}

.total-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.total-price {
  font-size: 1.3rem;
  font-weight: 700;
  color: var(--accent);
}

.actions {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.add-cart-btn {
  justify-content: center;
  padding: 14px;
  font-size: 0.95rem;
}

.view-cart-btn {
  justify-content: center;
  padding: 12px;
}

.added-toast {
  position: absolute;
  top: -44px;
  left: 0;
  right: 0;
  background: rgba(34, 197, 94, 0.15);
  border: 1px solid rgba(34, 197, 94, 0.3);
  color: #22c55e;
  border-radius: var(--radius);
  padding: 8px 16px;
  font-size: 0.85rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

@media (max-width: 768px) {
  .detail-layout,
  .loading-layout {
    grid-template-columns: 1fr;
    gap: 32px;
  }
}
</style>
