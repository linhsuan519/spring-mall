package com.jason.springbootmall.dto;

import com.jason.springbootmall.constant.CourtStatus;
import com.jason.springbootmall.constant.CourtType;
import jakarta.validation.constraints.NotNull;

public class CourtRequest {

  @NotNull private String courtName;
  @NotNull private CourtType courtType;
  @NotNull private String location;
  @NotNull private Integer pricePerHour;
  private CourtStatus status = CourtStatus.AVAILABLE;
  private String description;
  private String imageUrl;

  public String getCourtName() {
    return courtName;
  }

  public void setCourtName(String courtName) {
    this.courtName = courtName;
  }

  public CourtType getCourtType() {
    return courtType;
  }

  public void setCourtType(CourtType courtType) {
    this.courtType = courtType;
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

  public void setStatus(CourtStatus status) {
    this.status = status;
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
}
