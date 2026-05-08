package com.jason.springbootmall.rowmapper;

import com.jason.springbootmall.constant.ParticipantStatus;
import com.jason.springbootmall.model.ActivityParticipant;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ActivityParticipantRowMapper implements RowMapper<ActivityParticipant> {

  @Override
  public ActivityParticipant mapRow(ResultSet rs, int rowNum) throws SQLException {
    ActivityParticipant p = new ActivityParticipant();
    p.setParticipantId(rs.getInt("participant_id"));
    p.setActivityId(rs.getInt("activity_id"));
    p.setUserId(rs.getInt("user_id"));
    p.setUserEmail(rs.getString("user_email"));
    p.setStatus(ParticipantStatus.valueOf(rs.getString("status")));
    p.setJoinedAt(rs.getTimestamp("joined_at"));
    return p;
  }
}
