<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
</script>

<template>
  <div class="home">

    <!-- ========== HERO ========== -->
    <section class="hero">
      <!-- Animated background orbs -->
      <div class="hero-bg">
        <div class="orb orb-lime"></div>
        <div class="orb orb-sky"></div>
        <div class="orb orb-lime2"></div>
        <!-- Court line pattern -->
        <svg class="court-lines" viewBox="0 0 800 500" fill="none" preserveAspectRatio="xMidYMid slice">
          <!-- outer court -->
          <rect x="60" y="60" width="680" height="380" stroke="rgba(132,204,22,0.12)" stroke-width="2" />
          <!-- NVZ (kitchen) lines -->
          <line x1="60"  y1="197" x2="400" y2="197" stroke="rgba(132,204,22,0.1)" stroke-width="1.5" />
          <line x1="400" y1="197" x2="740" y2="197" stroke="rgba(132,204,22,0.1)" stroke-width="1.5" />
          <line x1="60"  y1="303" x2="400" y2="303" stroke="rgba(132,204,22,0.1)" stroke-width="1.5" />
          <line x1="400" y1="303" x2="740" y2="303" stroke="rgba(132,204,22,0.1)" stroke-width="1.5" />
          <!-- center line -->
          <line x1="400" y1="60" x2="400" y2="197" stroke="rgba(14,165,233,0.1)" stroke-width="1.5" />
          <line x1="400" y1="303" x2="400" y2="440" stroke="rgba(14,165,233,0.1)" stroke-width="1.5" />
          <!-- center service line horizontal -->
          <line x1="60" y1="250" x2="740" y2="250" stroke="rgba(14,165,233,0.08)" stroke-width="1.5" stroke-dasharray="8 6" />
          <!-- corner accents -->
          <circle cx="60"  cy="60"  r="4" fill="rgba(132,204,22,0.2)" />
          <circle cx="740" cy="60"  r="4" fill="rgba(132,204,22,0.2)" />
          <circle cx="60"  cy="440" r="4" fill="rgba(132,204,22,0.2)" />
          <circle cx="740" cy="440" r="4" fill="rgba(132,204,22,0.2)" />
          <circle cx="400" cy="250" r="5" fill="rgba(14,165,233,0.25)" />
        </svg>
        <div class="noise-overlay"></div>

        <!-- Bouncing pickleballs -->
        <div class="ball ball-1">
          <svg viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="14" cy="14" r="13" fill="#84cc16" opacity="0.9"/>
            <circle cx="9"  cy="10" r="1.6" fill="#080e1a" opacity="0.55"/>
            <circle cx="14" cy="8"  r="1.6" fill="#080e1a" opacity="0.55"/>
            <circle cx="19" cy="10" r="1.6" fill="#080e1a" opacity="0.55"/>
            <circle cx="7"  cy="15" r="1.6" fill="#080e1a" opacity="0.55"/>
            <circle cx="12" cy="14" r="1.6" fill="#080e1a" opacity="0.55"/>
            <circle cx="17" cy="14" r="1.6" fill="#080e1a" opacity="0.55"/>
            <circle cx="21" cy="15" r="1.6" fill="#080e1a" opacity="0.55"/>
            <circle cx="9"  cy="19" r="1.6" fill="#080e1a" opacity="0.55"/>
            <circle cx="14" cy="20" r="1.6" fill="#080e1a" opacity="0.55"/>
            <circle cx="19" cy="19" r="1.6" fill="#080e1a" opacity="0.55"/>
          </svg>
        </div>
        <div class="ball ball-2">
          <svg viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="14" cy="14" r="13" fill="#0ea5e9" opacity="0.8"/>
            <circle cx="9"  cy="10" r="1.6" fill="#080e1a" opacity="0.5"/>
            <circle cx="14" cy="8"  r="1.6" fill="#080e1a" opacity="0.5"/>
            <circle cx="19" cy="10" r="1.6" fill="#080e1a" opacity="0.5"/>
            <circle cx="7"  cy="15" r="1.6" fill="#080e1a" opacity="0.5"/>
            <circle cx="12" cy="14" r="1.6" fill="#080e1a" opacity="0.5"/>
            <circle cx="17" cy="14" r="1.6" fill="#080e1a" opacity="0.5"/>
            <circle cx="21" cy="15" r="1.6" fill="#080e1a" opacity="0.5"/>
            <circle cx="9"  cy="19" r="1.6" fill="#080e1a" opacity="0.5"/>
            <circle cx="14" cy="20" r="1.6" fill="#080e1a" opacity="0.5"/>
            <circle cx="19" cy="19" r="1.6" fill="#080e1a" opacity="0.5"/>
          </svg>
        </div>
        <div class="ball ball-3">
          <svg viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="14" cy="14" r="13" fill="#84cc16" opacity="0.6"/>
            <circle cx="9"  cy="10" r="1.6" fill="#080e1a" opacity="0.45"/>
            <circle cx="14" cy="8"  r="1.6" fill="#080e1a" opacity="0.45"/>
            <circle cx="19" cy="10" r="1.6" fill="#080e1a" opacity="0.45"/>
            <circle cx="7"  cy="15" r="1.6" fill="#080e1a" opacity="0.45"/>
            <circle cx="12" cy="14" r="1.6" fill="#080e1a" opacity="0.45"/>
            <circle cx="17" cy="14" r="1.6" fill="#080e1a" opacity="0.45"/>
            <circle cx="21" cy="15" r="1.6" fill="#080e1a" opacity="0.45"/>
            <circle cx="9"  cy="19" r="1.6" fill="#080e1a" opacity="0.45"/>
            <circle cx="14" cy="20" r="1.6" fill="#080e1a" opacity="0.45"/>
            <circle cx="19" cy="19" r="1.6" fill="#080e1a" opacity="0.45"/>
          </svg>
        </div>
      </div>


      <div class="container hero-content">
        <div class="hero-label">
          <span class="label-dot"></span>
          即時預約 · 隨時開球
        </div>
        <h1 class="hero-title">
          <span class="line line-1">BOOK YOUR</span>
          <span class="line line-2 accent-text">COURT</span>
          <span class="line line-3">NOW</span>
        </h1>
        <p class="hero-sub">找到最近的匹克球場，選擇時段，立即預約。<br>最快三步，場地到手。</p>
        <div class="hero-actions">
          <button class="btn btn-primary btn-lg" @click="router.push('/courts')">
            瀏覽場地
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
              <line x1="5" y1="12" x2="19" y2="12"/><polyline points="12,5 19,12 12,19"/>
            </svg>
          </button>
          <button
            class="btn btn-ghost btn-lg"
            @click="router.push(authStore.isAuthenticated ? '/my-reservations' : '/login')"
          >
            {{ authStore.isAuthenticated ? '我的預約' : '立即登入' }}
          </button>
        </div>

        <!-- Stats strip -->
        <div class="hero-stats">
          <div class="stat-item">
            <span class="stat-n">2</span>
            <span class="stat-l">場地類型</span>
          </div>
          <div class="stat-sep"></div>
          <div class="stat-item">
            <span class="stat-n">24/7</span>
            <span class="stat-l">線上預約</span>
          </div>
          <div class="stat-sep"></div>
          <div class="stat-item">
            <span class="stat-n">即時</span>
            <span class="stat-l">確認通知</span>
          </div>
        </div>
      </div>

      <div class="hero-scroll-hint">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="6,9 12,15 18,9"/>
        </svg>
      </div>
    </section>

    <!-- ========== HOW IT WORKS ========== -->
    <section class="steps-section">
      <div class="container">
        <p class="section-label">流程說明</p>
        <h2 class="section-title">三步完成預約</h2>
        <div class="steps-grid">
          <div class="step-card" v-for="(step, i) in steps" :key="i" :style="{ '--step-color': step.color }">
            <div class="step-num">{{ String(i + 1).padStart(2, '0') }}</div>
            <div class="step-icon-wrap">
              <span class="step-icon" v-html="step.icon"></span>
            </div>
            <h3 class="step-title">{{ step.title }}</h3>
            <p class="step-desc">{{ step.desc }}</p>
            <div class="step-connector" v-if="i < steps.length - 1">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="5" y1="12" x2="19" y2="12"/><polyline points="12,5 19,12 12,19"/>
              </svg>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ========== COURT TYPES ========== -->
    <section class="types-section">
      <div class="container">
        <p class="section-label">場地類型</p>
        <h2 class="section-title">選擇你的場地</h2>
        <div class="types-grid">

          <div class="type-card type-indoor" @click="router.push('/courts?type=INDOOR')">
            <div class="type-bg-pattern indoor-pattern"></div>
            <div class="type-body">
              <div class="type-icon">
                <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                  <path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/>
                  <polyline points="9,22 9,12 15,12 15,22"/>
                </svg>
              </div>
              <span class="type-tag">INDOOR</span>
              <h3 class="type-name">室內場地</h3>
              <p class="type-desc">全天候恆溫環境，不受天氣影響。專業地板、良好採光，適合進階訓練。</p>
              <div class="type-cta">
                查看室內場地
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                  <line x1="5" y1="12" x2="19" y2="12"/><polyline points="12,5 19,12 12,19"/>
                </svg>
              </div>
            </div>
          </div>

          <div class="type-card type-outdoor" @click="router.push('/courts?type=OUTDOOR')">
            <div class="type-bg-pattern outdoor-pattern"></div>
            <div class="type-body">
              <div class="type-icon">
                <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                  <circle cx="12" cy="12" r="5"/>
                  <line x1="12" y1="1" x2="12" y2="3"/>
                  <line x1="12" y1="21" x2="12" y2="23"/>
                  <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/>
                  <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/>
                  <line x1="1" y1="12" x2="3" y2="12"/>
                  <line x1="21" y1="12" x2="23" y2="12"/>
                  <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/>
                  <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/>
                </svg>
              </div>
              <span class="type-tag">OUTDOOR</span>
              <h3 class="type-name">室外場地</h3>
              <p class="type-desc">開闊的戶外環境，享受自然光線。快乾硬地球場，感受原汁原味的戶外比賽。</p>
              <div class="type-cta">
                查看室外場地
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                  <line x1="5" y1="12" x2="19" y2="12"/><polyline points="12,5 19,12 12,19"/>
                </svg>
              </div>
            </div>
          </div>

        </div>
      </div>
    </section>

    <!-- ========== CTA BANNER ========== -->
    <section class="cta-section">
      <div class="container">
        <div class="cta-card">
          <div class="cta-ball">
            <svg width="80" height="80" viewBox="0 0 28 28" fill="none">
              <circle cx="14" cy="14" r="13" fill="#84cc16" opacity="0.15"/>
              <circle cx="14" cy="14" r="13" stroke="#84cc16" stroke-width="1.5" stroke-dasharray="4 3"/>
            </svg>
          </div>
          <div class="cta-text">
            <h2 class="cta-title">準備好上場了嗎？</h2>
            <p class="cta-sub">立即瀏覽可預約的場地，搶先佔位。</p>
          </div>
          <button class="btn btn-primary btn-lg" @click="router.push('/courts')">
            立刻預約
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
              <line x1="5" y1="12" x2="19" y2="12"/><polyline points="12,5 19,12 12,19"/>
            </svg>
          </button>
        </div>
      </div>
    </section>

  </div>
