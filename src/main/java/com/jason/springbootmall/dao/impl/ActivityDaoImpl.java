package com.jason.springbootmall.dao.impl;

import com.jason.springbootmall.constant.ActivityStatus;
import com.jason.springbootmall.dao.ActivityDao;
import com.jason.springbootmall.dto.ActivityQueryParams;
import com.jason.springbootmall.dto.CreateActivityRequest;
import com.jason.springbootmall.model.Activity;
import com.jason.springbootmall.rowmapper.ActivityRowMapper;
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
public class ActivityDaoImpl implements ActivityDao {

  @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public Integer createActivity(Integer hostUserId, CreateActivityRequest request) {
    String sql =
        """
                INSERT INTO activity (host_user_id, title, location, scheduled_time,
                    max_participants, current_participants, skill_level, status, description, created_at)
                VALUES (:hostUserId, :title, :location, :scheduledTime,
                    :maxParticipants, 1, :skillLevel, 'OPEN', :description, :createdAt)
                """;

    Map<String, Object> map = new HashMap<>();
    map.put("hostUserId", hostUserId);
    map.put("title", request.getTitle());
    map.put("location", request.getLocation());
    map.put("scheduledTime", request.getScheduledTime());
    map.put("maxParticipants", request.getMaxParticipants());
    map.put("skillLevel", request.getSkillLevel().name());
    map.put("description", request.getDescription());
    map.put("createdAt", new Date());

    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
    return keyHolder.getKey().intValue();
  }

  @Override
  public Activity getActivityById(Integer activityId) {
    String sql =
        """
                SELECT a.*, u.email AS host_email
                FROM activity a
                JOIN user u ON a.host_user_id = u.user_id
                WHERE a.activity_id = :activityId
                """;
    Map<String, Object> map = Map.of("activityId", activityId);
    List<Activity> list = namedParameterJdbcTemplate.query(sql, map, new ActivityRowMapper());
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public List<Activity> getActivities(ActivityQueryParams params) {
    String sql =
        buildSelectSql(params) + " ORDER BY a.scheduled_time ASC LIMIT :limit OFFSET :offset";
    Map<String, Object> map = buildParamMap(params);
    map.put("limit", params.getLimit());
    map.put("offset", params.getOffset());
    return namedParameterJdbcTemplate.query(sql, map, new ActivityRowMapper());
  }

  @Override
  public Integer countActivities(ActivityQueryParams params) {
    String sql =
        "SELECT COUNT(*) FROM activity a JOIN user u ON a.host_user_id = u.user_id"
            + buildWhere(params);
    return namedParameterJdbcTemplate.queryForObject(sql, buildParamMap(params), Integer.class);
  }

  @Override
  public List<Activity> getActivitiesByHostUserId(Integer hostUserId) {
    String sql =
        """
                SELECT a.*, u.email AS host_email
                FROM activity a
                JOIN user u ON a.host_user_id = u.user_id
                WHERE a.host_user_id = :hostUserId
                ORDER BY a.scheduled_time DESC
                """;
    return namedParameterJdbcTemplate.query(
        sql, Map.of("hostUserId", hostUserId), new ActivityRowMapper());
  }

  @Override
  public void updateStatus(Integer activityId, ActivityStatus status) {
    String sql = "UPDATE activity SET status = :status WHERE activity_id = :activityId";
    namedParameterJdbcTemplate.update(
        sql, Map.of("status", status.name(), "activityId", activityId));
  }

  @Override
  public void incrementParticipants(Integer activityId) {
    String sql =
        """
                UPDATE activity SET current_participants = current_participants + 1,
                    status = CASE WHEN current_participants + 1 >= max_participants THEN 'FULL' ELSE status END
                WHERE activity_id = :activityId
                """;
    namedParameterJdbcTemplate.update(sql, Map.of("activityId", activityId));
  }

  @Override
  public void decrementParticipants(Integer activityId) {
    String sql =
        """
                UPDATE activity SET current_participants = current_participants - 1,
                    status = CASE WHEN status = 'FULL' THEN 'OPEN' ELSE status END
                WHERE activity_id = :activityId
                """;
    namedParameterJdbcTemplate.update(sql, Map.of("activityId", activityId));
  }

  private String buildSelectSql(ActivityQueryParams params) {
    return "SELECT a.*, u.email AS host_email FROM activity a JOIN user u ON a.host_user_id = u.user_id"
        + buildWhere(params);
  }

  private String buildWhere(ActivityQueryParams params) {
    StringBuilder sb = new StringBuilder(" WHERE 1=1");
    if (params.getSkillLevel() != null) sb.append(" AND a.skill_level = :skillLevel");
    if (params.getStatus() != null) sb.append(" AND a.status = :status");
    return sb.toString();
  }

  private Map<String, Object> buildParamMap(ActivityQueryParams params) {
    Map<String, Object> map = new HashMap<>();
    if (params.getSkillLevel() != null) map.put("skillLevel", params.getSkillLevel().name());
    if (params.getStatus() != null) map.put("status", params.getStatus().name());
    return map;
  }
}
