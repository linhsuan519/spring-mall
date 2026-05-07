<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { getCourts, createCourt, updateCourt, deleteCourt } from '../api/courts'

const courts = ref([])
const loading = ref(true)
const error = ref('')
const submitting = ref(false)
const deleteTarget = ref(null)
const searchTerm = ref('')

const modal = reactive({ open: false, mode: 'create', id: null })

const form = reactive({
  courtName: '',
  courtType: '',
  location: '',
  pricePerHour: '',
  status: 'AVAILABLE',
  description: '',
  imageUrl: '',
})

const formErrors = reactive({})

const COURT_TYPES  = [{ key: 'INDOOR', label: '室內' }, { key: 'OUTDOOR', label: '室外' }]
const COURT_STATUS = [
  { key: 'AVAILABLE',   label: '可預約',   color: '#84cc16' },
  { key: 'MAINTENANCE', label: '維護中',   color: '#fbbf24' },
  { key: 'CLOSED',      label: '暫停開放', color: '#64748b' },
]
const statusMap = Object.fromEntries(COURT_STATUS.map((s) => [s.key, s]))

const filteredCourts = computed(() => {
  const q = searchTerm.value.toLowerCase()
  if (!q) return courts.value
  return courts.value.filter(
    (c) => c.courtName.toLowerCase().includes(q) || c.location.toLowerCase().includes(q),
  )
})

async function fetchCourts() {
  loading.value = true
  error.value = ''
  try {
    const data = await getCourts({ limit: 100, offset: 0 })
    courts.value = data.results || data
  } catch {
    error.value = '無法載入場地資料'
  } finally {
    loading.value = false
  }
}

function openCreate() {
  modal.mode = 'create'
  modal.id = null
  Object.assign(form, { courtName: '', courtType: '', location: '', pricePerHour: '', status: 'AVAILABLE', description: '', imageUrl: '' })
  Object.keys(formErrors).forEach((k) => delete formErrors[k])
  modal.open = true
}

function openEdit(court) {
  modal.mode = 'edit'
  modal.id = court.courtId
  Object.assign(form, {
    courtName:    court.courtName,
    courtType:    court.courtType,
    location:     court.location,
    pricePerHour: String(court.pricePerHour),
    status:       court.status,
    description:  court.description || '',
    imageUrl:     court.imageUrl || '',
  })
  Object.keys(formErrors).forEach((k) => delete formErrors[k])
  modal.open = true
}

function validate() {
  Object.keys(formErrors).forEach((k) => delete formErrors[k])
  if (!form.courtName.trim())                                      formErrors.courtName    = '請輸入場地名稱'
  if (!form.courtType)                                             formErrors.courtType    = '請選擇場地類型'
  if (!form.location.trim())                                       formErrors.location     = '請輸入場地地點'
  if (!form.pricePerHour || isNaN(form.pricePerHour) || Number(form.pricePerHour) < 0)
                                                                   formErrors.pricePerHour = '請輸入有效費用'
  return Object.keys(formErrors).length === 0
}

async function submitForm() {
  if (!validate()) return
  submitting.value = true
  const payload = {
    courtName:    form.courtName.trim(),
    courtType:    form.courtType,
    location:     form.location.trim(),
    pricePerHour: Number(form.pricePerHour),
    status:       form.status,
    description:  form.description.trim() || null,
    imageUrl:     form.imageUrl.trim() || null,
  }
  try {
    if (modal.mode === 'create') {
      await createCourt(payload)
    } else {
      await updateCourt(modal.id, payload)
    }
    modal.open = false
    await fetchCourts()
  } catch {
    error.value = modal.mode === 'create' ? '新增失敗' : '更新失敗'
  } finally {
    submitting.value = false
  }
}

async function confirmDelete() {
  if (!deleteTarget.value) return
  try {
    await deleteCourt(deleteTarget.value.courtId)
    deleteTarget.value = null
    await fetchCourts()
  } catch {
    error.value = '刪除失敗'
    deleteTarget.value = null
  }
}

function formatDate(d) { return d ? new Date(d).toLocaleDateString('zh-TW') : '—' }

onMounted(fetchCourts)
</script>

