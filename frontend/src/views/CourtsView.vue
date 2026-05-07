<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getCourts } from '../api/courts'

const router = useRouter()
const route = useRoute()

const courts = ref([])
const loading = ref(true)
const error = ref('')
const activeFilter = ref(route.query.type || 'ALL')

const FILTERS = [
  { key: 'ALL',     label: '全部場地' },
  { key: 'INDOOR',  label: '室內' },
  { key: 'OUTDOOR', label: '室外' },
]

const STATUS_LABELS = {
  AVAILABLE:   { text: '可預約', cls: 'badge-lime' },
  MAINTENANCE: { text: '維護中', cls: 'badge-yellow' },
  CLOSED:      { text: '暫停開放', cls: 'badge-gray' },
}

async function fetchCourts() {
  loading.value = true
  error.value = ''
  try {
    const params = { limit: 50, offset: 0 }
    if (activeFilter.value !== 'ALL') params.courtType = activeFilter.value
    const data = await getCourts(params)
    courts.value = data.results || data
  } catch (e) {
    error.value = '無法載入場地資料，請稍後再試。'
  } finally {
    loading.value = false
  }
}

function setFilter(key) {
  activeFilter.value = key
  router.replace({ query: key !== 'ALL' ? { type: key } : {} })
}

onMounted(fetchCourts)
watch(activeFilter, fetchCourts)
</script>

<template>
  <div class="courts-page">

    <!-- Page header -->
    <div class="page-header">
      <div class="header-bg">
        <div class="header-orb"></div>
      </div>
      <div class="container header-content">
        <p class="section-label">場地列表</p>
        <h1 class="page-title">選擇你的場地</h1>
        <p class="page-sub">所有可預約的匹克球場地一覽</p>
      </div>
    </div>

    <div class="container page-body">

      <!-- Filter tabs -->
      <div class="filter-bar">
        <button
          v-for="f in FILTERS"
          :key="f.key"
          :class="['filter-btn', { active: activeFilter === f.key }]"
          @click="setFilter(f.key)"
        >{{ f.label }}</button>
      </div>

      <!-- Loading skeletons -->
      <div v-if="loading" class="courts-grid">
        <div v-for="i in 6" :key="i" class="skeleton-card">
          <div class="skel-img"></div>
          <div class="skel-body">
            <div class="skel-line skel-w60"></div>
            <div class="skel-line skel-w40"></div>
            <div class="skel-line skel-w80"></div>
          </div>
        </div>
      </div>

      <!-- Error -->
      <div v-else-if="error" class="empty-state">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/>
        </svg>
        <p>{{ error }}</p>
        <button class="btn btn-ghost btn-sm" @click="fetchCourts">重新載入</button>
      </div>

      <!-- Court grid -->
      <div v-else-if="courts.length" class="courts-grid">
        <div v-for="court in courts" :key="court.courtId" class="court-card">
          <!-- Court visual header -->
          <div class="court-visual" :class="court.courtType === 'INDOOR' ? 'visual-indoor' : 'visual-outdoor'">
            <div class="court-lines-mini">
              <svg viewBox="0 0 200 120" fill="none" width="100%" height="100%">
                <rect x="10" y="10" width="180" height="100" stroke="currentColor" stroke-width="1.5" opacity="0.3"/>
                <line x1="10" y1="43" x2="100" y2="43" stroke="currentColor" stroke-width="1" opacity="0.25"/>
                <line x1="100" y1="43" x2="190" y2="43" stroke="currentColor" stroke-width="1" opacity="0.25"/>
                <line x1="10" y1="77" x2="100" y2="77" stroke="currentColor" stroke-width="1" opacity="0.25"/>
                <line x1="100" y1="77" x2="190" y2="77" stroke="currentColor" stroke-width="1" opacity="0.25"/>
                <line x1="100" y1="10" x2="100" y2="43" stroke="currentColor" stroke-width="1" opacity="0.25"/>
                <line x1="100" y1="77" x2="100" y2="110" stroke="currentColor" stroke-width="1" opacity="0.25"/>
                <circle cx="100" cy="60" r="4" fill="currentColor" opacity="0.4"/>
              </svg>
            </div>
            <span :class="['type-badge', court.courtType === 'INDOOR' ? 'badge-sky' : 'badge-lime', 'badge']">
              {{ court.courtType === 'INDOOR' ? '室內' : '室外' }}
            </span>
          </div>

          <!-- Card body -->
          <div class="card-body">
            <div class="card-top">
              <h3 class="court-name">{{ court.courtName }}</h3>
              <span :class="['badge', STATUS_LABELS[court.status]?.cls]">
                {{ STATUS_LABELS[court.status]?.text || court.status }}
              </span>
            </div>
            <p class="court-location">
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 10c0 7-9 13-9 13S3 17 3 10a9 9 0 0118 0z"/><circle cx="12" cy="10" r="3"/>
              </svg>
              {{ court.location }}
            </p>
            <p v-if="court.description" class="court-desc">{{ court.description }}</p>
            <div class="card-footer">
              <div class="price">
                <span class="price-num">NT$ {{ court.pricePerHour }}</span>
                <span class="price-unit">/ 小時</span>
              </div>
              <button
                class="btn btn-primary btn-sm"
                :disabled="court.status !== 'AVAILABLE'"
                @click="router.push(`/courts/${court.courtId}`)"
              >立即預約</button>
            </div>
          </div>
        </div>
      </div>

      <!-- Empty state -->
      <div v-else class="empty-state">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/><polyline points="9,22 9,12 15,12 15,22"/>
        </svg>
        <p>目前沒有符合條件的場地</p>
      </div>

    </div>
  </div>
