package com.jason.springbootmall.dao;

import com.jason.springbootmall.constant.ActivityStatus;
import com.jason.springbootmall.dto.ActivityQueryParams;
import com.jason.springbootmall.dto.CreateActivityRequest;
import com.jason.springbootmall.model.Activity;
import java.util.List;

public interface ActivityDao {

  Integer createActivity(Integer hostUserId, CreateActivityRequest request);

  Activity getActivityById(Integer activityId);

  List<Activity> getActivities(ActivityQueryParams params);

  Integer countActivities(ActivityQueryParams params);

  List<Activity> getActivitiesByHostUserId(Integer hostUserId);

  void updateStatus(Integer activityId, ActivityStatus status);

  void incrementParticipants(Integer activityId);

  void decrementParticipants(Integer activityId);
}
