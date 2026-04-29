package com.jason.springbootmall.rowmapper;

import com.jason.springbootmall.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {

  @Override
  public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    User user = new User();
    user.setUserId(rs.getInt("user_id"));
    user.setRole(rs.getString("role"));
    user.setEmail(rs.getString("email"));
    user.setPassWord(rs.getString("password"));
    user.setCreateDate(rs.getTimestamp("created_date"));
    user.setLastModifiedDate(rs.getTime("last_modified_date"));

    return user;
  }
}