</template>

<script>
const steps = [
  {
    title: '瀏覽場地',
    desc: '依照類型、地點篩選，找到最適合你的場地。',
    color: '#84cc16',
    icon: `<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
      <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
    </svg>`,
  },
  {
    title: '選擇時段',
    desc: '挑選日期與開始/結束時間，系統即時確認是否可用。',
    color: '#0ea5e9',
    icon: `<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
      <rect x="3" y="4" width="18" height="18" rx="2"/><line x1="16" y1="2" x2="16" y2="6"/>
      <line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/>
      <circle cx="12" cy="15" r="2" fill="currentColor"/>
    </svg>`,
  },
  {
    title: '開始比賽！',
    desc: '預約成功，按時前往場地，盡情享受匹克球樂趣。',
    color: '#84cc16',
    icon: `<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
      <circle cx="12" cy="12" r="9"/>
      <polyline points="12,8 12,12 15,15"/>
    </svg>`,
  },
]
</script>

<style scoped>
/* ===== HERO ===== */
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
  filter: blur(100px);
  pointer-events: none;
}

.orb-lime {
  width: 500px; height: 500px;
  background: radial-gradient(circle, rgba(132, 204, 22, 0.2), transparent 70%);
  top: -100px; right: -80px;
  animation: drift 16s ease-in-out infinite alternate;
}

