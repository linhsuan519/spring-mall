<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { getMyHosted, getMyJoined, cancelActivity, leaveActivity } from '../api/activities'

const router = useRouter()
const authStore = useAuthStore()

const hosted = ref([])
const joined = ref([])
const loading = ref(true)
const activeTab = ref('joined')

const skillLabels = { BEGINNER: '新手', INTERMEDIATE: '中級', ADVANCED: '高手' }
const statusLabels = { OPEN: '開放中', FULL: '已滿', CANCELLED: '已取消', COMPLETED: '已結束' }
const participantStatusLabels = { APPROVED: '已確認', PENDING: '待審核', REJECTED: '已拒絕' }

async function load() {
  loading.value = true
  try {
    const [h, j] = await Promise.all([getMyHosted(), getMyJoined()])
    hosted.value = h
    joined.value = j
  } finally {
    loading.value = false
  }
}

async function handleCancel(activityId) {
  if (!confirm('確定要取消這場活動嗎？')) return
  try {
    await cancelActivity(activityId)
    await load()
  } catch (e) {
    alert(e.response?.data?.message || '操作失敗')
  }
}

async function handleLeave(activityId) {
  if (!confirm('確定要退出這場活動嗎？')) return
  try {
    await leaveActivity(activityId)
    await load()
  } catch (e) {
    alert(e.response?.data?.message || '操作失敗')
  }
}

function formatTime(t) {
  return new Date(t).toLocaleString('zh-TW', { month: 'numeric', day: 'numeric', hour: '2-digit', minute: '2-digit' })
}

onMounted(load)
</script>

<template>
  <div class="my-page">
    <div class="container">
      <div class="page-header">
        <div>
          <p class="section-label">個人</p>
          <h1 class="page-title">我的揪球</h1>
        </div>
        <button class="btn btn-primary" @click="router.push('/create-activity')">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
          發起揪球
        </button>
      </div>

      <div class="tabs">
        <button :class="['tab', { active: activeTab === 'joined' }]" @click="activeTab = 'joined'">
          我參加的
          <span class="tab-count">{{ joined.length }}</span>
        </button>
        <button :class="['tab', { active: activeTab === 'hosted' }]" @click="activeTab = 'hosted'">
          我發起的
          <span class="tab-count">{{ hosted.length }}</span>
        </button>
      </div>

      <div v-if="loading" class="loading-wrap"><div class="spinner"></div></div>

      <!-- Joined activities -->
      <div v-else-if="activeTab === 'joined'">
        <div v-if="joined.length === 0" class="empty-state">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/></svg>
          <p>還沒有參加任何活動</p>
          <button class="btn btn-primary" @click="router.push('/activities')">瀏覽活動</button>
        </div>
        <div v-else class="activity-list">
          <div v-for="p in joined" :key="p.participantId" class="list-item" @click="router.push(`/activities/${p.activityId}`)">
            <div class="list-main">
              <div class="list-meta">
                <span class="activity-id-badge">活動 #{{ p.activityId }}</span>
                <span :class="['p-status', p.status.toLowerCase()]">{{ participantStatusLabels[p.status] }}</span>
              </div>
              <p class="joined-at">報名時間：{{ formatTime(p.joinedAt) }}</p>
            </div>
            <div class="list-actions">
              <button class="btn btn-ghost btn-sm" @click.stop="handleLeave(p.activityId)">退出</button>
            </div>
          </div>
        </div>
      </div>

      <!-- Hosted activities -->
      <div v-else>
        <div v-if="hosted.length === 0" class="empty-state">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><circle cx="12" cy="12" r="9"/><line x1="12" y1="8" x2="12" y2="12"/><circle cx="12" cy="16" r="0.5" fill="currentColor"/></svg>
          <p>還沒有發起任何活動</p>
          <button class="btn btn-primary" @click="router.push('/create-activity')">發起第一場揪球</button>
        </div>
        <div v-else class="activity-list">
          <div v-for="a in hosted" :key="a.activityId" class="list-item" @click="router.push(`/activities/${a.activityId}`)">
            <div class="list-main">
              <div class="list-meta-top">
                <span :class="['skill-badge', `skill-${a.skillLevel.toLowerCase()}`]">{{ skillLabels[a.skillLevel] }}</span>
                <span class="status-tag">{{ statusLabels[a.status] }}</span>
              </div>
              <h3 class="list-title">{{ a.title }}</h3>
              <div class="list-info">
                <span>{{ a.location }}</span>
                <span>{{ formatTime(a.scheduledTime) }}</span>
                <span>{{ a.currentParticipants }}/{{ a.maxParticipants }} 人</span>
              </div>
            </div>
            <div class="list-actions" v-if="a.status === 'OPEN' || a.status === 'FULL'">
              <button class="btn btn-danger btn-sm" @click.stop="handleCancel(a.activityId)">取消活動</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.my-page { padding: 80px 0; min-height: 80vh; }
