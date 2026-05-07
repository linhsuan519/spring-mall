package com.jason.springbootmall.dao;

import com.jason.springbootmall.dto.CreateReservationRequest;
import com.jason.springbootmall.dto.ReservationQueryParams;
import com.jason.springbootmall.model.Reservation;
import java.util.List;

public interface ReservationDao {

  Integer countReservation(ReservationQueryParams params);

  List<Reservation> getReservations(ReservationQueryParams params);

  Reservation getReservationById(Integer reservationId);

  boolean hasConflict(CreateReservationRequest request, Integer excludeReservationId);

  Integer createReservation(Integer userId, CreateReservationRequest request, Integer totalPrice);

  void updateStatus(Integer reservationId, String status);
}
