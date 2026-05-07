<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { getProducts, createProduct, updateProduct, deleteProduct } from '../api/products'

const products = ref([])
const loading = ref(true)
const error = ref('')
const submitting = ref(false)
const deleteTarget = ref(null)
const searchTerm = ref('')

const modal = reactive({
  open: false,
  mode: 'create',
  id: null,
})

const form = reactive({
  productName: '',
  category: '',
  imageUrl: '',
  price: '',
  stock: '',
  description: '',
})

const formErrors = reactive({})

const CATEGORIES = [
  { key: 'SP', label: '股票', color: '#22c55e' },
  { key: 'FUND', label: '基金', color: '#60a5fa' },
  { key: 'BOND', label: '債券', color: '#a78bfa' },
  { key: 'ETF', label: 'ETF', color: '#34d399' },
  { key: 'SAVING', label: '儲蓄', color: '#fbbf24' },
  { key: 'INSURANCE', label: '保險', color: '#fb7185' },
  { key: 'FOOD', label: '食品', color: '#fb923c' },
  { key: 'CAR', label: '汽車', color: '#94a3b8' },
]

const catMap = Object.fromEntries(CATEGORIES.map((c) => [c.key, c]))

const filteredProducts = computed(() => {
  const q = searchTerm.value.toLowerCase()
  if (!q) return products.value
  return products.value.filter(
    (p) =>
      p.productName.toLowerCase().includes(q) ||
      (catMap[p.category]?.label || p.category).toLowerCase().includes(q)
  )
})

async function fetchProducts() {
  loading.value = true
  error.value = ''
  try {
    const data = await getProducts({ limit: 1000 })
    products.value = data.results || data
  } catch {
    error.value = '無法載入商品資料'
  } finally {
    loading.value = false
  }
}

function openCreate() {
  modal.mode = 'create'
  modal.id = null
  Object.assign(form, {
    productName: '',
    category: '',
    imageUrl: '',
    price: '',
    stock: '',
    description: '',
  })
  Object.keys(formErrors).forEach((k) => delete formErrors[k])
  modal.open = true
}

function openEdit(product) {
  modal.mode = 'edit'
  modal.id = product.productId
  Object.assign(form, {
    productName: product.productName,
    category: product.category,
    imageUrl: product.imageUrl || '',
    price: String(product.price),
    stock: String(product.stock),
    description: product.description || '',
  })
  Object.keys(formErrors).forEach((k) => delete formErrors[k])
  modal.open = true
}

function validate() {
  Object.keys(formErrors).forEach((k) => delete formErrors[k])
  if (!form.productName.trim()) formErrors.productName = '請輸入商品名稱'
  if (!form.category) formErrors.category = '請選擇分類'
  if (!form.imageUrl.trim()) formErrors.imageUrl = '請輸入圖片網址'
  if (!form.price || isNaN(form.price) || Number(form.price) < 0)
    formErrors.price = '請輸入有效價格'
  if (!form.stock || isNaN(form.stock) || Number(form.stock) < 0)
    formErrors.stock = '請輸入有效庫存'
  return Object.keys(formErrors).length === 0
}

async function submitForm() {
  if (!validate()) return
  submitting.value = true
  const payload = {
    productName: form.productName.trim(),
    category: form.category,
    imageUrl: form.imageUrl.trim(),
    price: Number(form.price),
    stock: Number(form.stock),
    description: form.description.trim() || null,
  }
  try {
    if (modal.mode === 'create') {
      await createProduct(payload)
    } else {
      await updateProduct(modal.id, payload)
    }
    modal.open = false
    await fetchProducts()
  } catch (e) {
    error.value = modal.mode === 'create' ? '新增失敗' : '更新失敗'
  } finally {
    submitting.value = false
  }
}

async function confirmDelete() {
  if (!deleteTarget.value) return
  try {
    await deleteProduct(deleteTarget.value.productId)
    deleteTarget.value = null
    await fetchProducts()
  } catch {
    error.value = '刪除失敗'
    deleteTarget.value = null
  }
}

function formatDate(d) {
  return d ? new Date(d).toLocaleDateString('zh-TW') : '—'
}

function formatPrice(p) {
  return new Intl.NumberFormat('zh-TW').format(p)
}

onMounted(fetchProducts)
</script>