<template>
  <div class="admin-page">
    <div class="page-header">
      <div class="container header-inner">
        <div>
          <p class="section-label">後台管理</p>
          <h1 class="page-title">場地管理</h1>
        </div>
        <button class="btn btn-primary" @click="openCreate">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
          </svg>
          新增場地
        </button>
      </div>
    </div>

    <div class="container admin-content">
      <transition name="fade">
        <div v-if="error" class="alert-error">
          {{ error }}
          <button @click="error = ''" class="alert-close">✕</button>
        </div>
      </transition>

      <div class="toolbar">
        <div class="search-wrap">
          <svg class="search-icon" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
          </svg>
          <input v-model="searchTerm" type="text" placeholder="搜尋場地名稱或地點..." class="search-input" />
        </div>
        <span class="total-count">共 {{ filteredCourts.length }} 個場地</span>
      </div>

      <div class="table-wrap">
        <div v-if="loading" class="table-loading">
          <div class="spinner"></div>
          <span>載入中...</span>
        </div>

        <table v-else class="courts-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>場地名稱</th>
              <th>類型</th>
              <th>地點</th>
              <th>費用/小時</th>
              <th>狀態</th>
              <th>建立日期</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="!filteredCourts.length">
              <td colspan="8" class="empty-row">
                {{ searchTerm ? '沒有符合搜尋的場地' : '尚未有場地，點擊「新增場地」開始' }}
              </td>
            </tr>
            <tr v-for="c in filteredCourts" :key="c.courtId" class="table-row">
              <td class="td-id">#{{ c.courtId }}</td>
              <td class="td-name">
                <div class="name-cell">
                  <div class="name-avatar" :class="c.courtType === 'INDOOR' ? 'avatar-sky' : 'avatar-lime'">
                    {{ c.courtType === 'INDOOR' ? '室' : '外' }}
                  </div>
                  <div>
                    <p class="name-text">{{ c.courtName }}</p>
                    <p v-if="c.description" class="name-desc">{{ c.description }}</p>
                  </div>
                </div>
              </td>
              <td>
                <span :class="['type-badge', c.courtType === 'INDOOR' ? 'badge-sky' : 'badge-lime', 'badge']">
                  {{ c.courtType === 'INDOOR' ? '室內' : '室外' }}
                </span>
              </td>
              <td class="td-location">{{ c.location }}</td>
              <td class="td-price">NT$ {{ c.pricePerHour?.toLocaleString() }}</td>
              <td>
                <span class="status-dot" :style="{ '--dot-color': statusMap[c.status]?.color || '#888' }">
                  {{ statusMap[c.status]?.label || c.status }}
                </span>
              </td>
              <td class="td-date">{{ formatDate(c.createdDate) }}</td>
              <td class="td-action">
                <button class="action-btn edit" @click="openEdit(c)" title="編輯">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
                    <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
                  </svg>
                </button>
                <button class="action-btn delete" @click="deleteTarget = c" title="刪除">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <polyline points="3,6 5,6 21,6"/>
                    <path d="M19 6l-1 14a2 2 0 01-2 2H8a2 2 0 01-2-2L5 6"/>
                    <path d="M10 11v6M14 11v6"/>
                    <path d="M9 6V4a1 1 0 011-1h4a1 1 0 011 1v2"/>
                  </svg>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Court Modal -->
    <transition name="modal">
      <div v-if="modal.open" class="modal-overlay" @click.self="modal.open = false">
        <div class="modal-box">
          <div class="modal-header">
            <h2>{{ modal.mode === 'create' ? '新增場地' : '編輯場地' }}</h2>
            <button class="modal-close" @click="modal.open = false">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>

          <form @submit.prevent="submitForm" class="modal-form">
            <div class="form-grid">
              <div class="form-group" :class="{ error: formErrors.courtName }">
                <label class="form-label">場地名稱 <span class="required">*</span></label>
                <input v-model="form.courtName" type="text" class="form-input" placeholder="例：A場地" />
                <span v-if="formErrors.courtName" class="form-error">{{ formErrors.courtName }}</span>
              </div>

              <div class="form-group" :class="{ error: formErrors.courtType }">
                <label class="form-label">場地類型 <span class="required">*</span></label>
                <select v-model="form.courtType" class="form-input">
                  <option value="" disabled>選擇類型</option>
                  <option v-for="t in COURT_TYPES" :key="t.key" :value="t.key">{{ t.label }}</option>
                </select>
                <span v-if="formErrors.courtType" class="form-error">{{ formErrors.courtType }}</span>
              </div>

              <div class="form-group full" :class="{ error: formErrors.location }">
                <label class="form-label">場地地點 <span class="required">*</span></label>
                <input v-model="form.location" type="text" class="form-input" placeholder="例：台北市信義區..." />
                <span v-if="formErrors.location" class="form-error">{{ formErrors.location }}</span>
              </div>

              <div class="form-group" :class="{ error: formErrors.pricePerHour }">
                <label class="form-label">每小時費用 (NT$) <span class="required">*</span></label>
                <input v-model="form.pricePerHour" type="number" min="0" class="form-input" placeholder="0" />
                <span v-if="formErrors.pricePerHour" class="form-error">{{ formErrors.pricePerHour }}</span>
              </div>

              <div class="form-group">
                <label class="form-label">場地狀態</label>
                <select v-model="form.status" class="form-input">
                  <option v-for="s in COURT_STATUS" :key="s.key" :value="s.key">{{ s.label }}</option>
                </select>
              </div>

              <div class="form-group full">
                <label class="form-label">場地圖片網址</label>
                <input v-model="form.imageUrl" type="url" class="form-input" placeholder="https://..." />
              </div>

              <div class="form-group full">
                <label class="form-label">場地描述</label>
                <textarea v-model="form.description" class="form-input textarea" placeholder="輸入場地說明（選填）" rows="3"></textarea>
              </div>
            </div>

            <div class="modal-footer">
              <button type="button" class="btn btn-ghost" @click="modal.open = false">取消</button>
              <button type="submit" class="btn btn-primary" :disabled="submitting">
                <span v-if="submitting" class="mini-spinner"></span>
                {{ modal.mode === 'create' ? '新增場地' : '儲存變更' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>

    <!-- Delete Confirm -->
    <transition name="modal">
      <div v-if="deleteTarget" class="modal-overlay" @click.self="deleteTarget = null">
        <div class="modal-box modal-sm">
          <div class="modal-header">
            <h2>確認刪除</h2>
            <button class="modal-close" @click="deleteTarget = null">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>
          <div class="delete-body">
            <div class="delete-icon">
              <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/>
                <line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/>
              </svg>
            </div>
            <p class="delete-msg">
              確定要刪除 <strong>{{ deleteTarget?.courtName }}</strong> 嗎？<br />
              <span>此操作無法復原。</span>
            </p>
          </div>
          <div class="modal-footer">
            <button class="btn btn-ghost" @click="deleteTarget = null">取消</button>
            <button class="btn btn-danger" @click="confirmDelete">確認刪除</button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<style scoped>
.admin-page { min-height: 80vh; }

.page-header {
  padding: calc(var(--navbar-h) + 32px) 0 32px;
  border-bottom: 1px solid var(--border);
  background: var(--surface);
}

.header-inner {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  gap: 16px;
  flex-wrap: wrap;
}

.page-title {
  font-family: 'Rajdhani', sans-serif;
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.admin-content { padding-top: 32px; padding-bottom: 80px; }

.alert-error {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--danger-dim);
  border: 1px solid rgba(239,68,68,0.3);
  color: var(--danger);
  border-radius: var(--radius);
  padding: 12px 16px;
  font-size: 0.88rem;
  margin-bottom: 20px;
}

.alert-close { background: none; border: none; color: var(--danger); cursor: pointer; font-size: 0.8rem; }

.toolbar {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.search-wrap { position: relative; flex: 1; max-width: 360px; }

.search-icon {
  position: absolute;
  left: 12px; top: 50%;
  transform: translateY(-50%);
  color: var(--text-muted);
  pointer-events: none;
}

.search-input {
  width: 100%;
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 9px 14px 9px 34px;
  color: var(--text);
  font-size: 0.88rem;
  outline: none;
  transition: var(--transition);
}

.search-input:focus { border-color: var(--accent); }
.search-input::placeholder { color: var(--text-muted); }

.total-count { font-size: 0.82rem; color: var(--text-muted); margin-left: auto; }

.table-wrap {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  overflow: hidden;
  overflow-x: auto;
}

.table-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 60px;
  color: var(--text-muted);
  font-size: 0.88rem;
}

.courts-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 800px;
}

.courts-table th {
  padding: 14px 16px;
  text-align: left;
  font-family: 'Rajdhani', sans-serif;
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: var(--text-muted);
  background: var(--surface);
  border-bottom: 1px solid var(--border);
  white-space: nowrap;
}

.table-row { transition: background 0.15s ease; }
.table-row:hover { background: rgba(255,255,255,0.025); }

.table-row td {
  padding: 14px 16px;
  border-bottom: 1px solid var(--border);
  font-size: 0.88rem;
  vertical-align: middle;
}

.table-row:last-child td { border-bottom: none; }

.td-id { color: var(--text-muted); font-size: 0.8rem; }

.name-cell { display: flex; align-items: center; gap: 12px; }

.name-avatar {
  width: 36px; height: 36px;
  border-radius: 8px;
  display: flex; align-items: center; justify-content: center;
  font-family: 'Rajdhani', sans-serif;
  font-size: 0.85rem;
  font-weight: 700;
  flex-shrink: 0;
}

.avatar-sky  { background: var(--sky-dim); color: var(--sky); }
.avatar-lime { background: var(--accent-dim); color: var(--accent); }

.name-text { font-weight: 500; color: var(--text); margin-bottom: 2px; }
.name-desc { font-size: 0.75rem; color: var(--text-muted); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 200px; }

.td-location { color: var(--text-dim); font-size: 0.85rem; }

.td-price { font-family: 'Rajdhani', sans-serif; font-size: 1rem; font-weight: 700; color: var(--accent); white-space: nowrap; }

.status-dot {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 0.82rem;
  color: var(--dot-color, var(--text-muted));
}

.status-dot::before {
  content: '';
  width: 6px; height: 6px;
  border-radius: 50%;
  background: var(--dot-color, var(--text-muted));
  box-shadow: 0 0 6px var(--dot-color, var(--text-muted));
  flex-shrink: 0;
}

.td-date { color: var(--text-dim); white-space: nowrap; }

.td-action { display: flex; gap: 6px; }

.action-btn {
  width: 32px; height: 32px;
  border-radius: 7px;
  border: 1px solid transparent;
  background: transparent;
  cursor: pointer;
  display: flex; align-items: center; justify-content: center;
  transition: var(--transition);
  color: var(--text-muted);
}

.action-btn.edit:hover  { background: var(--sky-dim); border-color: rgba(14,165,233,0.3); color: var(--sky); }
.action-btn.delete:hover { background: var(--danger-dim); border-color: rgba(239,68,68,0.3); color: var(--danger); }

.empty-row { text-align: center; padding: 60px 20px !important; color: var(--text-muted); font-size: 0.9rem; }

/* Modal */
.modal-overlay {
  position: fixed; inset: 0;
  background: rgba(0,0,0,0.75);
  backdrop-filter: blur(4px);
  z-index: 2000;
  display: flex; align-items: center; justify-content: center;
  padding: 20px;
}

.modal-box {
  background: var(--card);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  width: 100%;
  max-width: 560px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: var(--shadow-lg);
}

.modal-sm { max-width: 400px; }

.modal-header {
  display: flex; align-items: center; justify-content: space-between;
  padding: 24px 24px 0;
  margin-bottom: 20px;
}

.modal-header h2 {
  font-family: 'Rajdhani', sans-serif;
  font-size: 1.4rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.modal-close {
  width: 32px; height: 32px;
  border-radius: 8px;
  background: none;
  border: 1px solid var(--border);
  color: var(--text-muted);
  cursor: pointer;
  display: flex; align-items: center; justify-content: center;
  transition: var(--transition);
}

.modal-close:hover { border-color: var(--border-light); color: var(--text); }

.modal-form { padding: 0 24px; }

.form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }

.form-group { display: flex; flex-direction: column; gap: 6px; }
.form-group.full { grid-column: 1 / -1; }

.form-label {
  font-family: 'Rajdhani', sans-serif;
  font-size: 0.72rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  color: var(--text-dim);
}

.required { color: var(--accent); }

.form-input {
  background: var(--surface);
  border: 1px solid var(--border-light);
  border-radius: var(--radius);
  padding: 10px 14px;
  color: var(--text);
  font-size: 0.9rem;
  outline: none;
  transition: var(--transition);
  width: 100%;
}

.form-input:focus { border-color: var(--accent); background: var(--bg); }
.form-input::placeholder { color: var(--text-muted); }
.form-group.error .form-input { border-color: var(--danger); }
.form-error { font-size: 0.75rem; color: var(--danger); }
.textarea { resize: vertical; min-height: 80px; line-height: 1.5; }
select.form-input { cursor: pointer; }

.modal-footer {
  display: flex; justify-content: flex-end; gap: 10px;
  padding: 20px 24px;
  border-top: 1px solid var(--border);
  margin-top: 20px;
}

.delete-body {
  padding: 8px 24px 4px;
  display: flex; flex-direction: column; align-items: center; gap: 16px;
  text-align: center;
}

.delete-icon {
  color: #fbbf24;
  background: rgba(251,191,36,0.1);
  width: 64px; height: 64px;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
}

.delete-msg { font-size: 0.92rem; color: var(--text-dim); line-height: 1.6; }
.delete-msg strong { color: var(--text); font-weight: 600; }
.delete-msg span { font-size: 0.82rem; color: var(--text-muted); }

.mini-spinner {
  width: 14px; height: 14px;
  border: 2px solid rgba(0,0,0,0.3);
  border-top-color: #080e1a;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
  display: inline-block;
}

@keyframes spin { to { transform: rotate(360deg); } }

.modal-enter-active { transition: opacity 0.2s ease; }
.modal-leave-active { transition: opacity 0.15s ease; }
.modal-enter-from, .modal-leave-to { opacity: 0; }
.modal-enter-active .modal-box { transition: transform 0.25s cubic-bezier(0.34,1.56,0.64,1); }
.modal-enter-from .modal-box { transform: scale(0.94) translateY(8px); }

@media (max-width: 600px) {
  .form-grid { grid-template-columns: 1fr; }
  .form-group.full { grid-column: 1; }
}
</style>
