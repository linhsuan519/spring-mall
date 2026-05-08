package com.jason.springbootmall.rowmapper;

import com.jason.springbootmall.constant.ActivityStatus;
import com.jason.springbootmall.constant.SkillLevel;
import com.jason.springbootmall.model.Activity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ActivityRowMapper implements RowMapper<Activity> {

  @Override
  public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
    Activity a = new Activity();
    a.setActivityId(rs.getInt("activity_id"));
    a.setHostUserId(rs.getInt("host_user_id"));
    a.setHostEmail(rs.getString("host_email"));
    a.setTitle(rs.getString("title"));
    a.setLocation(rs.getString("location"));
    a.setScheduledTime(rs.getTimestamp("scheduled_time"));
    a.setMaxParticipants(rs.getInt("max_participants"));
    a.setCurrentParticipants(rs.getInt("current_participants"));
    a.setSkillLevel(SkillLevel.valueOf(rs.getString("skill_level")));
    a.setStatus(ActivityStatus.valueOf(rs.getString("status")));
    a.setDescription(rs.getString("description"));
    a.setCreatedAt(rs.getTimestamp("created_at"));
    return a;
  }
}