<template>
  <div class="admin-page">
    <div class="page-header">
      <div class="container header-inner">
        <div>
          <p class="section-label">後台管理</p>
          <h1 class="page-title">商品管理</h1>
        </div>
        <button class="btn btn-primary" @click="openCreate">
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
          >
            <line x1="12" y1="5" x2="12" y2="19" />
            <line x1="5" y1="12" x2="19" y2="12" />
          </svg>
          新增商品
        </button>
      </div>
    </div>

    <div class="container admin-content">
      <!-- Error -->
      <transition name="fade">
        <div v-if="error" class="alert-error">
          {{ error }}
          <button @click="error = ''" class="alert-close">✕</button>
        </div>
      </transition>

      <!-- Toolbar -->
      <div class="toolbar">
        <div class="search-wrap">
          <svg
            class="search-icon"
            width="14"
            height="14"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <circle cx="11" cy="11" r="8" />
            <line x1="21" y1="21" x2="16.65" y2="16.65" />
          </svg>
          <input
            v-model="searchTerm"
            type="text"
            placeholder="搜尋商品名稱或分類..."
            class="search-input"
          />
        </div>
        <span class="total-count">共 {{ filteredProducts.length }} 件商品</span>
      </div>

      <!-- Table -->
      <div class="table-wrap">
        <div v-if="loading" class="table-loading">
          <div class="spinner"></div>
          <span>載入中...</span>
        </div>

        <table v-else class="products-table">
          <thead>
            <tr>
              <th class="th-id">ID</th>
              <th class="th-name">商品名稱</th>
              <th class="th-cat">分類</th>
              <th class="th-price">價格</th>
              <th class="th-stock">庫存</th>
              <th class="th-date">建立日期</th>
              <th class="th-action">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="!filteredProducts.length">
              <td colspan="7" class="empty-row">
                {{ searchTerm ? '沒有符合搜尋的商品' : '尚未有商品，點擊「新增商品」開始' }}
              </td>
            </tr>
            <tr v-for="p in filteredProducts" :key="p.productId" class="table-row">
              <td class="td-id">#{{ p.productId }}</td>
              <td class="td-name">
                <div class="name-cell">
                  <div
                    class="name-avatar"
                    :style="{
                      background: `${catMap[p.category]?.color || '#888'}22`,
                      color: catMap[p.category]?.color || '#888',
                    }"
                  >
                    {{ p.productName?.charAt(0) }}
                  </div>
                  <div>
                    <p class="name-text">{{ p.productName }}</p>
                    <p v-if="p.description" class="name-desc">{{ p.description }}</p>
                  </div>
                </div>
              </td>
              <td class="td-cat">
                <span
                  class="cat-badge"
                  :style="{
                    color: catMap[p.category]?.color || '#888',
                    background: `${catMap[p.category]?.color || '#888'}18`,
                  }"
                  >{{ catMap[p.category]?.label || p.category }}</span
                >
              </td>
              <td class="td-price">NT$ {{ formatPrice(p.price) }}</td>
              <td class="td-stock">
                <span :class="['stock-num', p.stock === 0 ? 'out' : p.stock <= 10 ? 'low' : 'in']">
                  {{ p.stock }}
                </span>
              </td>
              <td class="td-date">{{ formatDate(p.createdDate) }}</td>
              <td class="td-action">
                <button class="action-btn edit" @click="openEdit(p)" title="編輯">
                  <svg
                    width="14"
                    height="14"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                  >
                    <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7" />
                    <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z" />
                  </svg>
                </button>
                <button class="action-btn delete" @click="deleteTarget = p" title="刪除">
                  <svg
                    width="14"
                    height="14"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                  >
                    <polyline points="3,6 5,6 21,6" />
                    <path d="M19 6l-1 14a2 2 0 01-2 2H8a2 2 0 01-2-2L5 6" />
                    <path d="M10 11v6M14 11v6" />
                    <path d="M9 6V4a1 1 0 011-1h4a1 1 0 011 1v2" />
                  </svg>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Product Modal -->
    <transition name="modal">
      <div v-if="modal.open" class="modal-overlay" @click.self="modal.open = false">
        <div class="modal-box">
          <div class="modal-header">
            <h2>{{ modal.mode === 'create' ? '新增商品' : '編輯商品' }}</h2>
            <button class="modal-close" @click="modal.open = false">
              <svg
                width="18"
                height="18"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <line x1="18" y1="6" x2="6" y2="18" />
                <line x1="6" y1="6" x2="18" y2="18" />
              </svg>
            </button>
          </div>

          <form @submit.prevent="submitForm" class="modal-form">
            <div class="form-grid">
              <div class="form-group" :class="{ error: formErrors.productName }">
                <label class="form-label">商品名稱 <span class="required">*</span></label>
                <input
                  v-model="form.productName"
                  type="text"
                  class="form-input"
                  placeholder="輸入商品名稱"
                />
                <span v-if="formErrors.productName" class="form-error">{{
                  formErrors.productName
                }}</span>
              </div>

              <div class="form-group" :class="{ error: formErrors.category }">
                <label class="form-label">商品分類 <span class="required">*</span></label>
                <select v-model="form.category" class="form-input">
                  <option value="" disabled>選擇分類</option>
                  <option v-for="cat in CATEGORIES" :key="cat.key" :value="cat.key">
                    {{ cat.label }} ({{ cat.key }})
                  </option>
                </select>
                <span v-if="formErrors.category" class="form-error">{{ formErrors.category }}</span>
              </div>

              <div class="form-group full" :class="{ error: formErrors.imageUrl }">
                <label class="form-label">圖片網址 <span class="required">*</span></label>
                <input
                  v-model="form.imageUrl"
                  type="url"
                  class="form-input"
                  placeholder="https://example.com/image.jpg"
                />
                <span v-if="formErrors.imageUrl" class="form-error">{{ formErrors.imageUrl }}</span>
              </div>

              <div class="form-group" :class="{ error: formErrors.price }">
                <label class="form-label">價格 (NT$) <span class="required">*</span></label>
                <input
                  v-model="form.price"
                  type="number"
                  min="0"
                  class="form-input"
                  placeholder="0"
                />
                <span v-if="formErrors.price" class="form-error">{{ formErrors.price }}</span>
              </div>

              <div class="form-group" :class="{ error: formErrors.stock }">
                <label class="form-label">庫存數量 <span class="required">*</span></label>
                <input
                  v-model="form.stock"
                  type="number"
                  min="0"
                  class="form-input"
                  placeholder="0"
                />
                <span v-if="formErrors.stock" class="form-error">{{ formErrors.stock }}</span>
              </div>

              <div class="form-group full">
                <label class="form-label">商品描述</label>
                <textarea
                  v-model="form.description"
                  class="form-input textarea"
                  placeholder="輸入商品說明（選填）"
                  rows="3"
                ></textarea>
              </div>
            </div>

            <div class="modal-footer">
              <button type="button" class="btn btn-ghost" @click="modal.open = false">取消</button>
              <button type="submit" class="btn btn-primary" :disabled="submitting">
                <span v-if="submitting" class="mini-spinner"></span>
                {{ modal.mode === 'create' ? '新增商品' : '儲存變更' }}
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
              <svg
                width="18"
                height="18"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <line x1="18" y1="6" x2="6" y2="18" />
                <line x1="6" y1="6" x2="18" y2="18" />
              </svg>
            </button>
          </div>
          <div class="delete-body">
            <div class="delete-icon">
              <svg
                width="32"
                height="32"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="1.5"
              >
                <path
                  d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"
                />
                <line x1="12" y1="9" x2="12" y2="13" />
                <line x1="12" y1="17" x2="12.01" y2="17" />
              </svg>
            </div>
            <p class="delete-msg">
              確定要刪除 <strong>{{ deleteTarget?.productName }}</strong> 嗎？ <br /><span
                >此操作無法復原。</span
              >
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
.admin-page {
  min-height: 80vh;
}

