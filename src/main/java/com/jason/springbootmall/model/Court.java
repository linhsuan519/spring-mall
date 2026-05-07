package com.jason.springbootmall.model;

import com.jason.springbootmall.constant.CourtStatus;
import com.jason.springbootmall.constant.CourtType;
import java.util.Date;

public class Court {

  private Integer courtId;
  private String courtName;
  private CourtType courtType;
  private String location;
  private Integer pricePerHour;
  private CourtStatus status;
  private String description;
  private String imageUrl;
  private Date createdDate;
  private Date lastModifiedDate;

  public Integer getCourtId() {
    return courtId;
  }

  public void setCourtId(Integer courtId) {
    this.courtId = courtId;
  }

  public String getCourtName() {
    return courtName;
  }

  public void setCourtName(String courtName) {
    this.courtName = courtName;
  }

  public CourtType getCourtType() {
    return courtType;
  }

  public void setCourtType(String courtType) {
    this.courtType = CourtType.valueOf(courtType);
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Integer getPricePerHour() {
    return pricePerHour;
  }

  public void setPricePerHour(Integer pricePerHour) {
    this.pricePerHour = pricePerHour;
  }

  public CourtStatus getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = CourtStatus.valueOf(status);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
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
