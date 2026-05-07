package com.jason.springbootmall.dto;

import jakarta.validation.constraints.NotNull;

public class CreateReservationRequest {

  @NotNull private Integer courtId;

  // 格式: yyyy-MM-dd
  @NotNull private String reservationDate;

  // 格式: HH:mm
  @NotNull private String startTime;

  @NotNull private String endTime;

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

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }
}
