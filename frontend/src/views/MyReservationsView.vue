<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { getReservations, cancelReservation } from '../api/reservations'

const router = useRouter()
const authStore = useAuthStore()

const reservations = ref([])
const loading = ref(true)
const error = ref('')
const cancellingId = ref(null)

const STATUS_MAP = {
  PENDING:   { text: '待確認', cls: 'badge-yellow' },
  CONFIRMED: { text: '已確認', cls: 'badge-green' },
  CANCELLED: { text: '已取消', cls: 'badge-gray' },
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('zh-TW', { year: 'numeric', month: '2-digit', day: '2-digit' })
}

function formatTime(timeStr) {
  if (!timeStr) return ''
  return timeStr.substring(0, 5)
}

async function fetchReservations() {
  if (!authStore.userId) return
  loading.value = true
  error.value = ''
  try {
    const data = await getReservations(authStore.userId, { limit: 50, offset: 0 })
    reservations.value = data.results || data
  } catch {
    error.value = '無法載入預約記錄，請稍後再試。'
  } finally {
    loading.value = false
  }
}

async function handleCancel(reservation) {
  if (!confirm(`確定要取消「${reservation.courtName}」的預約嗎？`)) return
  cancellingId.value = reservation.reservationId
  try {
    await cancelReservation(authStore.userId, reservation.reservationId)
    await fetchReservations()
  } catch {
    alert('取消失敗，請稍後再試。')
  } finally {
    cancellingId.value = null
  }
}

onMounted(fetchReservations)
</script>

<template>
  <div class="reservations-page">

    <!-- Page header -->
    <div class="page-header">
      <div class="header-bg"><div class="header-orb"></div></div>
      <div class="container header-content">
        <p class="section-label">會員專區</p>
        <h1 class="page-title">我的預約</h1>
        <p class="page-sub">{{ authStore.user?.email }}</p>
      </div>
    </div>

    <div class="container page-body">

      <!-- Loading -->
      <div v-if="loading" class="res-list">
        <div v-for="i in 4" :key="i" class="skeleton-row">
          <div class="skel-left">
            <div class="skel-line skel-w40"></div>
            <div class="skel-line skel-w60"></div>
          </div>
          <div class="skel-right">
            <div class="skel-pill"></div>
          </div>
        </div>
      </div>

      <!-- Error -->
      <div v-else-if="error" class="empty-state">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/>
        </svg>
        <p>{{ error }}</p>
        <button class="btn btn-ghost btn-sm" @click="fetchReservations">重新載入</button>
      </div>

      <!-- Reservation list -->
      <div v-else-if="reservations.length" class="res-list">
        <div
          v-for="res in reservations"
          :key="res.reservationId"
          :class="['res-card', { cancelled: res.status === 'CANCELLED' }]"
        >
          <!-- Left: court visual accent -->
          <div class="res-accent" :class="res.status === 'CANCELLED' ? 'accent-gray' : 'accent-lime'"></div>

          <!-- Main info -->
          <div class="res-main">
            <div class="res-top">
              <h3 class="res-court">{{ res.courtName || `場地 #${res.courtId}` }}</h3>
              <span :class="['badge', STATUS_MAP[res.status]?.cls]">
                {{ STATUS_MAP[res.status]?.text || res.status }}
              </span>
            </div>
            <div class="res-meta">
              <span class="meta-item">
                <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="3" y="4" width="18" height="18" rx="2"/><line x1="16" y1="2" x2="16" y2="6"/>
                  <line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/>
                </svg>
                {{ formatDate(res.reservationDate) }}
              </span>
              <span class="meta-item">
                <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="12" cy="12" r="10"/><polyline points="12,6 12,12 16,14"/>
                </svg>
                {{ formatTime(res.startTime) }} – {{ formatTime(res.endTime) }}
              </span>
              <span v-if="res.location" class="meta-item">
                <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M21 10c0 7-9 13-9 13S3 17 3 10a9 9 0 0118 0z"/><circle cx="12" cy="10" r="3"/>
                </svg>
                {{ res.location }}
              </span>
            </div>
          </div>

          <!-- Right: price + actions -->
          <div class="res-right">
            <div class="res-price">
              <span class="price-num">NT$ {{ res.totalPrice?.toLocaleString() }}</span>
            </div>
            <button
              v-if="res.status !== 'CANCELLED'"
              class="btn btn-danger btn-sm"
              :disabled="cancellingId === res.reservationId"
              @click="handleCancel(res)"
            >
              <span v-if="cancellingId === res.reservationId">
                <span class="mini-spinner"></span>
              </span>
              <span v-else>取消預約</span>
            </button>
          </div>
        </div>
      </div>

      <!-- Empty state -->
      <div v-else class="empty-state">
        <svg width="56" height="56" viewBox="0 0 28 28" fill="none">
          <circle cx="14" cy="14" r="13" stroke="var(--border-light)" stroke-width="1.5"/>
          <circle cx="9"  cy="10" r="1.6" fill="var(--border-light)"/>
          <circle cx="14" cy="8"  r="1.6" fill="var(--border-light)"/>
          <circle cx="19" cy="10" r="1.6" fill="var(--border-light)"/>
          <circle cx="7"  cy="15" r="1.6" fill="var(--border-light)"/>
          <circle cx="12" cy="14" r="1.6" fill="var(--border-light)"/>
          <circle cx="17" cy="14" r="1.6" fill="var(--border-light)"/>
          <circle cx="21" cy="15" r="1.6" fill="var(--border-light)"/>
          <circle cx="9"  cy="19" r="1.6" fill="var(--border-light)"/>
          <circle cx="14" cy="20" r="1.6" fill="var(--border-light)"/>
          <circle cx="19" cy="19" r="1.6" fill="var(--border-light)"/>
        </svg>
        <p>你還沒有任何預約記錄</p>
        <button class="btn btn-primary" @click="router.push('/courts')">立即瀏覽場地</button>
      </div>

    </div>
  </div>
