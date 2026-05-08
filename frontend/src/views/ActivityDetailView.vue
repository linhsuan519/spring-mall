<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { getActivityById, joinActivity, leaveActivity, cancelActivity, getParticipants } from '../api/activities'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const activity = ref(null)
const participants = ref([])
const loading = ref(true)
const loadingAction = ref(false)
const error = ref('')

const activityId = Number(route.params.id)

const isHost = computed(() => authStore.userId && activity.value?.hostUserId === authStore.userId)
const isParticipant = computed(() => participants.value.some(p => p.userId === authStore.userId))
const isFull = computed(() => activity.value && activity.value.currentParticipants >= activity.value.maxParticipants)

const skillLabels = { BEGINNER: '新手', INTERMEDIATE: '中級', ADVANCED: '高手' }
const statusLabels = { OPEN: '開放中', FULL: '已滿', CANCELLED: '已取消', COMPLETED: '已結束' }

async function load() {
  try {
    activity.value = await getActivityById(activityId)
    if (authStore.isAuthenticated && isHost.value) {
      participants.value = await getParticipants(activityId)
    } else if (authStore.isAuthenticated) {
      // fetch to check if current user is participant
      try { participants.value = await getParticipants(activityId) } catch {}
    }
  } catch {
    error.value = '無法載入活動資訊'
  } finally {
    loading.value = false
  }
}

async function handleJoin() {
  if (!authStore.isAuthenticated) { router.push('/login'); return }
  loadingAction.value = true
  try {
    await joinActivity(activityId)
    await load()
  } catch (e) {
    alert(e.response?.data?.message || '報名失敗')
  } finally {
    loadingAction.value = false
  }
}

async function handleLeave() {
  loadingAction.value = true
  try {
    await leaveActivity(activityId)
    await load()
  } catch (e) {
    alert(e.response?.data?.message || '退出失敗')
  } finally {
    loadingAction.value = false
  }
}

async function handleCancel() {
  if (!confirm('確定要取消這場活動嗎？')) return
  loadingAction.value = true
  try {
    await cancelActivity(activityId)
    await load()
  } catch (e) {
    alert(e.response?.data?.message || '取消失敗')
  } finally {
    loadingAction.value = false
  }
}