.page-header {
  padding: 48px 0 36px;
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
  font-size: clamp(2rem, 4vw, 3rem);
}

.admin-content {
  padding-top: 32px;
  padding-bottom: 80px;
}

/* Alert */
.alert-error {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--danger-dim);
  border: 1px solid rgba(239, 68, 68, 0.3);
  color: var(--danger);
  border-radius: var(--radius);
  padding: 12px 16px;
  font-size: 0.88rem;
  margin-bottom: 20px;
}

.alert-close {
  background: none;
  border: none;
  color: var(--danger);
  cursor: pointer;
  font-size: 0.8rem;
}

/* Toolbar */
.toolbar {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.search-wrap {
  position: relative;
  flex: 1;
  max-width: 360px;
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
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

.search-input:focus {
  border-color: var(--accent);
}
.search-input::placeholder {
  color: var(--text-muted);
}

.total-count {
  font-size: 0.82rem;
  color: var(--text-muted);
  margin-left: auto;
}

/* Table */
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

.products-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 800px;
}

.products-table th {
  padding: 14px 16px;
  text-align: left;
  font-size: 0.68rem;
  font-weight: 600;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  color: var(--text-muted);
  background: var(--surface);
  border-bottom: 1px solid var(--border);
  white-space: nowrap;
}

.table-row {
  transition: background 0.15s ease;
}

.table-row:hover {
  background: rgba(255, 255, 255, 0.025);
}

.table-row td {
  padding: 14px 16px;
  border-bottom: 1px solid var(--border);
  font-size: 0.88rem;
  vertical-align: middle;
}

.table-row:last-child td {
  border-bottom: none;
}

.td-id {
  color: var(--text-muted);
  font-size: 0.8rem;
}

.name-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.name-avatar {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Playfair Display', serif;
  font-size: 1.1rem;
  font-weight: 500;
  flex-shrink: 0;
}

.name-text {
  font-weight: 500;
  color: var(--text);
  margin-bottom: 2px;
}

.name-desc {
  font-size: 0.75rem;
  color: var(--text-muted);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 200px;
}

.cat-badge {
  display: inline-flex;
  padding: 3px 10px;
  border-radius: 99px;
  font-size: 0.75rem;
  font-weight: 600;
  letter-spacing: 0.02em;
  white-space: nowrap;
}

.td-price {
  font-weight: 600;
  color: var(--accent);
  white-space: nowrap;
}

.stock-num {
  font-weight: 600;
}

.stock-num.in {
  color: #22c55e;
}
.stock-num.low {
  color: #fbbf24;
}
.stock-num.out {
  color: #ef4444;
}

.td-date {
  color: var(--text-dim);
  white-space: nowrap;
}

.td-action {
  display: flex;
  gap: 6px;
}

.action-btn {
  width: 32px;
  height: 32px;
  border-radius: 7px;
  border: 1px solid transparent;
  background: transparent;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: var(--transition);
}

.action-btn.edit {
  color: var(--text-muted);
}
.action-btn.edit:hover {
  background: rgba(96, 165, 250, 0.12);
  border-color: rgba(96, 165, 250, 0.3);
  color: #60a5fa;
}

.action-btn.delete {
  color: var(--text-muted);
}
.action-btn.delete:hover {
  background: var(--danger-dim);
  border-color: rgba(239, 68, 68, 0.3);
  color: var(--danger);
}

.empty-row {
  text-align: center;
  padding: 60px 20px !important;
  color: var(--text-muted);
  font-size: 0.9rem;
}

/* Modal */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.75);
  backdrop-filter: blur(4px);
  z-index: 2000;
  display: flex;
  align-items: center;
  justify-content: center;
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

