<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import ProductCard from '../components/ProductCard.vue'
import { getProducts } from '../api/products'

const router = useRouter()
const featured = ref([])
const loadingFeatured = ref(true)

const CATEGORIES = [
  { key: 'SP', label: '股票', en: 'Stocks', color: '#22c55e', icon: '▲', desc: '投資股票市場' },
  { key: 'FUND', label: '基金', en: 'Funds', color: '#60a5fa', icon: '◆', desc: '多元化投資組合' },
  { key: 'BOND', label: '債券', en: 'Bonds', color: '#a78bfa', icon: '≡', desc: '穩健固定收益' },
  { key: 'ETF', label: 'ETF', en: 'ETF', color: '#34d399', icon: '▦', desc: '追蹤指數表現' },
  {
    key: 'SAVING',
    label: '儲蓄',
    en: 'Savings',
    color: '#fbbf24',
    icon: '○',
    desc: '安全保值方案',
  },
  {
    key: 'INSURANCE',
    label: '保險',
    en: 'Insurance',
    color: '#fb7185',
    icon: '⬡',
    desc: '全面風險保障',
  },
  { key: 'FOOD', label: '食品', en: 'Food', color: '#fb923c', icon: '●', desc: '優質生活食品' },
  {
    key: 'CAR',
    label: '汽車',
    en: 'Automotive',
    color: '#94a3b8',
    icon: '◉',
    desc: '精選車款服務',
  },
]

onMounted(async () => {
  try {
    const data = await getProducts()
    featured.value = data.slice(0, 4)
  } catch (e) {
    console.error(e)
  } finally {
    loadingFeatured.value = false
  }
})
</script>

<template>
  <div class="home">
    <!-- Hero -->
    <section class="hero">
      <div class="hero-bg">
        <div class="orb orb-1"></div>
        <div class="orb orb-2"></div>
        <div class="orb orb-3"></div>
        <div class="grid-overlay"></div>
      </div>
      <div class="container hero-content">
        <p class="hero-eyebrow">Premium · Curated · Trusted</p>
        <h1 class="hero-title">
          <span class="title-line">THE</span>
          <span class="title-line accent">MODERN</span>
          <span class="title-line">MARKETPLACE</span>
        </h1>
        <p class="hero-subtitle">
          精選商城 — 從金融投資到日常生活，<br class="mobile-break" />
          一站式滿足您所有需求。
        </p>
        <div class="hero-actions">
          <button class="btn btn-primary btn-hero" @click="router.push('/products')">
            探索商品
            <svg
              width="16"
              height="16"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <line x1="5" y1="12" x2="19" y2="12" />
              <polyline points="12,5 19,12 12,19" />
            </svg>
          </button>
          <button class="btn btn-ghost" @click="router.push('/admin')">管理後台</button>
        </div>
        <div class="hero-stats">
          <div class="stat">
            <span class="stat-num">8</span>
            <span class="stat-label">商品分類</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat">
            <span class="stat-num">{{ featured.length > 0 ? '∞' : '—' }}</span>
            <span class="stat-label">精選商品</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat">
            <span class="stat-num">24/7</span>
            <span class="stat-label">全天候服務</span>
          </div>
        </div>
      </div>
      <div class="hero-scroll">
        <span>向下探索</span>
        <svg
          width="14"
          height="14"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
        >
          <polyline points="6,9 12,15 18,9" />
        </svg>
      </div>
    </section>

    <!-- Categories -->
    <section class="categories-section">
      <div class="container">
        <p class="section-label">商品分類</p>
        <h2 class="section-title">選擇您的商品類別</h2>
        <p class="section-desc">從金融投資到消費品，多元選擇一應俱全</p>
        <div class="categories-grid">
          <button
            v-for="cat in CATEGORIES"
            :key="cat.key"
            class="cat-card"
            :style="{ '--cat-color': cat.color }"
            @click="router.push(`/products?category=${cat.key}`)"
          >
            <span class="cat-icon" :style="{ color: cat.color }">{{ cat.icon }}</span>
            <div class="cat-info">
              <span class="cat-name">{{ cat.label }}</span>
              <span class="cat-en">{{ cat.en }}</span>
              <span class="cat-desc">{{ cat.desc }}</span>
            </div>
            <svg
              class="cat-arrow"
              width="14"
              height="14"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
            >
              <polyline points="9,18 15,12 9,6" />
            </svg>
          </button>
        </div>
      </div>
    </section>

    <!-- Featured Products -->
    <section class="featured-section">
      <div class="container">
        <div class="featured-header">
          <div>
            <p class="section-label">精選商品</p>
            <h2 class="section-title">熱門推薦</h2>
          </div>
          <button class="btn btn-ghost" @click="router.push('/products')">
            查看全部
            <svg
              width="14"
              height="14"
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

        <div v-if="loadingFeatured" class="loading-row">
          <div class="skeleton-card" v-for="i in 4" :key="i"></div>
        </div>

        <div v-else-if="featured.length" class="products-grid">
          <ProductCard v-for="p in featured" :key="p.productId" :product="p" />
        </div>

        <div v-else class="empty-featured">
          <p>尚未有商品，請先至管理後台新增商品。</p>
          <button class="btn btn-primary" @click="router.push('/admin')">前往管理後台</button>
        </div>
      </div>
    </section>

    <!-- CTA Banner -->
    <section class="cta-section">
      <div class="container">
        <div class="cta-card">
          <div class="cta-content">
            <h2 class="cta-title">準備好開始了嗎？</h2>
            <p class="cta-sub">立即探索所有商品，找到適合您的選擇</p>
          </div>
          <button class="btn btn-primary" @click="router.push('/products')">
            開始購物
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
      </div>
    </section>
  </div>