.orb-sky {
  width: 400px; height: 400px;
  background: radial-gradient(circle, rgba(14, 165, 233, 0.18), transparent 70%);
  bottom: -60px; left: -60px;
  animation: drift 20s ease-in-out infinite alternate-reverse;
}

.orb-lime2 {
  width: 250px; height: 250px;
  background: radial-gradient(circle, rgba(132, 204, 22, 0.1), transparent 70%);
  top: 50%; left: 45%;
  animation: drift 12s ease-in-out infinite alternate;
}

@keyframes drift {
  from { transform: translate(0, 0) scale(1); }
  to   { transform: translate(40px, 25px) scale(1.1); }
}

.court-lines {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
}

.noise-overlay {
  position: absolute;
  inset: 0;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noise'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noise)' opacity='0.04'/%3E%3C/svg%3E");
  background-size: 200px 200px;
  opacity: 0.4;
  pointer-events: none;
}

.hero-content {
  position: relative;
  z-index: 1;
  padding-top: 80px;
  padding-bottom: 80px;
}

.hero-label {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 0.72rem;
  font-weight: 600;
  letter-spacing: 0.2em;
  text-transform: uppercase;
  color: var(--accent);
  margin-bottom: 28px;
  opacity: 0;
  animation: fadeUp 0.5s ease 0.1s forwards;
}