</template>

<style scoped>
/* Header */
.page-header {
  position: relative;
  padding: calc(var(--navbar-h) + 48px) 0 48px;
  background: var(--surface);
  border-bottom: 1px solid var(--border);
  overflow: hidden;
}

.header-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.header-orb {
  position: absolute;
  right: -100px; top: -100px;
  width: 400px; height: 400px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(132,204,22,0.08), transparent 70%);
}

.header-content { position: relative; z-index: 1; }

.page-title {
  font-family: 'Rajdhani', sans-serif;
  font-size: clamp(2.5rem, 5vw, 4rem);
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  margin-bottom: 8px;
}

.page-sub {
  font-size: 0.95rem;
  color: var(--text-dim);
}

/* Body */
.page-body { padding: 40px 0 80px; }

/* Filters */
.filter-bar {
  display: flex;
  gap: 8px;
  margin-bottom: 36px;
  flex-wrap: wrap;
}

.filter-btn {
  padding: 8px 20px;
  border-radius: 99px;
  border: 1px solid var(--border-light);
  background: transparent;
  color: var(--text-dim);
  font-family: 'Rajdhani', sans-serif;
  font-size: 0.9rem;
  font-weight: 600;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  cursor: pointer;
  transition: var(--transition);
}

.filter-btn:hover { border-color: var(--accent); color: var(--accent); }
.filter-btn.active { background: var(--accent); color: #080e1a; border-color: var(--accent); }

/* Grid */
.courts-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

/* Court card */
.court-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  overflow: hidden;
  transition: transform 0.25s ease, box-shadow 0.25s ease, border-color 0.25s ease;
}

.court-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: var(--border-light);
}

/* Court visual */
.court-visual {
  height: 140px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.visual-indoor  { background: linear-gradient(135deg, #0d1e38, #102040); color: var(--sky); }
.visual-outdoor { background: linear-gradient(135deg, #0d2010, #102018); color: var(--accent); }

.court-lines-mini {
  position: absolute;
  inset: 0;
  padding: 8px;
}

.type-badge {
  position: absolute;
  top: 12px; right: 12px;
  z-index: 1;
}

/* Card body */
.card-body { padding: 20px; }

.card-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 8px;
  margin-bottom: 8px;
}

.court-name {
  font-family: 'Rajdhani', sans-serif;
  font-size: 1.2rem;
  font-weight: 700;
  letter-spacing: 0.03em;
  text-transform: uppercase;
  color: var(--text);
}

.court-location {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 0.82rem;
  color: var(--text-muted);
  margin-bottom: 10px;
}

.court-desc {
  font-size: 0.83rem;
  color: var(--text-dim);
  line-height: 1.6;
  margin-bottom: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding-top: 14px;
  border-top: 1px solid var(--border);
}

.price { display: flex; align-items: baseline; gap: 3px; }

.price-num {
  font-family: 'Rajdhani', sans-serif;
  font-size: 1.3rem;
  font-weight: 700;
  color: var(--accent);
}

.price-unit { font-size: 0.78rem; color: var(--text-muted); }

/* Skeleton */
.skeleton-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  overflow: hidden;
  animation: pulse 1.6s ease-in-out infinite;
}

.skel-img {
  height: 140px;
  background: var(--card-hover);
}

.skel-body { padding: 20px; display: flex; flex-direction: column; gap: 10px; }

.skel-line {
  height: 12px;
  background: var(--border-light);
  border-radius: 6px;
}

.skel-w60 { width: 60%; }
.skel-w40 { width: 40%; }
.skel-w80 { width: 80%; }

@keyframes pulse {
  0%, 100% { opacity: 0.6; }
  50%       { opacity: 1; }
}

/* Empty/error */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
  padding: 80px 20px;
  color: var(--text-muted);
  text-align: center;
  font-size: 0.9rem;
}

/* Responsive */
@media (max-width: 1024px) {
  .courts-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 600px) {
  .courts-grid { grid-template-columns: 1fr; }
}
</style>
