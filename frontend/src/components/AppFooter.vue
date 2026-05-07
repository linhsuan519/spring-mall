<template>
  <footer class="footer">
    <div class="container footer-inner">
      <div class="footer-brand">
        <div class="brand-row">
          <svg width="22" height="22" viewBox="0 0 28 28" fill="none">
            <circle cx="14" cy="14" r="13" fill="#84cc16" />
            <circle cx="9"  cy="10" r="1.6" fill="rgba(8,14,26,0.55)" />
            <circle cx="14" cy="8"  r="1.6" fill="rgba(8,14,26,0.55)" />
            <circle cx="19" cy="10" r="1.6" fill="rgba(8,14,26,0.55)" />
            <circle cx="7"  cy="15" r="1.6" fill="rgba(8,14,26,0.55)" />
            <circle cx="12" cy="14" r="1.6" fill="rgba(8,14,26,0.55)" />
            <circle cx="17" cy="14" r="1.6" fill="rgba(8,14,26,0.55)" />
            <circle cx="21" cy="15" r="1.6" fill="rgba(8,14,26,0.55)" />
            <circle cx="9"  cy="19" r="1.6" fill="rgba(8,14,26,0.55)" />
            <circle cx="14" cy="20" r="1.6" fill="rgba(8,14,26,0.55)" />
            <circle cx="19" cy="19" r="1.6" fill="rgba(8,14,26,0.55)" />
          </svg>
          <span class="brand-name">Pickle<span class="accent">Zone</span></span>
        </div>
        <span class="brand-tagline">匹克球場地預約平台</span>
      </div>
      <nav class="footer-nav">
        <router-link to="/" class="footer-link">首頁</router-link>
        <router-link to="/courts" class="footer-link">場地瀏覽</router-link>
        <router-link to="/my-reservations" class="footer-link">我的預約</router-link>
        <router-link to="/admin" class="footer-link">管理後台</router-link>
      </nav>
      <p class="footer-copy">© 2026 PickleZone. All rights reserved.</p>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  border-top: 1px solid var(--border);
  background: var(--surface);
  margin-top: auto;
}

.footer-inner {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding-top: 40px;
  padding-bottom: 40px;
  text-align: center;
}

.footer-brand {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
}

.brand-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.brand-name {
  font-family: 'Rajdhani', sans-serif;
  font-size: 1.3rem;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--text);
}

.accent {
  color: var(--accent);
}

.brand-tagline {
  font-size: 0.75rem;
  color: var(--text-muted);
  letter-spacing: 0.06em;
}

.footer-nav {
  display: flex;
  gap: 28px;
  flex-wrap: wrap;
  justify-content: center;
}

.footer-link {
  font-family: 'Rajdhani', sans-serif;
  font-size: 0.9rem;
  font-weight: 600;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  color: var(--text-muted);
  transition: var(--transition);
}

.footer-link:hover {
  color: var(--accent);
}

.footer-copy {
  font-size: 0.75rem;
  color: var(--text-muted);
}
</style>
