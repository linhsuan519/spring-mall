package com.jason.springbootmall.dao.impl;

import com.jason.springbootmall.constant.ParticipantStatus;
import com.jason.springbootmall.dao.ActivityParticipantDao;
import com.jason.springbootmall.model.ActivityParticipant;
import com.jason.springbootmall.rowmapper.ActivityParticipantRowMapper;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class ActivityParticipantDaoImpl implements ActivityParticipantDao {

  @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public Integer joinActivity(Integer activityId, Integer userId) {
    String sql =
        """
                INSERT INTO activity_participant (activity_id, user_id, status, joined_at)
                VALUES (:activityId, :userId, 'APPROVED', :joinedAt)
                """;
    Map<String, Object> map = new HashMap<>();
    map.put("activityId", activityId);
    map.put("userId", userId);
    map.put("joinedAt", new Date());

    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
    return keyHolder.getKey().intValue();
  }

  @Override
  public ActivityParticipant getParticipant(Integer activityId, Integer userId) {
    String sql =
        """
                SELECT ap.*, u.email AS user_email
                FROM activity_participant ap
                JOIN user u ON ap.user_id = u.user_id
                WHERE ap.activity_id = :activityId AND ap.user_id = :userId
                """;
    List<ActivityParticipant> list =
        namedParameterJdbcTemplate.query(
            sql,
            Map.of("activityId", activityId, "userId", userId),
            new ActivityParticipantRowMapper());
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public ActivityParticipant getParticipantById(Integer participantId) {
    String sql =
        """
                SELECT ap.*, u.email AS user_email
                FROM activity_participant ap
                JOIN user u ON ap.user_id = u.user_id
                WHERE ap.participant_id = :participantId
                """;
    List<ActivityParticipant> list =
        namedParameterJdbcTemplate.query(
            sql, Map.of("participantId", participantId), new ActivityParticipantRowMapper());
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public List<ActivityParticipant> getParticipantsByActivityId(Integer activityId) {
    String sql =
        """
                SELECT ap.*, u.email AS user_email
                FROM activity_participant ap
                JOIN user u ON ap.user_id = u.user_id
                WHERE ap.activity_id = :activityId
                ORDER BY ap.joined_at ASC
                """;
    return namedParameterJdbcTemplate.query(
        sql, Map.of("activityId", activityId), new ActivityParticipantRowMapper());
  }

  @Override
  public List<ActivityParticipant> getActivitiesByUserId(Integer userId) {
    String sql =
        """
                SELECT ap.*, u.email AS user_email
                FROM activity_participant ap
                JOIN user u ON ap.user_id = u.user_id
                WHERE ap.user_id = :userId
                ORDER BY ap.joined_at DESC
                """;
    return namedParameterJdbcTemplate.query(
        sql, Map.of("userId", userId), new ActivityParticipantRowMapper());
  }

  @Override
  public void updateStatus(Integer participantId, ParticipantStatus status) {
    String sql =
        "UPDATE activity_participant SET status = :status WHERE participant_id = :participantId";
    namedParameterJdbcTemplate.update(
        sql, Map.of("status", status.name(), "participantId", participantId));
  }

  @Override
  public void deleteParticipant(Integer activityId, Integer userId) {
    String sql =
        "DELETE FROM activity_participant WHERE activity_id = :activityId AND user_id = :userId";
    namedParameterJdbcTemplate.update(sql, Map.of("activityId", activityId, "userId", userId));
  }
}
