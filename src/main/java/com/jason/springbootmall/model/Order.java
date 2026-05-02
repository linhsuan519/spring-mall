package com.jason.springbootmall.model;

import java.util.Date;

public class Order {
  private Integer ordertId;
  private Integer userId;
  private Integer totalAmount;
  private Date createdDate;
  private Date lastModifiedDate;

  public Integer getOrdertId() {
    return ordertId;
  }

  public void setOrdertId(Integer ordertId) {
    this.ordertId = ordertId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(Integer totalAmount) {
    this.totalAmount = totalAmount;
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
}
