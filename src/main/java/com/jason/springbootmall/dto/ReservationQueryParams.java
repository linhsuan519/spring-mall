package com.jason.springbootmall.dto;

import com.jason.springbootmall.constant.ReservationStatus;

public class ReservationQueryParams {

  private Integer userId;
  private Integer courtId;
  private String reservationDate;
  private ReservationStatus status;
  private Integer limit;
  private Integer offset;

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getCourtId() {
    return courtId;
  }

  public void setCourtId(Integer courtId) {
    this.courtId = courtId;
  }

  public String getReservationDate() {
    return reservationDate;
  }

  public void setReservationDate(String reservationDate) {
    this.reservationDate = reservationDate;
  }

  public ReservationStatus getStatus() {
    return status;
  }

  public void setStatus(ReservationStatus status) {
    this.status = status;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }
}
