<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '../stores/cart'

const router = useRouter()
const cartStore = useCartStore()

const CATEGORY_MAP = {
  SP: '股票',
  FUND: '基金',
  BOND: '債券',
  ETF: 'ETF',
  SAVING: '儲蓄',
  INSURANCE: '保險',
  FOOD: '食品',
  CAR: '汽車',
}

const totalFormatted = computed(() => new Intl.NumberFormat('zh-TW').format(cartStore.totalPrice))

function itemTotal(item) {
  return new Intl.NumberFormat('zh-TW').format(item.price * item.qty)
}

function unitPrice(item) {
  return new Intl.NumberFormat('zh-TW').format(item.price)
}
</script>

<template>
  <div class="cart-page">
    <div class="page-header">
      <div class="container">
        <p class="section-label">購物車</p>
        <h1 class="page-title">
          我的購物車
          <span v-if="cartStore.totalItems > 0" class="count-badge">{{
            cartStore.totalItems
          }}</span>
        </h1>
      </div>
    </div>

    <div class="container cart-layout">
      <!-- Empty state -->
      <div v-if="!cartStore.items.length" class="empty-cart">
        <div class="empty-icon">
          <svg
            width="64"
            height="64"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1"
          >
            <path d="M6 2L3 6v14a2 2 0 002 2h14a2 2 0 002-2V6l-3-4z" />
            <line x1="3" y1="6" x2="21" y2="6" />
            <path d="M16 10a4 4 0 01-8 0" />
          </svg>
        </div>
        <h2>購物車是空的</h2>
        <p>探索我們的商品，找到您喜歡的物品</p>
        <button class="btn btn-primary" @click="router.push('/products')">開始購物</button>
      </div>

      <template v-else>
        <!-- Items list -->
        <div class="cart-items">
          <div class="items-header">
            <span class="col-product">商品</span>
            <span class="col-price">單價</span>
            <span class="col-qty">數量</span>
            <span class="col-total">小計</span>
            <span class="col-action"></span>
          </div>

          <transition-group name="list" tag="div" class="items-list">
            <div v-for="item in cartStore.items" :key="item.productId" class="cart-item">
              <div class="col-product item-product">
                <div class="item-image" @click="router.push(`/products/${item.productId}`)">
                  <img v-if="item.imageUrl" :src="item.imageUrl" :alt="item.productName" />
                  <div v-else class="img-fallback">{{ item.productName?.charAt(0) }}</div>
                </div>
                <div class="item-info">
                  <p class="item-name" @click="router.push(`/products/${item.productId}`)">
                    {{ item.productName }}
                  </p>
                  <span class="item-cat">{{ CATEGORY_MAP[item.category] || item.category }}</span>
                </div>
              </div>

              <div class="col-price item-price">NT$ {{ unitPrice(item) }}</div>

              <div class="col-qty item-qty">
                <div class="qty-ctrl">
                  <button
                    class="qty-btn"
                    @click="cartStore.updateQty(item.productId, item.qty - 1)"
                    :disabled="item.qty <= 1"
                  >
                    −
                  </button>
                  <span class="qty-num">{{ item.qty }}</span>
                  <button
                    class="qty-btn"
                    @click="cartStore.updateQty(item.productId, item.qty + 1)"
                    :disabled="item.qty >= item.stock"
                  >
                    +
                  </button>
                </div>
              </div>

              <div class="col-total item-total">NT$ {{ itemTotal(item) }}</div>

              <div class="col-action">
                <button
                  class="remove-btn"
                  @click="cartStore.removeItem(item.productId)"
                  title="移除"
                >
                  <svg
                    width="16"
                    height="16"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                  >
                    <line x1="18" y1="6" x2="6" y2="18" />
                    <line x1="6" y1="6" x2="18" y2="18" />
                  </svg>
                </button>
              </div>
            </div>
          </transition-group>

          <div class="cart-footer-actions">
            <button class="btn btn-ghost btn-sm" @click="router.push('/products')">
              ← 繼續購物
            </button>
            <button class="btn btn-danger btn-sm" @click="cartStore.clearCart()">清空購物車</button>
          </div>
        </div>

        <!-- Summary -->
        <div class="summary-card">
          <h2 class="summary-title">訂單摘要</h2>

          <div class="summary-rows">
            <div class="summary-row" v-for="item in cartStore.items" :key="item.productId">
              <span class="summary-item-name">{{ item.productName }} × {{ item.qty }}</span>
              <span>NT$ {{ itemTotal(item) }}</span>
            </div>
          </div>

          <div class="summary-divider"></div>

          <div class="summary-total-row">
            <span class="summary-total-label">合計</span>
            <span class="summary-total-price">NT$ {{ totalFormatted }}</span>
          </div>

          <div class="summary-note">
            <svg
              width="14"
              height="14"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
            >
              <circle cx="12" cy="12" r="10" />
              <line x1="12" y1="8" x2="12" y2="12" />
              <line x1="12" y1="16" x2="12.01" y2="16" />
            </svg>
            價格不含運費與稅金
          </div>

          <button class="btn btn-primary checkout-btn">
            前往結帳
            <svg
              width="16"
              height="16"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
            >
              <line x1="5" y1="12" x2="19" y2="12" />
              <polyline points="12,5 19,12 12,19" />
            </svg>
          </button>
        </div>
      </template>
    </div>
  </div>