</template>

<style scoped>
/* ---- Hero ---- */
.hero {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  inset: 0;
  z-index: 0;
}

.orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.18;
  animation: drift 14s ease-in-out infinite alternate;
}

.orb-1 {
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, #d4a84b, transparent);
  top: -200px;
  right: -100px;
  animation-delay: 0s;
}

.orb-2 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, #60a5fa, transparent);
  bottom: 0;
  left: -100px;
  animation-delay: -5s;
}

.orb-3 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, #a78bfa, transparent);
  top: 40%;
  left: 50%;
  animation-delay: -9s;
}

@keyframes drift {
  from {
    transform: translate(0, 0) scale(1);
  }
  to {
    transform: translate(30px, 20px) scale(1.08);
  }
}

.grid-overlay {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(var(--border) 1px, transparent 1px),
    linear-gradient(90deg, var(--border) 1px, transparent 1px);
  background-size: 60px 60px;
  opacity: 0.3;
}

.hero-content {
  position: relative;
  z-index: 1;
  padding-top: 80px;
  padding-bottom: 80px;
  max-width: 820px;
}

.hero-eyebrow {
  font-size: 0.72rem;
  font-weight: 600;
  letter-spacing: 0.2em;
  text-transform: uppercase;
  color: var(--accent);
  margin-bottom: 24px;
  opacity: 0;
  animation: fadeUp 0.6s ease 0.2s forwards;
}

.hero-title {
  display: flex;
  flex-direction: column;
  font-family: 'Playfair Display', serif;
  font-size: clamp(3.5rem, 8vw, 7rem);
  font-weight: 300;
  letter-spacing: -0.02em;
  line-height: 1;
  margin-bottom: 28px;
}

.title-line {
  opacity: 0;
  animation: fadeUp 0.6s ease forwards;
}

.title-line:nth-child(1) {
  animation-delay: 0.3s;
}
.title-line:nth-child(2) {
  animation-delay: 0.45s;
}
.title-line:nth-child(3) {
  animation-delay: 0.6s;
}

.title-line.accent {
  color: var(--accent);
  font-weight: 400;
  font-style: italic;
}

.hero-subtitle {
  font-size: clamp(1rem, 1.8vw, 1.1rem);
  color: var(--text-dim);
  line-height: 1.7;
  margin-bottom: 40px;
  opacity: 0;
  animation: fadeUp 0.6s ease 0.75s forwards;
}

.hero-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 60px;
  opacity: 0;
  animation: fadeUp 0.6s ease 0.9s forwards;
}

.btn-hero {
  padding: 13px 28px;
  font-size: 0.95rem;
}

