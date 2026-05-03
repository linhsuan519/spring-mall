<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { createOrder } from '../api/orders'
import { useCartStore } from '../stores/cart'

const router = useRouter()
const cartStore = useCartStore()

const USER_ID = 1
const submitting = ref(false)
const error = ref('')

const totalFormatted = computed(() => new Intl.NumberFormat('zh-TW').format(cartStore.totalPrice))

function itemTotal(item) {
  return new Intl.NumberFormat('zh-TW').format(item.price * item.qty)
}

function unitPrice(item) {
  return new Intl.NumberFormat('zh-TW').format(item.price)
}

async function checkout() {
  if (!cartStore.items.length || submitting.value) return

  submitting.value = true
  error.value = ''

  const payload = {
    buyItemList: cartStore.items.map((item) => ({
      productId: item.productId,
      quantity: item.qty,
    })),
  }

  try {
    const order = await createOrder(USER_ID, payload)
    const orderId = order.orderId || order.ordertId
    cartStore.clearCart()
    router.push({ path: '/orders', query: orderId ? { created: orderId } : {} })
  } catch {
    error.value = 'Checkout failed. Please check stock or try again later.'
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="cart-page">
    <div class="page-header">
      <div class="container">
        <p class="section-label">Cart</p>
        <h1 class="page-title">
          Shopping Cart
          <span v-if="cartStore.totalItems > 0" class="count-badge">{{
            cartStore.totalItems
          }}</span>
        </h1>
      </div>
    </div>

    <div class="container cart-layout">
      <div v-if="!cartStore.items.length" class="empty-cart">
        <h2>Your cart is empty</h2>
        <p>Add products before creating an order.</p>
        <button class="btn btn-primary" @click="router.push('/products')">Browse Products</button>
      </div>

      <template v-else>
        <div class="cart-items">
          <div class="items-header">
            <span>Product</span>
            <span>Price</span>
            <span>Qty</span>
            <span>Total</span>
            <span></span>
          </div>

          <div class="items-list">
            <div v-for="item in cartStore.items" :key="item.productId" class="cart-item">
              <div class="item-product">
                <div class="item-image" @click="router.push(`/products/${item.productId}`)">
                  <img v-if="item.imageUrl" :src="item.imageUrl" :alt="item.productName" />
                  <div v-else class="img-fallback">{{ item.productName?.charAt(0) }}</div>
                </div>
                <div>
                  <p class="item-name" @click="router.push(`/products/${item.productId}`)">
                    {{ item.productName }}
                  </p>
                  <span class="item-cat">{{ item.category }}</span>
                </div>
              </div>

              <div class="item-price">NT$ {{ unitPrice(item) }}</div>

              <div class="qty-ctrl">
                <button
                  class="qty-btn"
                  @click="cartStore.updateQty(item.productId, item.qty - 1)"
                  :disabled="item.qty <= 1"
                >
                  -
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

              <div class="item-total">NT$ {{ itemTotal(item) }}</div>

              <button
                class="remove-btn"
                @click="cartStore.removeItem(item.productId)"
                title="Remove"
              >
                x
              </button>
            </div>
          </div>

          <div class="cart-footer-actions">
            <button class="btn btn-ghost btn-sm" @click="router.push('/products')">
              Continue Shopping
            </button>
            <button class="btn btn-danger btn-sm" @click="cartStore.clearCart()">Clear Cart</button>
          </div>
        </div>

        <div class="summary-card">
          <h2 class="summary-title">Order Summary</h2>

          <div class="summary-rows">
            <div class="summary-row" v-for="item in cartStore.items" :key="item.productId">
              <span>{{ item.productName }} x {{ item.qty }}</span>
              <strong>NT$ {{ itemTotal(item) }}</strong>
            </div>
          </div>

          <div class="summary-divider"></div>

          <div class="summary-total-row">
            <span>Total</span>
            <strong>NT$ {{ totalFormatted }}</strong>
          </div>

          <p v-if="error" class="checkout-error">{{ error }}</p>

          <button class="btn btn-primary checkout-btn" :disabled="submitting" @click="checkout">
            {{ submitting ? 'Creating Order...' : 'Create Order' }}
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

.empty-cart {
  grid-column: 1 / -1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 100px 20px;
  text-align: center;
}

.empty-cart h2 {
  font-size: 1.6rem;
  color: var(--text-dim);
}

.empty-cart p {
  color: var(--text-muted);
}

.items-header,
.cart-item {
  display: grid;
  grid-template-columns: 1fr 120px 120px 120px 40px;
  gap: 12px;
  align-items: center;
}

.items-header {
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border);
  font-size: 0.72rem;
  font-weight: 600;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  color: var(--text-muted);
}

.cart-item {
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
  color: var(--text-muted);
  background: var(--surface);
}

.item-name {
  font-size: 0.92rem;
  font-weight: 500;
  color: var(--text);
  cursor: pointer;
  margin-bottom: 4px;
}

.item-name:hover {
  color: var(--accent);
}

.item-cat,
.item-price {
  font-size: 0.82rem;
  color: var(--text-dim);
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
}

.summary-row,
.summary-total-row {
  display: flex;
  justify-content: space-between;
  gap: 8px;
}

.summary-row {
  font-size: 0.82rem;
  color: var(--text-dim);
}

.summary-divider {
  height: 1px;
  background: var(--border);
  margin: 16px 0;
}

.summary-total-row {
  color: var(--accent);
  font-size: 1.2rem;
}

.checkout-error {
  color: var(--danger);
  font-size: 0.82rem;
  margin: 14px 0;
}

.checkout-btn {
  width: 100%;
  justify-content: center;
  padding: 14px;
  margin-top: 18px;
}

.checkout-btn:disabled {
  opacity: 0.7;
  cursor: wait;
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
    grid-template-columns: 1fr;
  }
}
</style>
