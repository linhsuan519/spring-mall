package com.jason.springbootmall.model;

import com.jason.springbootmall.constant.ReservationStatus;
import java.sql.Time;
import java.util.Date;

public class Reservation {

  private Integer reservationId;
  private Integer userId;
  private Integer courtId;
  private Date reservationDate;
  private Time startTime;
  private Time endTime;
  private Integer totalPrice;
  private ReservationStatus status;
  private Date createdDate;
  private Date lastModifiedDate;

  // 查詢時 join 帶入的欄位
  private String courtName;
  private String location;

  public Integer getReservationId() {
    return reservationId;
  }

  public void setReservationId(Integer reservationId) {
    this.reservationId = reservationId;
  }

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

  public Date getReservationDate() {
    return reservationDate;
  }

  public void setReservationDate(Date reservationDate) {
    this.reservationDate = reservationDate;
  }

  public Time getStartTime() {
    return startTime;
  }

  public void setStartTime(Time startTime) {
    this.startTime = startTime;
  }

  public Time getEndTime() {
    return endTime;
  }

  public void setEndTime(Time endTime) {
    this.endTime = endTime;
  }

  public Integer getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Integer totalPrice) {
    this.totalPrice = totalPrice;
  }

  public ReservationStatus getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = ReservationStatus.valueOf(status);
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public String getCourtName() {
    return courtName;
  }

  public void setCourtName(String courtName) {
    this.courtName = courtName;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
