package com.jason.springbootmall.controller;

import com.jason.springbootmall.dao.UserDao;
import com.jason.springbootmall.dto.CreateReservationRequest;
import com.jason.springbootmall.dto.ReservationQueryParams;
import com.jason.springbootmall.model.Reservation;
import com.jason.springbootmall.model.User;
import com.jason.springbootmall.service.ReservationService;
import com.jason.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Valid
@RestController
public class ReservationController {

  @Autowired private ReservationService reservationService;
  @Autowired private UserDao userDao;

  @GetMapping("/users/{userId}/reservations")
  public ResponseEntity<Page<Reservation>> getReservations(
      @PathVariable Integer userId,
      @RequestParam(defaultValue = "10") @Max(100) @Min(1) Integer limit,
      @RequestParam(defaultValue = "0") @Min(0) Integer offset,
      Authentication authentication) {

    verifyOwner(userId, authentication);

    ReservationQueryParams params = new ReservationQueryParams();
    params.setUserId(userId);
    params.setLimit(limit);
    params.setOffset(offset);

    List<Reservation> list = reservationService.getReservations(params);
    Integer total = reservationService.countReservation(params);

    Page<Reservation> page = new Page<>();
    page.setLimit(limit);
    page.setOffset(offset);
    page.setTotal(total);
    page.setResults(list);

    return ResponseEntity.status(HttpStatus.OK).body(page);
  }

  @PostMapping("/users/{userId}/reservations")
  public ResponseEntity<Reservation> createReservation(
      @PathVariable Integer userId,
      @RequestBody @Valid CreateReservationRequest request,
      Authentication authentication) {

    verifyOwner(userId, authentication);

    Integer reservationId = reservationService.createReservation(userId, request);
    Reservation reservation = reservationService.getReservationById(reservationId);

    return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
  }

  @DeleteMapping("/users/{userId}/reservations/{reservationId}")
  public ResponseEntity<?> cancelReservation(
      @PathVariable Integer userId,
      @PathVariable Integer reservationId,
      Authentication authentication) {

    verifyOwner(userId, authentication);

    reservationService.cancelReservation(reservationId, userId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  // 驗證 path 中的 userId 必須與當前登入者一致
  private void verifyOwner(Integer userId, Authentication authentication) {
    User loginUser = userDao.getUserByEmail(authentication.getName());
    if (loginUser == null || !loginUser.getUserId().equals(userId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
  }
}
