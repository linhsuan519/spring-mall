import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useCartStore = defineStore('cart', () => {
  const items = ref(JSON.parse(localStorage.getItem('cart') || '[]'))

  const save = () => localStorage.setItem('cart', JSON.stringify(items.value))

  const totalItems = computed(() => items.value.reduce((s, i) => s + i.qty, 0))

  const totalPrice = computed(() => items.value.reduce((s, i) => s + i.price * i.qty, 0))

  function addItem(product) {
    const existing = items.value.find((i) => i.productId === product.productId)
    if (existing) {
      existing.qty = Math.min(existing.qty + 1, product.stock)
    } else {
      items.value.push({
        productId: product.productId,
        productName: product.productName,
        imageUrl: product.imageUrl,
        price: product.price,
        category: product.category,
        stock: product.stock,
        qty: 1,
      })
    }
    save()
  }

  function updateQty(productId, qty) {
    const item = items.value.find((i) => i.productId === productId)
    if (!item) return
    if (qty <= 0) {
      removeItem(productId)
    } else {
      item.qty = Math.min(qty, item.stock)
      save()
    }
  }

  function removeItem(productId) {
    items.value = items.value.filter((i) => i.productId !== productId)
    save()
  }

  function clearCart() {
    items.value = []
    save()
  }

  return { items, totalItems, totalPrice, addItem, updateQty, removeItem, clearCart }
})
