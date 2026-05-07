import axios from 'axios'

const api = axios.create({
  baseURL: `${import.meta.env.VITE_API_BASE_URL || ''}/api/users`,
  timeout: 10000,
  withCredentials: true,
})

export const getReservations = (userId, params = {}) =>
  api.get(`/${userId}/reservations`, { params }).then((r) => r.data)

export const createReservation = (userId, data) =>
  api.post(`/${userId}/reservations`, data).then((r) => r.data)

export const cancelReservation = (userId, reservationId) =>
  api.delete(`/${userId}/reservations/${reservationId}`)