</template>

<style scoped>
.cart-page {
  min-height: 80vh;
}

.page-header {
  padding: 48px 0 36px;
  border-bottom: 1px solid var(--border);
  background: var(--surface);
}

.page-title {
  font-size: clamp(2rem, 4vw, 3rem);
  display: flex;
  align-items: center;
  gap: 14px;
}

.count-badge {
  font-family: 'DM Sans', sans-serif;
  font-size: 1rem;
  font-weight: 600;
  background: var(--accent-dim2);
  color: var(--accent);
  padding: 2px 12px;
  border-radius: 99px;
}

.cart-layout {
  padding-top: 40px;
  padding-bottom: 80px;
  display: grid;
  grid-template-columns: 1fr 340px;
  gap: 40px;
  align-items: start;
}

/* Empty */
.empty-cart {
  grid-column: 1 / -1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 100px 20px;
  text-align: center;
}

.empty-icon {
  color: var(--text-muted);
  margin-bottom: 8px;
}

.empty-cart h2 {
  font-size: 1.6rem;
  color: var(--text-dim);
}

.empty-cart p {
  font-size: 0.9rem;
  color: var(--text-muted);
}

/* Items */
.cart-items {
}

.items-header {
  display: grid;
  grid-template-columns: 1fr 120px 120px 120px 40px;
  gap: 12px;
  padding: 0 0 12px;
  border-bottom: 1px solid var(--border);
  font-size: 0.72rem;
  font-weight: 600;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  color: var(--text-muted);
}

.items-list {
  display: flex;
  flex-direction: column;
}

.cart-item {
  display: grid;
  grid-template-columns: 1fr 120px 120px 120px 40px;
  gap: 12px;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid var(--border);
}

.item-product {
  display: flex;
  align-items: center;
  gap: 16px;
}

.item-image {
  width: 64px;
  height: 64px;
  border-radius: 8px;
  overflow: hidden;
  background: var(--card);
  flex-shrink: 0;
  cursor: pointer;
}

.item-image img {
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
  font-family: 'Playfair Display', serif;
  font-size: 1.5rem;
  color: var(--text-muted);
  background: var(--surface);
}

.item-name {
  font-size: 0.92rem;
  font-weight: 500;
  color: var(--text);
  cursor: pointer;
  transition: var(--transition);
  margin-bottom: 4px;
}

.item-name:hover {
  color: var(--accent);
}

.item-cat {
  font-size: 0.72rem;
  color: var(--text-muted);
}

.item-price {
  font-size: 0.88rem;
  color: var(--text-dim);
}

.item-qty {
}

.qty-ctrl {
  display: flex;
  align-items: center;
  border: 1px solid var(--border-light);
  border-radius: 8px;
  overflow: hidden;
  width: fit-content;
}

.qty-btn {
  width: 30px;
  height: 30px;
  background: var(--surface);
  border: none;
  color: var(--text-dim);
  cursor: pointer;
  font-size: 1rem;
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

.qty-num {
  min-width: 32px;
  text-align: center;
  font-size: 0.88rem;
  font-weight: 500;
  background: var(--card);
  line-height: 30px;
}

.item-total {
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--accent);
}

.remove-btn {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: none;
  border: 1px solid transparent;
  color: var(--text-muted);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: var(--transition);
}

.remove-btn:hover {
  background: var(--danger-dim);
  border-color: var(--danger);
  color: var(--danger);
}

.cart-footer-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

/* Summary */
.summary-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 28px;
  position: sticky;
  top: calc(var(--navbar-h) + 20px);
}

.summary-title {
  font-size: 1.3rem;
  margin-bottom: 20px;
}

.summary-rows {
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-height: 200px;
  overflow-y: auto;
  margin-bottom: 4px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  gap: 8px;
  font-size: 0.82rem;
  color: var(--text-dim);
}

.summary-item-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}

.summary-divider {
  height: 1px;
  background: var(--border);
  margin: 16px 0;
}

.summary-total-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.summary-total-label {
  font-size: 0.88rem;
  font-weight: 600;
  color: var(--text-dim);
}

.summary-total-price {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--accent);
}

.summary-note {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.72rem;
  color: var(--text-muted);
  margin-bottom: 20px;
}

.checkout-btn {
  width: 100%;
  justify-content: center;
  padding: 14px;
  font-size: 0.95rem;
}

/* List transition */
.list-enter-active,
.list-leave-active {
  transition: all 0.3s ease;
}
.list-enter-from {
  opacity: 0;
  transform: translateX(-20px);
}
.list-leave-to {
  opacity: 0;
  transform: translateX(20px);
}

@media (max-width: 900px) {
  .cart-layout {
    grid-template-columns: 1fr;
  }
  .summary-card {
    position: static;
  }
  .items-header {
    display: none;
  }
  .cart-item {
    grid-template-columns: 1fr auto;
    grid-template-areas: 'product action' 'price total' 'qty .';
    gap: 8px;
  }
  .col-product {
    grid-area: product;
  }
  .col-action {
    grid-area: action;
  }
  .col-price {
    grid-area: price;
  }
  .col-total {
    grid-area: total;
  }
  .col-qty {
    grid-area: qty;
  }
}
</style>