.page-header { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 36px; }
.page-title { font-family: 'Rajdhani', sans-serif; font-size: 2.8rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.04em; }

.tabs { display: flex; gap: 4px; margin-bottom: 32px; border-bottom: 1px solid var(--border); }
.tab { padding: 10px 20px; background: none; border: none; border-bottom: 2px solid transparent; color: var(--text-muted); font-size: 0.95rem; cursor: pointer; display: flex; align-items: center; gap: 8px; transition: all 0.2s; margin-bottom: -1px; }
.tab:hover { color: var(--text); }
.tab.active { color: var(--accent); border-bottom-color: var(--accent); }
.tab-count { font-size: 0.75rem; background: var(--surface); border: 1px solid var(--border); border-radius: 99px; padding: 1px 8px; }

.loading-wrap { display: flex; justify-content: center; padding: 80px; }
.spinner { width: 36px; height: 36px; border: 3px solid var(--border); border-top-color: var(--accent); border-radius: 50%; animation: spin 0.8s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }

.empty-state { display: flex; flex-direction: column; align-items: center; gap: 16px; padding: 80px 0; color: var(--text-muted); }

.activity-list { display: flex; flex-direction: column; gap: 12px; }
.list-item {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  transition: border-color 0.2s, background 0.2s;
  gap: 16px;
}
.list-item:hover { border-color: var(--accent); background: var(--card-hover); }

.list-main { flex: 1; display: flex; flex-direction: column; gap: 8px; }
.list-meta { display: flex; align-items: center; gap: 8px; }
.list-meta-top { display: flex; align-items: center; gap: 8px; }
.activity-id-badge { font-size: 0.78rem; color: var(--text-muted); font-family: monospace; }
.p-status { font-size: 0.72rem; font-weight: 700; padding: 2px 8px; border-radius: 99px; }
.p-status.approved { background: rgba(132,204,22,0.12); color: var(--accent); }
.p-status.pending { background: rgba(234,179,8,0.1); color: #eab308; }
.p-status.rejected { background: rgba(239,68,68,0.1); color: #ef4444; }

.joined-at { font-size: 0.84rem; color: var(--text-muted); }
.list-title { font-family: 'Rajdhani', sans-serif; font-size: 1.2rem; font-weight: 700; color: var(--text); }
.list-info { display: flex; gap: 16px; font-size: 0.84rem; color: var(--text-dim); flex-wrap: wrap; }
.status-tag { font-size: 0.72rem; font-weight: 600; padding: 2px 8px; border-radius: 99px; background: rgba(132,204,22,0.12); color: var(--accent); }

.skill-badge { font-size: 0.68rem; font-weight: 700; padding: 2px 8px; border-radius: 99px; text-transform: uppercase; }
.skill-beginner { background: rgba(34,197,94,0.15); color: #22c55e; }
.skill-intermediate { background: rgba(234,179,8,0.15); color: #eab308; }
.skill-advanced { background: rgba(239,68,68,0.15); color: #ef4444; }

.list-actions { flex-shrink: 0; }
.btn-sm { padding: 6px 14px; font-size: 0.84rem; }
.btn-danger { background: rgba(239,68,68,0.12); color: #ef4444; border: 1px solid rgba(239,68,68,0.25); }
.btn-danger:hover { background: rgba(239,68,68,0.2); }

@media (max-width: 600px) {
  .list-item { flex-direction: column; align-items: flex-start; }
  .page-header { flex-direction: column; align-items: flex-start; gap: 16px; }
}
</style>
