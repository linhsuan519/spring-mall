package com.jason.springbootmall.rowmapper;

import com.jason.springbootmall.model.Court;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class CourtRowMapper implements RowMapper<Court> {

  @Override
  public Court mapRow(ResultSet rs, int rowNum) throws SQLException {
    Court court = new Court();
    court.setCourtId(rs.getInt("court_id"));
    court.setCourtName(rs.getString("court_name"));
    court.setCourtType(rs.getString("court_type"));
    court.setLocation(rs.getString("location"));
    court.setPricePerHour(rs.getInt("price_per_hour"));
    court.setStatus(rs.getString("status"));
    court.setDescription(rs.getString("description"));
    court.setImageUrl(rs.getString("image_url"));
    court.setCreatedDate(rs.getTimestamp("created_date"));
    court.setLastModifiedDate(rs.getTimestamp("last_modified_date"));
    return court;
  }
}
