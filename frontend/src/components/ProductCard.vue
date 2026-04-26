<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '../stores/cart'

const props = defineProps({
  product: { type: Object, required: true },
})

const router = useRouter()
const cartStore = useCartStore()

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

const cat = computed(
  () =>
    CATEGORY_MAP[props.product.category] || {
      label: props.product.category,
      color: '#888',
      bg: 'rgba(136,136,136,0.12)',
    }
)

const stockStatus = computed(() => {
  const s = props.product.stock
  if (s === 0) return { text: '已售完', cls: 'out' }
  if (s <= 10) return { text: `僅剩 ${s}`, cls: 'low' }
  return { text: '有庫存', cls: 'in' }
})

const price = computed(() => new Intl.NumberFormat('zh-TW').format(props.product.price))

function addToCart(e) {
  e.stopPropagation()
  if (props.product.stock > 0) cartStore.addItem(props.product)
}
</script>

<template>
  <article class="product-card" @click="router.push(`/products/${product.productId}`)">
    <div class="card-image-wrap">
      <img
        v-if="product.imageUrl"
        :src="product.imageUrl"
        :alt="product.productName"
        class="card-image"
        loading="lazy"
        @error="(e) => (e.target.style.display = 'none')"
      />
      <div v-else class="card-image-fallback">
        <span>{{ product.productName?.charAt(0) }}</span>
      </div>
      <span class="category-badge" :style="{ color: cat.color, background: cat.bg }">{{
        cat.label
      }}</span>
    </div>

    <div class="card-body">
      <h3 class="card-title">{{ product.productName }}</h3>
      <p v-if="product.description" class="card-desc">{{ product.description }}</p>
      <div class="card-footer">
        <div class="price-wrap">
          <span class="price">NT$ {{ price }}</span>
          <span :class="['stock-tag', stockStatus.cls]">{{ stockStatus.text }}</span>
        </div>
        <button
          class="add-btn"
          :disabled="product.stock === 0"
          @click="addToCart"
          :title="product.stock === 0 ? '已售完' : '加入購物車'"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <line x1="12" y1="5" x2="12" y2="19" />
            <line x1="5" y1="12" x2="19" y2="12" />
          </svg>
        </button>
      </div>
    </div>
  </article>
</template>

<style scoped>
.product-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  overflow: hidden;
  cursor: pointer;
  transition:
    transform 0.25s ease,
    box-shadow 0.25s ease,
    border-color 0.25s ease;
  display: flex;
  flex-direction: column;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: var(--border-light);
}

.card-image-wrap {
  position: relative;
  aspect-ratio: 4 / 3;
  background: var(--surface);
  overflow: hidden;
}

.card-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}

.product-card:hover .card-image {
  transform: scale(1.04);
}

.card-image-fallback {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--surface) 0%, var(--card-hover) 100%);
}

.card-image-fallback span {
  font-family: 'Playfair Display', serif;
  font-size: 3rem;
  color: var(--text-muted);
  font-weight: 300;
}

.category-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  padding: 4px 10px;
  border-radius: 99px;
  font-size: 0.72rem;
  font-weight: 600;
  letter-spacing: 0.04em;
}

.card-body {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
}

.card-title {
  font-family: 'Playfair Display', serif;
  font-size: 1.15rem;
  font-weight: 500;
  color: var(--text);
  line-height: 1.3;
}

.card-desc {
  font-size: 0.82rem;
  color: var(--text-muted);
  line-height: 1.5;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: auto;
  padding-top: 8px;
}

.price-wrap {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.price {
  font-size: 1.05rem;
  font-weight: 600;
  color: var(--accent);
  letter-spacing: 0.01em;
}

.stock-tag {
  font-size: 0.7rem;
  font-weight: 500;
}

.stock-tag.in {
  color: #22c55e;
}
.stock-tag.low {
  color: #fbbf24;
}
.stock-tag.out {
  color: #ef4444;
}

.add-btn {
  width: 34px;
  height: 34px;
  border-radius: 8px;
  border: 1px solid var(--border-light);
  background: transparent;
  color: var(--text-dim);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: var(--transition);
  flex-shrink: 0;
}

.add-btn:hover:not(:disabled) {
  background: var(--accent);
  border-color: var(--accent);
  color: #0d0d10;
}

.add-btn:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}
</style>
