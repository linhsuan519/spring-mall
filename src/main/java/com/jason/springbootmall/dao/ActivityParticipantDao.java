package com.jason.springbootmall.dao;

import com.jason.springbootmall.constant.ParticipantStatus;
import com.jason.springbootmall.model.ActivityParticipant;
import java.util.List;

public interface ActivityParticipantDao {

  Integer joinActivity(Integer activityId, Integer userId);

  ActivityParticipant getParticipant(Integer activityId, Integer userId);

  ActivityParticipant getParticipantById(Integer participantId);

  List<ActivityParticipant> getParticipantsByActivityId(Integer activityId);

  List<ActivityParticipant> getActivitiesByUserId(Integer userId);

  void updateStatus(Integer participantId, ParticipantStatus status);

  void deleteParticipant(Integer activityId, Integer userId);
}
