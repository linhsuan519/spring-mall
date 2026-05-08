package com.jason.springbootmall.service.impl;

import com.jason.springbootmall.constant.ActivityStatus;
import com.jason.springbootmall.dao.ActivityDao;
import com.jason.springbootmall.dao.ActivityParticipantDao;
import com.jason.springbootmall.dto.ActivityQueryParams;
import com.jason.springbootmall.dto.CreateActivityRequest;
import com.jason.springbootmall.model.Activity;
import com.jason.springbootmall.model.ActivityParticipant;
import com.jason.springbootmall.service.ActivityService;
import com.jason.springbootmall.util.Page;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ActivityServiceImpl implements ActivityService {

  private static final Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);

  @Autowired private ActivityDao activityDao;
  @Autowired private ActivityParticipantDao participantDao;

  @Override
  public Page<Activity> getActivities(ActivityQueryParams params) {
    List<Activity> list = activityDao.getActivities(params);
    Integer total = activityDao.countActivities(params);
    Page<Activity> page = new Page<>();
    page.setResults(list);
    page.setTotal(total);
    page.setLimit(params.getLimit());
    page.setOffset(params.getOffset());
    return page;
  }

  @Override
  public Activity getActivityById(Integer activityId) {
    Activity activity = activityDao.getActivityById(activityId);
    if (activity == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found");
    }
    return activity;
  }

  @Override
  public Integer createActivity(Integer hostUserId, CreateActivityRequest request) {
    Integer activityId = activityDao.createActivity(hostUserId, request);
    // Host automatically joins as first participant
    participantDao.joinActivity(activityId, hostUserId);
    return activityId;
  }

  @Override
  public void cancelActivity(Integer activityId, Integer requestUserId, boolean isAdmin) {
    Activity activity = getActivityById(activityId);
    if (!isAdmin && !activity.getHostUserId().equals(requestUserId)) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN, "Only the host can cancel this activity");
    }
    activityDao.updateStatus(activityId, ActivityStatus.CANCELLED);
  }

  @Override
  @Transactional
  public void joinActivity(Integer activityId, Integer userId) {
    Activity activity = getActivityById(activityId);

    if (activity.getStatus() == ActivityStatus.CANCELLED) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activity is cancelled");
    }
    if (activity.getStatus() == ActivityStatus.FULL) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activity is full");
    }
    if (activity.getStatus() == ActivityStatus.COMPLETED) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activity has ended");
    }

    ActivityParticipant existing = participantDao.getParticipant(activityId, userId);
    if (existing != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already joined this activity");
    }

    participantDao.joinActivity(activityId, userId);
    activityDao.incrementParticipants(activityId);
  }

  @Override
  @Transactional
  public void leaveActivity(Integer activityId, Integer userId) {
    Activity activity = getActivityById(activityId);

    if (activity.getHostUserId().equals(userId)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Host cannot leave. Cancel the activity instead.");
    }

    ActivityParticipant existing = participantDao.getParticipant(activityId, userId);
    if (existing == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Not a participant of this activity");
    }

    participantDao.deleteParticipant(activityId, userId);
    activityDao.decrementParticipants(activityId);
  }

  @Override
  public List<ActivityParticipant> getParticipants(
      Integer activityId, Integer requestUserId, boolean isAdmin) {
    Activity activity = getActivityById(activityId);
    if (!isAdmin && !activity.getHostUserId().equals(requestUserId)) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN, "Only the host can view participants");
    }
    return participantDao.getParticipantsByActivityId(activityId);
  }

  @Override
  public List<Activity> getHostedActivities(Integer userId) {
    return activityDao.getActivitiesByHostUserId(userId);
  }

  @Override
  public List<ActivityParticipant> getJoinedActivities(Integer userId) {
    return participantDao.getActivitiesByUserId(userId);
  }
}
