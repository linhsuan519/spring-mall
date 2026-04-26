<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import ProductCard from '../components/ProductCard.vue'
import { getProducts } from '../api/products'

const route = useRoute()
const router = useRouter()

const products = ref([])
const loading = ref(true)
const error = ref('')
const searchInput = ref(route.query.search || '')
const activeCategory = ref(route.query.category || '')

const CATEGORIES = [
  { key: '', label: '全部', color: '#ece8de' },
  { key: 'SP', label: '股票', color: '#22c55e' },
  { key: 'FUND', label: '基金', color: '#60a5fa' },
  { key: 'BOND', label: '債券', color: '#a78bfa' },
  { key: 'ETF', label: 'ETF', color: '#34d399' },
  { key: 'SAVING', label: '儲蓄', color: '#fbbf24' },
  { key: 'INSURANCE', label: '保險', color: '#fb7185' },
  { key: 'FOOD', label: '食品', color: '#fb923c' },
  { key: 'CAR', label: '汽車', color: '#94a3b8' },
]

async function fetchProducts() {
  loading.value = true
  error.value = ''
  try {
    const params = {}
    if (activeCategory.value) params.category = activeCategory.value
    if (searchInput.value.trim()) params.search = searchInput.value.trim()
    products.value = await getProducts(params)
  } catch (e) {
    error.value = '無法載入商品，請確認後端服務是否啟動。'
  } finally {
    loading.value = false
  }
}

function selectCategory(key) {
  activeCategory.value = key
  router.replace({ query: { ...route.query, category: key || undefined } })
}

let searchTimer
function onSearchInput() {
  clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    router.replace({ query: { ...route.query, search: searchInput.value || undefined } })
    fetchProducts()
  }, 400)
}

watch(activeCategory, fetchProducts)

onMounted(() => {
  activeCategory.value = route.query.category || ''
  searchInput.value = route.query.search || ''
  fetchProducts()
})
</script>

<template>
  <div class="products-page">
    <div class="page-header">
      <div class="container">
        <p class="section-label">商品目錄</p>
        <h1 class="page-title">所有商品</h1>
      </div>
    </div>

    <div class="container products-layout">
      <!-- Sidebar -->
      <aside class="sidebar">
        <div class="sidebar-block">
          <h3 class="sidebar-heading">搜尋</h3>
          <div class="search-wrap">
            <svg
              class="search-icon"
              width="15"
              height="15"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <circle cx="11" cy="11" r="8" />
              <line x1="21" y1="21" x2="16.65" y2="16.65" />
            </svg>
            <input
              v-model="searchInput"
              @input="onSearchInput"
              type="text"
              placeholder="搜尋商品名稱..."
              class="search-input"
            />
          </div>
        </div>

        <div class="sidebar-block">
          <h3 class="sidebar-heading">商品分類</h3>
          <div class="cat-filters">
            <button
              v-for="cat in CATEGORIES"
              :key="cat.key"
              :class="['cat-btn', { active: activeCategory === cat.key }]"
              :style="activeCategory === cat.key ? { '--active-color': cat.color } : {}"
              @click="selectCategory(cat.key)"
            >
              <span class="cat-dot" :style="{ background: cat.color }"></span>
              {{ cat.label }}
            </button>
          </div>
        </div>
      </aside>

      <!-- Main content -->
      <div class="products-main">
        <div class="results-bar">
          <span class="results-count">
            <template v-if="!loading">
              {{ products.length }} 件商品
              <template v-if="activeCategory">
                · {{ CATEGORIES.find((c) => c.key === activeCategory)?.label }}
              </template>
            </template>
          </span>
        </div>

        <div v-if="loading" class="grid-loading">
          <div class="skeleton-card" v-for="i in 6" :key="i"></div>
        </div>

        <div v-else-if="error" class="state-box error">
          <svg
            width="40"
            height="40"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1.5"
          >
            <circle cx="12" cy="12" r="10" />
            <line x1="12" y1="8" x2="12" y2="12" />
            <line x1="12" y1="16" x2="12.01" y2="16" />
          </svg>
          <p>{{ error }}</p>
        </div>

        <div v-else-if="!products.length" class="state-box">
          <svg
            width="40"
            height="40"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1.5"
          >
            <path
              d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"
            />
          </svg>
          <p>目前沒有符合條件的商品</p>
          <button
            class="btn btn-ghost btn-sm"
            @click="
              activeCategory = ''
              searchInput = ''
              fetchProducts()
            "
          >
            清除篩選
          </button>
        </div>

        <div v-else class="products-grid">
          <ProductCard v-for="p in products" :key="p.productId" :product="p" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.products-page {
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

.products-layout {
  display: grid;
  grid-template-columns: 220px 1fr;
  gap: 40px;
  padding-top: 40px;
  padding-bottom: 60px;
  align-items: start;
}

/* Sidebar */
.sidebar {
  position: sticky;
  top: calc(var(--navbar-h) + 20px);
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.sidebar-block {
}

.sidebar-heading {
  font-family: 'DM Sans', sans-serif;
  font-size: 0.72rem;
  font-weight: 600;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: var(--text-muted);
  margin-bottom: 12px;
}

.search-wrap {
  position: relative;
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--text-muted);
  pointer-events: none;
}

.search-input {
  width: 100%;
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 10px 14px 10px 36px;
  color: var(--text);
  font-size: 0.88rem;
  outline: none;
  transition: var(--transition);
}

.search-input::placeholder {
  color: var(--text-muted);
}

.search-input:focus {
  border-color: var(--accent);
  background: var(--card-hover);
}

.cat-filters {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.cat-btn {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 9px 12px;
  border-radius: var(--radius);
  background: none;
  border: 1px solid transparent;
  color: var(--text-dim);
  font-size: 0.88rem;
  cursor: pointer;
  text-align: left;
  transition: var(--transition);
}

.cat-btn:hover {
  background: rgba(255, 255, 255, 0.04);
  color: var(--text);
}

.cat-btn.active {
  background: rgba(var(--active-color), 0.08);
  border-color: var(--active-color, var(--accent));
  color: var(--text);
}

.cat-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  flex-shrink: 0;
}

/* Main */
.results-bar {
  margin-bottom: 20px;
}

.results-count {
  font-size: 0.85rem;
  color: var(--text-muted);
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.grid-loading {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.skeleton-card {
  border-radius: var(--radius-lg);
  background: var(--card);
  height: 280px;
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

.state-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 80px 20px;
  color: var(--text-muted);
  text-align: center;
  font-size: 0.9rem;
}

.state-box.error {
  color: var(--danger);
}

@media (max-width: 900px) {
  .products-layout {
    grid-template-columns: 1fr;
  }
  .sidebar {
    position: static;
  }
  .products-grid,
  .grid-loading {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .products-grid,
  .grid-loading {
    grid-template-columns: 1fr;
  }
}
</style>
