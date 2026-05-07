<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import AppNavbar from './components/AppNavbar.vue'
import AppFooter from './components/AppFooter.vue'

const cursorEl = ref(null)
const cursorVisible = ref(false)
const cursorPressed = ref(false)

function onMouseMove(e) {
  if (!cursorVisible.value) cursorVisible.value = true
  if (cursorEl.value) {
    cursorEl.value.style.transform = `translate(${e.clientX}px, ${e.clientY}px)`
  }
}
function onMouseLeave() { cursorVisible.value = false }
function onMouseDown()  { cursorPressed.value = true }
function onMouseUp()    { cursorPressed.value = false }

onMounted(() => {
  document.addEventListener('mousemove', onMouseMove)
  document.addEventListener('mouseleave', onMouseLeave)
  document.addEventListener('mousedown', onMouseDown)
  document.addEventListener('mouseup', onMouseUp)
  document.documentElement.style.cursor = 'none'
})

onUnmounted(() => {
  document.removeEventListener('mousemove', onMouseMove)
  document.removeEventListener('mouseleave', onMouseLeave)
  document.removeEventListener('mousedown', onMouseDown)
  document.removeEventListener('mouseup', onMouseUp)
  document.documentElement.style.cursor = ''
})
</script>

<template>
  <AppNavbar />
  <main class="main-content">
    <router-view v-slot="{ Component }">
      <transition name="page" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
  </main>
  <AppFooter />

  <!-- Global paddle cursor -->
  <div
    ref="cursorEl"
    class="paddle-cursor"
    :class="{ visible: cursorVisible, pressed: cursorPressed }"
  >
    <svg width="48" height="68" viewBox="0 0 48 68" fill="none" xmlns="http://www.w3.org/2000/svg">
      <defs>
        <radialGradient id="pg" cx="38%" cy="28%" r="55%">
          <stop offset="0%" stop-color="white" stop-opacity="0.25"/>
          <stop offset="100%" stop-color="white" stop-opacity="0"/>
        </radialGradient>
        <linearGradient id="hg" x1="0" y1="0" x2="1" y2="0">
          <stop offset="0%" stop-color="#2a4010"/>
          <stop offset="50%" stop-color="#3d6016"/>
          <stop offset="100%" stop-color="#2a4010"/>
        </linearGradient>
      </defs>
      <rect x="3" y="2" width="42" height="40" rx="10" ry="10" fill="#84cc16"/>
      <line x1="3"  y1="14" x2="45" y2="14" stroke="rgba(0,0,0,0.08)" stroke-width="1"/>
      <line x1="3"  y1="22" x2="45" y2="22" stroke="rgba(0,0,0,0.08)" stroke-width="1"/>
      <line x1="3"  y1="30" x2="45" y2="30" stroke="rgba(0,0,0,0.08)" stroke-width="1"/>
      <line x1="15" y1="2"  x2="15" y2="42" stroke="rgba(0,0,0,0.08)" stroke-width="1"/>
      <line x1="24" y1="2"  x2="24" y2="42" stroke="rgba(0,0,0,0.08)" stroke-width="1"/>
      <line x1="33" y1="2"  x2="33" y2="42" stroke="rgba(0,0,0,0.08)" stroke-width="1"/>
      <rect x="3" y="2" width="42" height="40" rx="10" ry="10" fill="url(#pg)"/>
      <rect x="3" y="2" width="42" height="40" rx="10" ry="10" stroke="rgba(255,255,255,0.18)" stroke-width="1.2" fill="none"/>
      <path d="M18 42 Q18 46 17 47 L17 48 H31 L31 47 Q30 46 30 42 Z" fill="#5a8c22"/>
      <rect x="17" y="48" width="14" height="18" rx="6" fill="url(#hg)"/>
      <line x1="17" y1="53" x2="31" y2="53" stroke="#84cc16" stroke-width="1.8" stroke-linecap="round" opacity="0.45"/>
      <line x1="17" y1="58" x2="31" y2="58" stroke="#84cc16" stroke-width="1.8" stroke-linecap="round" opacity="0.45"/>
      <line x1="17" y1="63" x2="31" y2="63" stroke="#84cc16" stroke-width="1.8" stroke-linecap="round" opacity="0.45"/>
      <rect x="18" y="64" width="12" height="3" rx="1.5" fill="#1e3a09"/>
    </svg>
  </div>
</template>

<style>
.main-content {
  flex: 1;
  padding-top: var(--navbar-h);
}

.paddle-cursor {
  position: fixed;
  top: 0; left: 0;
  margin-left: -24px;
  margin-top: -4px;
  pointer-events: none;
  z-index: 99999;
  opacity: 0;
  transition: opacity 0.15s ease;
  will-change: transform;
}

.paddle-cursor svg {
  transform: rotate(-35deg);
  transition: transform 0.12s ease;
  display: block;
  filter: drop-shadow(0 2px 8px rgba(0,0,0,0.5));
}

.paddle-cursor.visible { opacity: 1; }

.paddle-cursor.pressed svg {
  transform: rotate(-15deg) scale(0.88);
}
</style>