.modal-sm {
  max-width: 400px;
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24px 24px 0;
  margin-bottom: 20px;
}

.modal-header h2 {
  font-size: 1.3rem;
}

.modal-close {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: none;
  border: 1px solid var(--border);
  color: var(--text-muted);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: var(--transition);
}

.modal-close:hover {
  border-color: var(--border-light);
  color: var(--text);
}

.modal-form {
  padding: 0 24px;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.form-group.full {
  grid-column: 1 / -1;
}

.form-label {
  font-size: 0.75rem;
  font-weight: 600;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  color: var(--text-dim);
}

.required {
  color: var(--accent);
}

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

.form-input:focus {
  border-color: var(--accent);
  background: var(--bg);
}
.form-input::placeholder {
  color: var(--text-muted);
}

.form-group.error .form-input {
  border-color: var(--danger);
}

.form-error {
  font-size: 0.75rem;
  color: var(--danger);
}

.textarea {
  resize: vertical;
  min-height: 80px;
  line-height: 1.5;
}

select.form-input {
  cursor: pointer;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 20px 24px;
  border-top: 1px solid var(--border);
  margin-top: 20px;
}

/* Delete modal */
.delete-body {
  padding: 8px 24px 4px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  text-align: center;
}

.delete-icon {
  color: #fbbf24;
  background: rgba(251, 191, 36, 0.1);
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.delete-msg {
  font-size: 0.92rem;
  color: var(--text-dim);
  line-height: 1.6;
}

.delete-msg strong {
  color: var(--text);
  font-weight: 600;
}
.delete-msg span {
  font-size: 0.82rem;
  color: var(--text-muted);
}

.mini-spinner {
  width: 14px;
  height: 14px;
  border: 2px solid rgba(0, 0, 0, 0.3);
  border-top-color: #0d0d10;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
  display: inline-block;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* Modal transition */
.modal-enter-active {
  transition: opacity 0.2s ease;
}
.modal-leave-active {
  transition: opacity 0.15s ease;
}
.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}
.modal-enter-active .modal-box {
  transition: transform 0.25s cubic-bezier(0.34, 1.56, 0.64, 1);
}
.modal-enter-from .modal-box {
  transform: scale(0.94) translateY(8px);
}

@media (max-width: 600px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
  .form-group.full {
    grid-column: 1;
  }
}
</style>
