package com.jason.springbootmall.dto;

import com.jason.springbootmall.constant.SkillLevel;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

public class CreateActivityRequest {

  @NotBlank private String title;

  @NotBlank private String location;

  @NotNull @Future private Date scheduledTime;

  @NotNull
  @Min(2)
  @Max(20)
  private Integer maxParticipants;

  @NotNull private SkillLevel skillLevel;

  private String description;

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

  public SkillLevel getSkillLevel() {
    return skillLevel;
  }

  public void setSkillLevel(SkillLevel skillLevel) {
    this.skillLevel = skillLevel;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
