package com.jason.springbootmall.service;

import com.jason.springbootmall.dto.ActivityQueryParams;
import com.jason.springbootmall.dto.CreateActivityRequest;
import com.jason.springbootmall.model.Activity;
import com.jason.springbootmall.model.ActivityParticipant;
import com.jason.springbootmall.util.Page;
import java.util.List;

public interface ActivityService {

  Page<Activity> getActivities(ActivityQueryParams params);

  Activity getActivityById(Integer activityId);

  Integer createActivity(Integer hostUserId, CreateActivityRequest request);

  void cancelActivity(Integer activityId, Integer requestUserId, boolean isAdmin);

  void joinActivity(Integer activityId, Integer userId);

  void leaveActivity(Integer activityId, Integer userId);

  List<ActivityParticipant> getParticipants(
      Integer activityId, Integer requestUserId, boolean isAdmin);

  List<Activity> getHostedActivities(Integer userId);

  List<ActivityParticipant> getJoinedActivities(Integer userId);
}
