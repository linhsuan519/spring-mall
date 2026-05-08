package com.jason.springbootmall.model;

import com.jason.springbootmall.constant.ActivityStatus;
import com.jason.springbootmall.constant.SkillLevel;
import java.util.Date;

public class Activity {
  private Integer activityId;
  private Integer hostUserId;
  private String hostEmail;
  private String title;
  private String location;
  private Date scheduledTime;
  private Integer maxParticipants;
  private Integer currentParticipants;
  private SkillLevel skillLevel;
  private ActivityStatus status;
  private String description;
  private Date createdAt;

  public Integer getActivityId() {
    return activityId;
  }

  public void setActivityId(Integer activityId) {
    this.activityId = activityId;
  }

  public Integer getHostUserId() {
    return hostUserId;
  }

  public void setHostUserId(Integer hostUserId) {
    this.hostUserId = hostUserId;
  }

  public String getHostEmail() {
    return hostEmail;
  }

  public void setHostEmail(String hostEmail) {
    this.hostEmail = hostEmail;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Date getScheduledTime() {
    return scheduledTime;
  }

  public void setScheduledTime(Date scheduledTime) {
    this.scheduledTime = scheduledTime;
  }

  public Integer getMaxParticipants() {
    return maxParticipants;
  }

  public void setMaxParticipants(Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
  }

  public Integer getCurrentParticipants() {
    return currentParticipants;
  }

  public void setCurrentParticipants(Integer currentParticipants) {
    this.currentParticipants = currentParticipants;
  }

  public SkillLevel getSkillLevel() {
    return skillLevel;
  }

  public void setSkillLevel(SkillLevel skillLevel) {
    this.skillLevel = skillLevel;
  }

  public ActivityStatus getStatus() {
    return status;
  }

  public void setStatus(ActivityStatus status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }
}
