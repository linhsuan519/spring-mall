package com.jason.springbootmall.service.impl;

import com.jason.springbootmall.constant.CourtStatus;
import com.jason.springbootmall.constant.ReservationStatus;
import com.jason.springbootmall.dao.CourtDao;
import com.jason.springbootmall.dao.ReservationDao;
import com.jason.springbootmall.dao.UserDao;
import com.jason.springbootmall.dto.CreateReservationRequest;
import com.jason.springbootmall.dto.ReservationQueryParams;
import com.jason.springbootmall.model.Court;
import com.jason.springbootmall.model.Reservation;
import com.jason.springbootmall.model.User;
import com.jason.springbootmall.service.ReservationService;
import java.time.LocalTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ReservationServiceImpl implements ReservationService {

  private static final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

  @Autowired private ReservationDao reservationDao;
  @Autowired private CourtDao courtDao;
  @Autowired private UserDao userDao;

  @Override
  public Integer countReservation(ReservationQueryParams params) {
    return reservationDao.countReservation(params);
  }

  @Override
  public List<Reservation> getReservations(ReservationQueryParams params) {
    return reservationDao.getReservations(params);
  }

  @Override
  public Reservation getReservationById(Integer reservationId) {
    Reservation reservation = reservationDao.getReservationById(reservationId);
    if (reservation == null) {
      log.warn("預約 {} 不存在", reservationId);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return reservation;
  }

  @Transactional
  @Override
  public Integer createReservation(Integer userId, CreateReservationRequest request) {
    // 1. 確認 user 存在
    User user = userDao.getUserById(userId);
    if (user == null) {
      log.warn("userId {} 不存在", userId);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    // 2. 確認場地存在且可使用
    Court court = courtDao.getCourtById(request.getCourtId());
    if (court == null) {
      log.warn("場地 {} 不存在", request.getCourtId());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (court.getStatus() != CourtStatus.AVAILABLE) {
      log.warn("場地 {} 目前無法預約，狀態為 {}", request.getCourtId(), court.getStatus());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    // 3. 確認時間合法（開始時間必須早於結束時間）
    LocalTime start = LocalTime.parse(request.getStartTime());
    LocalTime end = LocalTime.parse(request.getEndTime());
    if (!start.isBefore(end)) {
      log.warn("開始時間 {} 必須早於結束時間 {}", request.getStartTime(), request.getEndTime());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    // 4. 確認時段無衝突
    if (reservationDao.hasConflict(request, null)) {
      log.warn(
          "場地 {} 在 {} {} ~ {} 已有預約衝突",
          request.getCourtId(),
          request.getReservationDate(),
          request.getStartTime(),
          request.getEndTime());
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    // 5. 計算總費用（以小時為單位，無條件進位）
    long minutes = java.time.Duration.between(start, end).toMinutes();
    int hours = (int) Math.ceil(minutes / 60.0);
    int totalPrice = court.getPricePerHour() * hours;

    return reservationDao.createReservation(userId, request, totalPrice);
  }

  @Override
  public void cancelReservation(Integer reservationId, Integer userId) {
    Reservation reservation = reservationDao.getReservationById(reservationId);
    if (reservation == null) {
      log.warn("預約 {} 不存在", reservationId);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // 只有預約本人可以取消
    if (!reservation.getUserId().equals(userId)) {
      log.warn("userId {} 無權取消預約 {}", userId, reservationId);
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    // 已取消的預約不能再操作
    if (reservation.getStatus() == ReservationStatus.CANCELLED) {
      log.warn("預約 {} 已經是取消狀態", reservationId);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    reservationDao.updateStatus(reservationId, ReservationStatus.CANCELLED.name());
  }
}
