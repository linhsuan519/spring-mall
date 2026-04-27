package com.jason.springbootmall.dao.impl;

import com.jason.springbootmall.dao.UserDao;
import com.jason.springbootmall.model.User;
import com.jason.springbootmall.model.UserOauth;
import com.jason.springbootmall.rowmapper.UserRowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class UserDaoImpl implements UserDao {

  @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public User getUserById(Integer userId) {
    String sql =
        "SELECT user_id, email, password, role, created_date, last_modified_date "
            + "FROM `user` WHERE user_id = :userId";

    Map<String, Object> map = new HashMap<>();
    map.put("userId", userId);

    List<User> list = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public User getUserByEmail(String email) {
    String sql =
        "SELECT user_id, email, password, role, created_date, last_modified_date "
            + "FROM `user` WHERE email = :email";

    Map<String, Object> map = new HashMap<>();
    map.put("email", email);

    List<User> list = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public Integer createUser(String email, String encodedPassword) {
    String sql =
        "INSERT INTO `user` (email, password, role, created_date, last_modified_date) "
            + "VALUES (:email, :password, 'ROLE_USER', :createdDate, :lastModifiedDate)";

    Map<String, Object> map = new HashMap<>();
    map.put("email", email);
    map.put("password", encodedPassword);
    Date now = new Date();
    map.put("createdDate", now);
    map.put("lastModifiedDate", now);

    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
    return Objects.requireNonNull(keyHolder.getKey()).intValue();
  }

  @Override
  public UserOauth getOauthByProvider(String provider, String providerUserId) {
    String sql =
        "SELECT oauth_id, user_id, provider, provider_user_id "
            + "FROM user_oauth WHERE provider = :provider AND provider_user_id = :providerUserId";

    Map<String, Object> map = new HashMap<>();
    map.put("provider", provider);
    map.put("providerUserId", providerUserId);

    List<UserOauth> list =
        namedParameterJdbcTemplate.query(
            sql,
            map,
            new RowMapper<UserOauth>() {
              @Override
              public UserOauth mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserOauth o = new UserOauth();
                o.setOauthId(rs.getInt("oauth_id"));
                o.setUserId(rs.getInt("user_id"));
                o.setProvider(rs.getString("provider"));
                o.setProviderUserId(rs.getString("provider_user_id"));
                return o;
              }
            });
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public void createOauth(Integer userId, String provider, String providerUserId) {
    String sql =
        "INSERT INTO user_oauth (user_id, provider, provider_user_id) "
            + "VALUES (:userId, :provider, :providerUserId)";

    Map<String, Object> map = new HashMap<>();
    map.put("userId", userId);
    map.put("provider", provider);
    map.put("providerUserId", providerUserId);

    namedParameterJdbcTemplate.update(sql, map);
  }
}
