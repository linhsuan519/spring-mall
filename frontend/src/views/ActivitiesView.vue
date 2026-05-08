<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { getActivities, joinActivity, leaveActivity } from '../api/activities'

const router = useRouter()
const authStore = useAuthStore()

const activities = ref([])
const total = ref(0)
const loading = ref(false)
const error = ref('')

const filters = ref({ skillLevel: '', status: 'OPEN' })
const limit = 12
const offset = ref(0)

const skillLabels = { BEGINNER: '新手', INTERMEDIATE: '中級', ADVANCED: '高手' }
const statusLabels = { OPEN: '開放中', FULL: '已滿', CANCELLED: '已取消', COMPLETED: '已結束' }
const statusClass = { OPEN: 'status-open', FULL: 'status-full', CANCELLED: 'status-cancelled', COMPLETED: 'status-completed' }

async function load() {
  loading.value = true
  error.value = ''
  try {
    const params = { limit, offset: offset.value }
    if (filters.value.skillLevel) params.skillLevel = filters.value.skillLevel
    if (filters.value.status) params.status = filters.value.status
    const data = await getActivities(params)
    activities.value = data.results
    total.value = data.total
  } catch {
    error.value = '載入失敗，請稍後再試。'
  } finally {
    loading.value = false
  }
}

async function handleJoin(activityId) {
  if (!authStore.isAuthenticated) { router.push('/login'); return }
  try {
    await joinActivity(activityId)
    await load()
  } catch (e) {
    alert(e.response?.data?.message || '報名失敗')
  }
}

async function handleLeave(activityId) {
  try {
    await leaveActivity(activityId)
    await load()
  } catch (e) {
    alert(e.response?.data?.message || '退出失敗')
  }
}

function formatTime(t) {
  return new Date(t).toLocaleString('zh-TW', { month: 'numeric', day: 'numeric', hour: '2-digit', minute: '2-digit' })
}

function isFull(a) { return a.currentParticipants >= a.maxParticipants }

watch(filters, () => { offset.value = 0; load() }, { deep: true })
onMounted(load)
</script>

<template>
  <div class="activities-page">
    <div class="container">
      <div class="page-header">
        <div>
          <p class="section-label">找球友</p>
          <h1 class="page-title">揪球活動</h1>
        </div>
        <button v-if="authStore.isAuthenticated" class="btn btn-primary" @click="router.push('/create-activity')">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
          發起揪球
        </button>
      </div>

      <!-- Filters -->
      <div class="filters">
        <div class="filter-group">
          <label class="filter-label">程度</label>
          <div class="filter-btns">
            <button :class="['filter-btn', { active: filters.skillLevel === '' }]" @click="filters.skillLevel = ''">全部</button>
            <button :class="['filter-btn', { active: filters.skillLevel === 'BEGINNER' }]" @click="filters.skillLevel = 'BEGINNER'">新手</button>
            <button :class="['filter-btn', { active: filters.skillLevel === 'INTERMEDIATE' }]" @click="filters.skillLevel = 'INTERMEDIATE'">中級</button>
            <button :class="['filter-btn', { active: filters.skillLevel === 'ADVANCED' }]" @click="filters.skillLevel = 'ADVANCED'">高手</button>
          </div>
        </div>
        <div class="filter-group">
          <label class="filter-label">狀態</label>
          <div class="filter-btns">
            <button :class="['filter-btn', { active: filters.status === '' }]" @click="filters.status = ''">全部</button>
            <button :class="['filter-btn', { active: filters.status === 'OPEN' }]" @click="filters.status = 'OPEN'">開放中</button>
            <button :class="['filter-btn', { active: filters.status === 'FULL' }]" @click="filters.status = 'FULL'">已滿</button>
          </div>
        </div>
      </div>

      <p v-if="error" class="error-msg">{{ error }}</p>

      <div v-if="loading" class="loading-wrap">
        <div class="spinner"></div>
      </div>

      <div v-else-if="activities.length === 0" class="empty-state">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><circle cx="12" cy="12" r="9"/><line x1="12" y1="8" x2="12" y2="12"/><circle cx="12" cy="16" r="0.5" fill="currentColor"/></svg>
        <p>目前沒有符合條件的活動</p>
        <button v-if="authStore.isAuthenticated" class="btn btn-primary" @click="router.push('/create-activity')">發起第一場揪球</button>
      </div>

      <div v-else class="activities-grid">
        <div v-for="a in activities" :key="a.activityId" class="activity-card" @click="router.push(`/activities/${a.activityId}`)">
          <div class="card-top">
            <span :class="['skill-badge', `skill-${a.skillLevel.toLowerCase()}`]">{{ skillLabels[a.skillLevel] }}</span>
            <span :class="['status-badge', statusClass[a.status]]">{{ statusLabels[a.status] }}</span>
          </div>
          <h3 class="card-title">{{ a.title }}</h3>
          <div class="card-meta">
            <div class="meta-item">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 10c0 7-9 13-9 13S3 17 3 10a9 9 0 0118 0z"/><circle cx="12" cy="10" r="3"/></svg>
              {{ a.location }}
            </div>
            <div class="meta-item">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2"/><line x1="3" y1="10" x2="21" y2="10"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="16" y1="2" x2="16" y2="6"/></svg>
              {{ formatTime(a.scheduledTime) }}
            </div>
            <div class="meta-item">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg>
              {{ a.currentParticipants }} / {{ a.maxParticipants }} 人
            </div>
          </div>
          <div class="participants-bar">
            <div class="bar-fill" :style="{ width: (a.currentParticipants / a.maxParticipants * 100) + '%' }"></div>
          </div>
          <p class="card-desc">{{ a.description || '主揪沒有留下說明' }}</p>
          <div class="card-footer">
            <span class="host-info">by {{ a.hostEmail }}</span>
            <button
              v-if="authStore.isAuthenticated && a.hostUserId !== authStore.userId"
              :class="['btn', 'btn-sm', isFull(a) ? 'btn-ghost' : 'btn-primary']"
              :disabled="isFull(a) || a.status !== 'OPEN'"
              @click.stop="handleJoin(a.activityId)"
            >
              {{ isFull(a) ? '已滿' : '報名' }}
            </button>
          </div>
        </div>
      </div>

      <!-- Pagination -->
      <div v-if="total > limit" class="pagination">
        <button class="btn btn-ghost" :disabled="offset === 0" @click="offset -= limit; load()">上一頁</button>
        <span class="page-info">{{ Math.floor(offset / limit) + 1 }} / {{ Math.ceil(total / limit) }}</span>
        <button class="btn btn-ghost" :disabled="offset + limit >= total" @click="offset += limit; load()">下一頁</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.activities-page { padding: 80px 0; min-height: 80vh; }