function formatTime(t) {
  return new Date(t).toLocaleString('zh-TW', { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' })
}

onMounted(load)
</script>

<template>
  <div class="detail-page">
    <div class="container">
      <button class="back-btn" @click="router.back()">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="19" y1="12" x2="5" y2="12"/><polyline points="12,19 5,12 12,5"/></svg>
        返回
      </button>

      <div v-if="loading" class="loading-wrap"><div class="spinner"></div></div>
      <p v-else-if="error" class="error-msg">{{ error }}</p>

      <div v-else-if="activity" class="detail-layout">
        <!-- Main content -->
        <div class="detail-main">
          <div class="badges">
            <span :class="['skill-badge', `skill-${activity.skillLevel.toLowerCase()}`]">{{ skillLabels[activity.skillLevel] }}</span>
            <span class="status-badge">{{ statusLabels[activity.status] }}</span>
          </div>
          <h1 class="detail-title">{{ activity.title }}</h1>

          <div class="info-grid">
            <div class="info-item">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 10c0 7-9 13-9 13S3 17 3 10a9 9 0 0118 0z"/><circle cx="12" cy="10" r="3"/></svg>
              <div><p class="info-label">地點</p><p class="info-val">{{ activity.location }}</p></div>
            </div>
            <div class="info-item">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2"/><line x1="3" y1="10" x2="21" y2="10"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="16" y1="2" x2="16" y2="6"/></svg>
              <div><p class="info-label">時間</p><p class="info-val">{{ formatTime(activity.scheduledTime) }}</p></div>
            </div>
            <div class="info-item">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/></svg>
              <div><p class="info-label">主揪</p><p class="info-val">{{ activity.hostEmail }}</p></div>
            </div>
            <div class="info-item">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg>
              <div><p class="info-label">人數</p><p class="info-val">{{ activity.currentParticipants }} / {{ activity.maxParticipants }} 人</p></div>
            </div>
          </div>

          <div class="participants-bar">
            <div class="bar-fill" :style="{ width: (activity.currentParticipants / activity.maxParticipants * 100) + '%' }"></div>
          </div>

          <div v-if="activity.description" class="description">
            <h3 class="desc-title">活動說明</h3>
            <p class="desc-text">{{ activity.description }}</p>
          </div>
        </div>

        <!-- Sidebar -->
        <div class="detail-sidebar">
          <div class="action-card">
            <div class="action-status" :class="activity.status.toLowerCase()">
              {{ statusLabels[activity.status] }}
            </div>
            <div class="action-slots">
              <span class="slots-num">{{ activity.maxParticipants - activity.currentParticipants }}</span>
              <span class="slots-label">剩餘名額</span>
            </div>

            <div v-if="activity.status === 'OPEN' || activity.status === 'FULL'">
              <div v-if="isHost">
                <button class="btn btn-danger w-full" :disabled="loadingAction" @click="handleCancel">取消活動</button>
              </div>
              <div v-else-if="authStore.isAuthenticated">
                <button v-if="!isParticipant" class="btn btn-primary w-full" :disabled="loadingAction || isFull" @click="handleJoin">
                  {{ isFull ? '名額已滿' : '立即報名' }}
                </button>
                <button v-else class="btn btn-ghost w-full" :disabled="loadingAction" @click="handleLeave">退出活動</button>
              </div>
              <div v-else>
                <button class="btn btn-primary w-full" @click="router.push('/login')">登入後報名</button>
              </div>
            </div>

            <div v-if="activity.status === 'CANCELLED'" class="cancelled-notice">此活動已取消</div>
            <div v-if="activity.status === 'COMPLETED'" class="cancelled-notice">此活動已結束</div>
          </div>

          <!-- Participants list (host only) -->
          <div v-if="isHost && participants.length > 0" class="participants-card">
            <h3 class="participants-title">參加名單</h3>
            <ul class="participants-list">
              <li v-for="p in participants" :key="p.participantId" class="participant-item">
                <div class="participant-avatar">{{ p.userEmail[0].toUpperCase() }}</div>
                <span class="participant-email">{{ p.userEmail }}</span>
                <span v-if="p.userId === activity.hostUserId" class="host-tag">主揪</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.detail-page { padding: 60px 0 80px; min-height: 80vh; }
.back-btn { display: inline-flex; align-items: center; gap: 6px; color: var(--text-muted); background: none; border: none; cursor: pointer; font-size: 0.9rem; margin-bottom: 32px; transition: color 0.2s; }
.back-btn:hover { color: var(--accent); }

.loading-wrap { display: flex; justify-content: center; padding: 80px; }
.spinner { width: 36px; height: 36px; border: 3px solid var(--border); border-top-color: var(--accent); border-radius: 50%; animation: spin 0.8s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }
.error-msg { color: #f87171; text-align: center; padding: 40px; }

.detail-layout { display: grid; grid-template-columns: 1fr 320px; gap: 40px; align-items: start; }

.badges { display: flex; gap: 8px; margin-bottom: 16px; }
.skill-badge { font-size: 0.72rem; font-weight: 700; padding: 4px 12px; border-radius: 99px; letter-spacing: 0.06em; text-transform: uppercase; }
.skill-beginner { background: rgba(34,197,94,0.15); color: #22c55e; }
.skill-intermediate { background: rgba(234,179,8,0.15); color: #eab308; }
.skill-advanced { background: rgba(239,68,68,0.15); color: #ef4444; }
.status-badge { font-size: 0.72rem; font-weight: 600; padding: 4px 12px; border-radius: 99px; background: rgba(132,204,22,0.12); color: var(--accent); }

.detail-title { font-family: 'Rajdhani', sans-serif; font-size: 2.5rem; font-weight: 700; letter-spacing: 0.04em; margin-bottom: 32px; color: var(--text); }

.info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 24px; }
.info-item { display: flex; gap: 12px; align-items: flex-start; color: var(--text-dim); }
.info-label { font-size: 0.72rem; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.08em; margin-bottom: 2px; }
.info-val { font-size: 0.95rem; color: var(--text); }

.participants-bar { height: 6px; background: var(--border); border-radius: 99px; overflow: hidden; margin-bottom: 32px; }
.bar-fill { height: 100%; background: var(--accent); border-radius: 99px; }

.description { border-top: 1px solid var(--border); padding-top: 24px; }
.desc-title { font-family: 'Rajdhani', sans-serif; font-size: 1.1rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.06em; margin-bottom: 12px; color: var(--text); }
.desc-text { font-size: 0.95rem; color: var(--text-dim); line-height: 1.8; }

.action-card { background: var(--card); border: 1px solid var(--border-light); border-radius: var(--radius-lg); padding: 24px; display: flex; flex-direction: column; gap: 20px; }
.action-status { font-size: 0.8rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.1em; padding: 6px 14px; border-radius: 99px; width: fit-content; background: rgba(132,204,22,0.12); color: var(--accent); }
.action-status.cancelled { background: rgba(239,68,68,0.1); color: #ef4444; }
.action-status.full { background: rgba(14,165,233,0.12); color: var(--sky); }
.action-status.completed { background: rgba(100,116,139,0.15); color: #64748b; }

.action-slots { display: flex; align-items: baseline; gap: 8px; }
.slots-num { font-family: 'Rajdhani', sans-serif; font-size: 3rem; font-weight: 700; color: var(--accent); line-height: 1; }
.slots-label { font-size: 0.85rem; color: var(--text-dim); }

.w-full { width: 100%; justify-content: center; }
.btn-danger { background: rgba(239,68,68,0.15); color: #ef4444; border: 1px solid rgba(239,68,68,0.3); }
.btn-danger:hover { background: rgba(239,68,68,0.25); }
.cancelled-notice { text-align: center; color: var(--text-muted); font-size: 0.9rem; }

.participants-card { background: var(--card); border: 1px solid var(--border); border-radius: var(--radius-lg); padding: 20px; margin-top: 16px; }
.participants-title { font-family: 'Rajdhani', sans-serif; font-size: 1rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.08em; margin-bottom: 16px; }
.participants-list { display: flex; flex-direction: column; gap: 10px; list-style: none; padding: 0; margin: 0; }
.participant-item { display: flex; align-items: center; gap: 10px; }
.participant-avatar { width: 30px; height: 30px; border-radius: 50%; background: var(--accent-dim); color: var(--accent); font-size: 0.8rem; font-weight: 700; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.participant-email { font-size: 0.85rem; color: var(--text-dim); flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.host-tag { font-size: 0.68rem; font-weight: 700; padding: 2px 8px; border-radius: 99px; background: rgba(132,204,22,0.15); color: var(--accent); white-space: nowrap; }

@media (max-width: 900px) {
  .detail-layout { grid-template-columns: 1fr; }
  .info-grid { grid-template-columns: 1fr; }
}
</style>
