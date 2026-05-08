package com.jason.springbootmall.dto;

import com.jason.springbootmall.constant.ParticipantStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateParticipantRequest {

  @NotNull private ParticipantStatus status;

  public ParticipantStatus getStatus() {
    return status;
  }

  public void setStatus(ParticipantStatus status) {
    this.status = status;
  }
}