.page-header { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 36px; }
.page-title { font-family: 'Rajdhani', sans-serif; font-size: 2.8rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.04em; }

.filters { display: flex; gap: 32px; margin-bottom: 32px; flex-wrap: wrap; }
.filter-group { display: flex; align-items: center; gap: 12px; }
.filter-label { font-size: 0.8rem; color: var(--text-muted); font-weight: 600; letter-spacing: 0.08em; text-transform: uppercase; white-space: nowrap; }
.filter-btns { display: flex; gap: 6px; }
.filter-btn { padding: 6px 14px; border-radius: 99px; border: 1px solid var(--border); background: transparent; color: var(--text-dim); font-size: 0.85rem; cursor: pointer; transition: all 0.2s; }
.filter-btn:hover { border-color: var(--accent); color: var(--accent); }
.filter-btn.active { background: var(--accent); border-color: var(--accent); color: #000; font-weight: 600; }

.error-msg { color: #f87171; text-align: center; padding: 24px; }
.loading-wrap { display: flex; justify-content: center; padding: 80px; }
.spinner { width: 36px; height: 36px; border: 3px solid var(--border); border-top-color: var(--accent); border-radius: 50%; animation: spin 0.8s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }

.empty-state { display: flex; flex-direction: column; align-items: center; gap: 16px; padding: 80px 0; color: var(--text-muted); }

.activities-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 20px; }

.activity-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 24px;
  cursor: pointer;
  transition: border-color 0.2s, transform 0.2s, box-shadow 0.2s;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.activity-card:hover { border-color: var(--accent); transform: translateY(-2px); box-shadow: 0 8px 32px rgba(0,0,0,0.4); }

.card-top { display: flex; gap: 8px; }
.skill-badge { font-size: 0.72rem; font-weight: 700; padding: 3px 10px; border-radius: 99px; letter-spacing: 0.06em; text-transform: uppercase; }
.skill-beginner { background: rgba(34,197,94,0.15); color: #22c55e; }
.skill-intermediate { background: rgba(234,179,8,0.15); color: #eab308; }
.skill-advanced { background: rgba(239,68,68,0.15); color: #ef4444; }

.status-badge { font-size: 0.72rem; font-weight: 600; padding: 3px 10px; border-radius: 99px; }
.status-open { background: rgba(132,204,22,0.12); color: var(--accent); }
.status-full { background: rgba(14,165,233,0.12); color: var(--sky); }
.status-cancelled { background: rgba(239,68,68,0.1); color: #ef4444; }
.status-completed { background: rgba(100,116,139,0.15); color: #64748b; }

.card-title { font-family: 'Rajdhani', sans-serif; font-size: 1.3rem; font-weight: 700; letter-spacing: 0.03em; color: var(--text); margin: 0; }
.card-meta { display: flex; flex-direction: column; gap: 6px; }
.meta-item { display: flex; align-items: center; gap: 6px; font-size: 0.84rem; color: var(--text-dim); }

.participants-bar { height: 4px; background: var(--border); border-radius: 99px; overflow: hidden; }
.bar-fill { height: 100%; background: var(--accent); border-radius: 99px; transition: width 0.3s; }

.card-desc { font-size: 0.84rem; color: var(--text-muted); line-height: 1.6; flex: 1; overflow: hidden; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; }
.card-footer { display: flex; align-items: center; justify-content: space-between; margin-top: 4px; }
.host-info { font-size: 0.78rem; color: var(--text-muted); }
.btn-sm { padding: 6px 16px; font-size: 0.84rem; }

.pagination { display: flex; align-items: center; justify-content: center; gap: 20px; margin-top: 48px; }
.page-info { font-size: 0.9rem; color: var(--text-dim); }

@media (max-width: 600px) {
  .activities-grid { grid-template-columns: 1fr; }
  .filters { flex-direction: column; gap: 16px; }
  .page-header { flex-direction: column; align-items: flex-start; gap: 16px; }
}
</style>