.label-dot {
  width: 6px; height: 6px;
  background: var(--accent);
  border-radius: 50%;
  box-shadow: 0 0 8px var(--accent);
  animation: pulse-dot 2s ease-in-out infinite;
}

@keyframes pulse-dot {
  0%, 100% { opacity: 1; transform: scale(1); }
  50%       { opacity: 0.5; transform: scale(0.7); }
}

.hero-title {
  display: flex;
  flex-direction: column;
  font-family: 'Rajdhani', sans-serif;
  font-size: clamp(4rem, 10vw, 9rem);
  font-weight: 700;
  letter-spacing: 0.04em;
  line-height: 0.95;
  text-transform: uppercase;
  margin-bottom: 32px;
}

.line {
  display: block;
  opacity: 0;
  animation: fadeUp 0.5s ease forwards;
}
.line-1 { animation-delay: 0.25s; }
.line-2 { animation-delay: 0.4s; }
.line-3 { animation-delay: 0.55s; }

.accent-text {
  color: var(--accent);
  -webkit-text-stroke: 2px var(--accent);
  text-shadow: 0 0 40px rgba(132, 204, 22, 0.4);
}

.hero-sub {
  font-size: clamp(0.95rem, 1.8vw, 1.1rem);
  color: var(--text-dim);
  line-height: 1.8;
  margin-bottom: 40px;
  max-width: 520px;
  opacity: 0;
  animation: fadeUp 0.5s ease 0.7s forwards;
}

.hero-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 64px;
  opacity: 0;
  animation: fadeUp 0.5s ease 0.85s forwards;
}

.btn-lg {
  padding: 13px 28px;
  font-size: 1rem;
}

.hero-stats {
  display: flex;
  align-items: center;
  gap: 28px;
  opacity: 0;
  animation: fadeUp 0.5s ease 1s forwards;
}

.stat-item {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.stat-n {
  font-family: 'Rajdhani', sans-serif;
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--text);
  letter-spacing: 0.04em;
  line-height: 1;
}

.stat-l {
  font-size: 0.7rem;
  color: var(--text-muted);
  letter-spacing: 0.06em;
  text-transform: uppercase;
}

.stat-sep {
  width: 1px;
  height: 38px;
  background: var(--border-light);
}

.hero-scroll-hint {
  position: absolute;
  bottom: 28px;
  left: 50%;
  transform: translateX(-50%);
  color: var(--text-muted);
  opacity: 0;
  animation: fadeIn 0.5s ease 1.4s forwards, bounce 2s ease-in-out 1.5s infinite;
}

@keyframes bounce {
  0%, 100% { transform: translateX(-50%) translateY(0); }
  50%       { transform: translateX(-50%) translateY(6px); }
}

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(20px); }
  to   { opacity: 1; transform: translateY(0); }
}

@keyframes fadeIn {
  to { opacity: 1; }
}

/* ===== STEPS ===== */
.steps-section {
  padding: 100px 0;
  background: var(--surface);
  border-top: 1px solid var(--border);
}

