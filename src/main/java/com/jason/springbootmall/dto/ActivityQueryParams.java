package com.jason.springbootmall.dto;

import com.jason.springbootmall.constant.ActivityStatus;
import com.jason.springbootmall.constant.SkillLevel;

public class ActivityQueryParams {
  private SkillLevel skillLevel;
  private ActivityStatus status;
  private Integer limit = 20;
  private Integer offset = 0;

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
