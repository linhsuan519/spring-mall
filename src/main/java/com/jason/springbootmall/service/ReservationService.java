package com.jason.springbootmall.service;

import com.jason.springbootmall.dto.CreateReservationRequest;
import com.jason.springbootmall.dto.ReservationQueryParams;
import com.jason.springbootmall.model.Reservation;
import java.util.List;

public interface ReservationService {

  Integer countReservation(ReservationQueryParams params);

  List<Reservation> getReservations(ReservationQueryParams params);

  Reservation getReservationById(Integer reservationId);

  Integer createReservation(Integer userId, CreateReservationRequest request);

  void cancelReservation(Integer reservationId, Integer userId);
}