.steps-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0;
  margin-top: 48px;
  position: relative;
}

.step-card {
  --step-color: var(--accent);
  padding: 36px 32px;
  border: 1px solid var(--border);
  background: var(--card);
  position: relative;
  transition: border-color 0.25s ease, background 0.25s ease;
}

.step-card:first-child { border-radius: var(--radius-lg) 0 0 var(--radius-lg); }
.step-card:last-child  { border-radius: 0 var(--radius-lg) var(--radius-lg) 0; }

.step-card:hover {
  border-color: var(--step-color);
  background: var(--card-hover);
}

.step-card::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0;
  height: 3px;
  border-radius: inherit;
  background: var(--step-color);
  opacity: 0;
  transition: opacity 0.25s ease;
}

.step-card:hover::before { opacity: 1; }

.step-num {
  font-family: 'Rajdhani', sans-serif;
  font-size: 3.5rem;
  font-weight: 700;
  color: var(--border-light);
  line-height: 1;
  margin-bottom: 16px;
  letter-spacing: -0.02em;
}

.step-icon-wrap {
  width: 52px; height: 52px;
  border-radius: 12px;
  background: rgba(132, 204, 22, 0.08);
  border: 1px solid rgba(132, 204, 22, 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--step-color);
  margin-bottom: 20px;
}

.step-title {
  font-family: 'Rajdhani', sans-serif;
  font-size: 1.4rem;
  font-weight: 700;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  color: var(--text);
  margin-bottom: 10px;
}

.step-desc {
  font-size: 0.88rem;
  color: var(--text-dim);
  line-height: 1.7;
}

.step-connector {
  position: absolute;
  right: -14px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 2;
  color: var(--text-muted);
  background: var(--bg);
  border-radius: 50%;
  width: 28px; height: 28px;
  display: flex; align-items: center; justify-content: center;
  border: 1px solid var(--border);
}

/* ===== COURT TYPES ===== */
.types-section {
  padding: 100px 0;
}

.types-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-top: 48px;
}

.type-card {
  position: relative;
  border-radius: var(--radius-lg);
  border: 1px solid var(--border);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease, border-color 0.3s ease;
  min-height: 320px;
  display: flex;
}

.type-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.6);
}