.hero-stats {
  display: flex;
  align-items: center;
  gap: 32px;
  opacity: 0;
  animation: fadeUp 0.6s ease 1.05s forwards;
}

.stat {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.stat-num {
  font-family: 'Playfair Display', serif;
  font-size: 1.6rem;
  font-weight: 500;
  color: var(--text);
}

.stat-label {
  font-size: 0.72rem;
  color: var(--text-muted);
  letter-spacing: 0.04em;
}

.stat-divider {
  width: 1px;
  height: 36px;
  background: var(--border-light);
}

.hero-scroll {
  position: absolute;
  bottom: 32px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  font-size: 0.68rem;
  color: var(--text-muted);
  letter-spacing: 0.08em;
  text-transform: uppercase;
  opacity: 0;
  animation: fadeIn 0.6s ease 1.4s forwards;
}

@keyframes fadeUp {
  from {
    opacity: 0;
    transform: translateY(16px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* ---- Categories ---- */
.categories-section {
  padding: 100px 0;
  background: var(--surface);
  border-top: 1px solid var(--border);
}

.section-desc {
  font-size: 0.9rem;
  color: var(--text-muted);
  margin-bottom: 48px;
  margin-top: 4px;
}

.categories-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.cat-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 20px;
  cursor: pointer;
  text-align: left;
  display: flex;
  flex-direction: column;
  gap: 12px;
  transition:
    transform 0.25s ease,
    box-shadow 0.25s ease,
    border-color 0.25s ease;
  position: relative;
}

.cat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.5);
  border-color: var(--cat-color);
}

.cat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  border-radius: var(--radius-lg) var(--radius-lg) 0 0;
  background: var(--cat-color);
  opacity: 0;
  transition: opacity 0.25s ease;
}

.cat-card:hover::before {
  opacity: 1;
}

.cat-icon {
  font-size: 1.4rem;
  line-height: 1;
}

.cat-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
  flex: 1;
}

.cat-name {
  font-size: 1rem;
  font-weight: 600;
  color: var(--text);
  display: block;
}

.cat-en {
  font-size: 0.72rem;
  color: var(--text-muted);
  letter-spacing: 0.04em;
  display: block;
}

.cat-desc {
  font-size: 0.78rem;
  color: var(--text-muted);
  margin-top: 6px;
  display: block;
  line-height: 1.4;
}

.cat-arrow {
  color: var(--text-muted);
  transition: var(--transition);
  align-self: flex-end;
}

.cat-card:hover .cat-arrow {
  color: var(--cat-color);
  transform: translateX(3px);
}

/* ---- Featured ---- */
.featured-section {
  padding: 100px 0;
}

.featured-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  margin-bottom: 40px;
  gap: 16px;
  flex-wrap: wrap;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.loading-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.skeleton-card {
  border-radius: var(--radius-lg);
  background: var(--card);
  height: 300px;
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

.empty-featured {
  text-align: center;
  padding: 60px 20px;
  color: var(--text-muted);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  font-size: 0.9rem;
}

/* ---- CTA ---- */
.cta-section {
  padding: 0 0 80px;
}

.cta-card {
  background: linear-gradient(135deg, var(--card) 0%, var(--card-hover) 100%);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: 56px 48px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 32px;
  position: relative;
  overflow: hidden;
}

.cta-card::before {
  content: '';
  position: absolute;
  top: -60px;
  right: -60px;
  width: 240px;
  height: 240px;
  background: radial-gradient(circle, rgba(212, 168, 75, 0.15), transparent);
  border-radius: 50%;
  pointer-events: none;
}

.cta-title {
  font-size: 2rem;
  margin-bottom: 8px;
}

.cta-sub {
  font-size: 0.9rem;
  color: var(--text-dim);
}

.mobile-break {
  display: none;
}

@media (max-width: 1024px) {
  .categories-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  .products-grid,
  .loading-row {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .hero-stats {
    gap: 20px;
  }
  .cta-card {
    flex-direction: column;
    text-align: center;
    padding: 40px 24px;
  }
  .mobile-break {
    display: block;
  }
}

@media (max-width: 480px) {
  .categories-grid {
    grid-template-columns: 1fr;
  }
  .products-grid,
  .loading-row {
    grid-template-columns: 1fr;
  }
}
</style>
