<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createActivity } from '../api/activities'

const router = useRouter()

const form = ref({
  title: '',
  location: '',
  scheduledTime: '',
  maxParticipants: 4,
  skillLevel: 'BEGINNER',
  description: '',
})
const loading = ref(false)
const error = ref('')

async function submit() {
  error.value = ''
  if (!form.value.title || !form.value.location || !form.value.scheduledTime) {
    error.value = '請填寫所有必填欄位'
    return
  }

  loading.value = true
  try {
    const payload = {
      ...form.value,
      scheduledTime: new Date(form.value.scheduledTime).getTime(),
    }
    const activity = await createActivity(payload)
    router.push(`/activities/${activity.activityId}`)
  } catch (e) {
    error.value = e.response?.data?.message || '建立失敗，請稍後再試'
  } finally {
    loading.value = false
  }
}

// Min datetime for input (now)
const minDatetime = new Date(Date.now() + 60 * 60 * 1000).toISOString().slice(0, 16)
</script>

<template>
  <div class="create-page">
    <div class="container">
      <div class="create-wrap">
        <div class="create-header">
          <button class="back-btn" @click="router.back()">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="19" y1="12" x2="5" y2="12"/><polyline points="12,19 5,12 12,5"/></svg>
            返回
          </button>
          <p class="section-label">揪球</p>
          <h1 class="create-title">發起活動</h1>
        </div>

        <form class="create-form" @submit.prevent="submit">
          <div class="form-group">
            <label class="form-label">活動名稱 <span class="required">*</span></label>
            <input v-model="form.title" class="form-input" type="text" placeholder="例：週末友誼賽 / 菜鳥練球" maxlength="100" required />
          </div>

          <div class="form-group">
            <label class="form-label">地點 <span class="required">*</span></label>
            <input v-model="form.location" class="form-input" type="text" placeholder="例：台北市大安區仁愛路某球場" required />
          </div>

          <div class="form-row">
            <div class="form-group">
              <label class="form-label">日期時間 <span class="required">*</span></label>
              <input v-model="form.scheduledTime" class="form-input" type="datetime-local" :min="minDatetime" required />
            </div>
            <div class="form-group">
              <label class="form-label">人數上限 <span class="required">*</span></label>
              <input v-model.number="form.maxParticipants" class="form-input" type="number" min="2" max="20" required />
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">技術程度 <span class="required">*</span></label>
            <div class="skill-options">
              <label v-for="opt in [['BEGINNER','新手入門'],['INTERMEDIATE','中級挑戰'],['ADVANCED','高手對決']]" :key="opt[0]" :class="['skill-opt', { active: form.skillLevel === opt[0] }]">
                <input type="radio" :value="opt[0]" v-model="form.skillLevel" class="hidden-radio" />
                {{ opt[1] }}
              </label>
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">活動說明</label>
            <textarea v-model="form.description" class="form-textarea" placeholder="告訴球友關於這場活動的更多資訊..." rows="4"></textarea>
          </div>

          <p v-if="error" class="error-msg">{{ error }}</p>

          <div class="form-actions">
            <button type="button" class="btn btn-ghost" @click="router.back()">取消</button>
            <button type="submit" class="btn btn-primary" :disabled="loading">
              {{ loading ? '建立中...' : '發起揪球' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.create-page { padding: 60px 0 80px; min-height: 80vh; }
.create-wrap { max-width: 640px; margin: 0 auto; }
.create-header { margin-bottom: 40px; }
.back-btn { display: inline-flex; align-items: center; gap: 6px; color: var(--text-muted); background: none; border: none; cursor: pointer; font-size: 0.9rem; margin-bottom: 20px; transition: color 0.2s; }
.back-btn:hover { color: var(--accent); }
.create-title { font-family: 'Rajdhani', sans-serif; font-size: 2.5rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.04em; }

.create-form { display: flex; flex-direction: column; gap: 24px; }
.form-group { display: flex; flex-direction: column; gap: 8px; }
.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
.form-label { font-size: 0.85rem; font-weight: 600; letter-spacing: 0.06em; text-transform: uppercase; color: var(--text-dim); }
.required { color: #ef4444; }
.form-input, .form-textarea {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 12px 16px;
  color: var(--text);
  font-size: 0.95rem;
  transition: border-color 0.2s;
  outline: none;
  width: 100%;
  box-sizing: border-box;
}
.form-input:focus, .form-textarea:focus { border-color: var(--accent); }
.form-textarea { resize: vertical; }

.skill-options { display: flex; gap: 10px; flex-wrap: wrap; }
.skill-opt {
  padding: 10px 20px;
  border-radius: 99px;
  border: 1px solid var(--border);
  color: var(--text-dim);
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.2s;
  user-select: none;
}
.skill-opt:hover { border-color: var(--accent); color: var(--accent); }
.skill-opt.active { background: var(--accent); border-color: var(--accent); color: #000; font-weight: 600; }
.hidden-radio { display: none; }

.error-msg { color: #f87171; font-size: 0.9rem; }
.form-actions { display: flex; gap: 12px; justify-content: flex-end; padding-top: 8px; }

@media (max-width: 600px) {
  .form-row { grid-template-columns: 1fr; }
}
</style>
