<template>
  <footer class="footer">
    <div class="container footer-inner">
      <div class="footer-brand">
        <span class="brand-name">Vesto</span>
        <span class="brand-tagline">精選商城 · Premium Marketplace</span>
      </div>
      <nav class="footer-nav">
        <router-link to="/" class="footer-link">首頁</router-link>
        <router-link to="/products" class="footer-link">所有商品</router-link>
        <router-link to="/cart" class="footer-link">購物車</router-link>
        <router-link to="/admin" class="footer-link">管理後台</router-link>
      </nav>
      <p class="footer-copy">© 2026 Vesto. All rights reserved.</p>
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
  gap: 4px;
}

.brand-name {
  font-family: 'Playfair Display', serif;
  font-size: 1.3rem;
  font-weight: 600;
  letter-spacing: 0.1em;
  color: var(--accent);
}

.brand-tagline {
  font-size: 0.75rem;
  color: var(--text-muted);
  letter-spacing: 0.04em;
}

.footer-nav {
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
  justify-content: center;
}

.footer-link {
  font-size: 0.85rem;
  color: var(--text-muted);
  transition: var(--transition);
}

.footer-link:hover {
  color: var(--text-dim);
}

.footer-copy {
  font-size: 0.75rem;
  color: var(--text-muted);
}
</style>