</template>

<style scoped>
.page-header {
  position: relative;
  padding: calc(var(--navbar-h) + 48px) 0 48px;
  background: var(--surface);
  border-bottom: 1px solid var(--border);
  overflow: hidden;
}

.header-bg { position: absolute; inset: 0; pointer-events: none; }

.header-orb {
  position: absolute;
  right: -80px; top: -80px;
  width: 350px; height: 350px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(132,204,22,0.07), transparent 70%);
}

.header-content { position: relative; z-index: 1; }

.page-title {
  font-family: 'Rajdhani', sans-serif;
  font-size: clamp(2.5rem, 5vw, 4rem);
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  margin-bottom: 6px;
}

.page-sub { font-size: 0.88rem; color: var(--text-dim); }

.page-body { padding: 40px 0 80px; }

/* List */
.res-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-width: 860px;
}

/* Reservation card */
.res-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  gap: 0;
  overflow: hidden;
  transition: border-color 0.2s ease, background 0.2s ease;
}

.res-card:hover { border-color: var(--border-light); background: var(--card-hover); }
.res-card.cancelled { opacity: 0.55; }

.res-accent {
  width: 4px;
  align-self: stretch;
  flex-shrink: 0;
}

.accent-lime { background: var(--accent); }
.accent-gray { background: var(--text-muted); }

.res-main {
  flex: 1;
  padding: 20px 24px;
  min-width: 0;
}

.res-top {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

.res-court {
  font-family: 'Rajdhani', sans-serif;
  font-size: 1.15rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--text);
}

.res-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 0.82rem;
  color: var(--text-dim);
}

.res-right {
  padding: 20px 24px;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 12px;
  flex-shrink: 0;
}

.res-price {}

.price-num {
  font-family: 'Rajdhani', sans-serif;
  font-size: 1.3rem;
  font-weight: 700;
  color: var(--accent);
}

.mini-spinner {
  display: inline-block;
  width: 12px; height: 12px;
  border: 2px solid currentColor;
  border-top-color: transparent;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

@keyframes spin { to { transform: rotate(360deg); } }

/* Skeleton */
.skeleton-row {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  animation: pulse 1.6s ease-in-out infinite;
}

.skel-left { display: flex; flex-direction: column; gap: 10px; flex: 1; }
.skel-right { flex-shrink: 0; }

.skel-line {
  height: 12px;
  background: var(--border-light);
  border-radius: 6px;
}

.skel-w40 { width: 40%; }
.skel-w60 { width: 60%; }

.skel-pill {
  width: 80px; height: 26px;
  background: var(--border-light);
  border-radius: 99px;
}

@keyframes pulse {
  0%, 100% { opacity: 0.6; }
  50%       { opacity: 1; }
}

/* Empty */
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

@media (max-width: 600px) {
  .res-card { flex-wrap: wrap; }
  .res-right { padding-top: 0; padding-left: 28px; align-items: flex-start; width: 100%; border-top: 1px solid var(--border); }
}
</style>
