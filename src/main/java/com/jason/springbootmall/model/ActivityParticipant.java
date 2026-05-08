package com.jason.springbootmall.model;

import com.jason.springbootmall.constant.ParticipantStatus;
import java.util.Date;

public class ActivityParticipant {
  private Integer participantId;
  private Integer activityId;
  private Integer userId;
  private String userEmail;
  private ParticipantStatus status;
  private Date joinedAt;

  public Integer getParticipantId() {
    return participantId;
  }

  public void setParticipantId(Integer participantId) {
    this.participantId = participantId;
  }

  public Integer getActivityId() {
    return activityId;
  }

  public void setActivityId(Integer activityId) {
    this.activityId = activityId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public ParticipantStatus getStatus() {
    return status;
  }

  public void setStatus(ParticipantStatus status) {
    this.status = status;
  }

  public Date getJoinedAt() {
    return joinedAt;
  }

  public void setJoinedAt(Date joinedAt) {
    this.joinedAt = joinedAt;
  }
}