.type-card.type-indoor { background: linear-gradient(135deg, #0e1e36, #101c30); }
.type-card.type-outdoor { background: linear-gradient(135deg, #0c1e18, #0f2010); }

.type-card.type-indoor:hover  { border-color: var(--sky); }
.type-card.type-outdoor:hover { border-color: var(--accent); }

.type-bg-pattern {
  position: absolute;
  inset: 0;
  opacity: 0.07;
  pointer-events: none;
}

.indoor-pattern {
  background:
    repeating-linear-gradient(0deg, var(--sky) 0, var(--sky) 1px, transparent 0, transparent 40px),
    repeating-linear-gradient(90deg, var(--sky) 0, var(--sky) 1px, transparent 0, transparent 40px);
}

.outdoor-pattern {
  background:
    repeating-linear-gradient(45deg, var(--accent) 0, var(--accent) 1px, transparent 0, transparent 30px),
    repeating-linear-gradient(-45deg, var(--accent) 0, var(--accent) 1px, transparent 0, transparent 30px);
}

.type-body {
  position: relative;
  z-index: 1;
  padding: 40px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.type-icon {
  width: 60px; height: 60px;
  border-radius: 14px;
  display: flex; align-items: center; justify-content: center;
  margin-bottom: 8px;
}

.type-indoor .type-icon  { background: var(--sky-dim); color: var(--sky); border: 1px solid rgba(14,165,233,0.2); }
.type-outdoor .type-icon { background: var(--accent-dim); color: var(--accent); border: 1px solid rgba(132,204,22,0.2); }

.type-tag {
  font-family: 'Rajdhani', sans-serif;
  font-size: 0.72rem;
  font-weight: 700;
  letter-spacing: 0.18em;
  text-transform: uppercase;
  padding: 3px 10px;
  border-radius: 99px;
  width: fit-content;
}

.type-indoor .type-tag  { background: var(--sky-dim); color: var(--sky); }
.type-outdoor .type-tag { background: var(--accent-dim); color: var(--accent); }

.type-name {
  font-family: 'Rajdhani', sans-serif;
  font-size: 2rem;
  font-weight: 700;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  color: #e2eeff;
}

.type-desc {
  font-size: 0.9rem;
  color: #7b96b8;
  line-height: 1.7;
  flex: 1;
}

.type-cta {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-family: 'Rajdhani', sans-serif;
  font-size: 0.9rem;
  font-weight: 600;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  margin-top: 8px;
  transition: gap 0.2s ease;
}

.type-indoor .type-cta  { color: var(--sky); }
.type-outdoor .type-cta { color: var(--accent); }

.type-card:hover .type-cta { gap: 10px; }

/* ===== CTA ===== */
.cta-section { padding: 0 0 80px; }

.cta-card {
  background: var(--card);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: 56px 48px;
  display: flex;
  align-items: center;
  gap: 32px;
  position: relative;
  overflow: hidden;
}

.cta-card::after {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, rgba(132,204,22,0.04) 0%, transparent 60%);
  pointer-events: none;
}

.cta-ball {
  flex-shrink: 0;
  animation: spin-slow 8s linear infinite;
}

@keyframes spin-slow {
  to { transform: rotate(360deg); }
}

.cta-text { flex: 1; }

.cta-title {
  font-family: 'Rajdhani', sans-serif;
  font-size: 2.2rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--text);
  margin-bottom: 8px;
}

.cta-sub {
  font-size: 0.9rem;
  color: var(--text-dim);
}

/* ===== Responsive ===== */
@media (max-width: 1024px) {
  .types-grid { grid-template-columns: 1fr; }
}

@media (max-width: 768px) {
  .steps-grid { grid-template-columns: 1fr; }
  .step-card:first-child { border-radius: var(--radius-lg) var(--radius-lg) 0 0; }
  .step-card:last-child  { border-radius: 0 0 var(--radius-lg) var(--radius-lg); }
  .step-connector { display: none; }
  .cta-card { flex-direction: column; text-align: center; padding: 40px 24px; }
  .cta-ball { display: none; }
}

@media (max-width: 480px) {
  .hero-title { font-size: clamp(3rem, 14vw, 5rem); }
  .hero-stats { gap: 16px; }
}

/* ===== BOUNCING BALLS ===== */
.ball {
  position: absolute;
  pointer-events: none;
  will-change: transform;
  filter: drop-shadow(0 0 12px rgba(132,204,22,0.4));
}

.ball-1 {
  width: 48px; height: 48px;
  top: 15%;
  left: 70%;
  animation: bounce1 7s ease-in-out infinite;
}

.ball-2 {
  width: 36px; height: 36px;
  top: 60%;
  left: 80%;
  animation: bounce2 5.5s ease-in-out infinite alternate;
  filter: drop-shadow(0 0 10px rgba(14,165,233,0.45));
}

.ball-3 {
  width: 28px; height: 28px;
  top: 35%;
  left: 55%;
  animation: bounce3 9s ease-in-out infinite;
  opacity: 0.6;
}

.ball svg { animation: ball-spin 2.5s linear infinite; display: block; }
.ball-2 svg { animation-duration: 3.5s; animation-direction: reverse; }
.ball-3 svg { animation-duration: 4.5s; }

@keyframes ball-spin {
  to { transform: rotate(360deg); }
}

@keyframes bounce1 {
  0%   { transform: translate(0px,   0px); }
  15%  { transform: translate(-90px, 140px); }
  30%  { transform: translate(-30px, 280px); }
  45%  { transform: translate(60px,  150px); }
  60%  { transform: translate(30px,  -60px); }
  75%  { transform: translate(-60px, 80px); }
  90%  { transform: translate(-120px,200px); }
  100% { transform: translate(0px,   0px); }
}

@keyframes bounce2 {
  0%   { transform: translate(0px, 0px); }
  25%  { transform: translate(-80px, -100px); }
  50%  { transform: translate(-160px, 20px); }
  75%  { transform: translate(-60px, -140px); }
  100% { transform: translate(0px, 0px); }
}

@keyframes bounce3 {
  0%   { transform: translate(0px, 0px); }
  20%  { transform: translate(120px, 180px); }
  40%  { transform: translate(60px, -80px); }
  60%  { transform: translate(-80px, 120px); }
  80%  { transform: translate(40px, 200px); }
  100% { transform: translate(0px, 0px); }
}

</style>
