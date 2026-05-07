package com.jason.springbootmall.dao.impl;

import com.jason.springbootmall.dao.CourtDao;
import com.jason.springbootmall.dto.CourtQueryParams;
import com.jason.springbootmall.dto.CourtRequest;
import com.jason.springbootmall.model.Court;
import com.jason.springbootmall.rowmapper.CourtRowMapper;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class CourtDaoImpl implements CourtDao {

  @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public Integer countCourt(CourtQueryParams params) {
    String sql = "SELECT count(1) FROM court WHERE 1=1";
    Map<String, Object> map = new HashMap<>();
    sql = addFilteringSql(sql, map, params);
    return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
  }

  @Override
  public List<Court> getCourts(CourtQueryParams params) {
    String sql =
        "SELECT court_id, court_name, court_type, location, price_per_hour, status, "
            + "description, image_url, created_date, last_modified_date "
            + "FROM court WHERE 1=1";

    Map<String, Object> map = new HashMap<>();
    sql = addFilteringSql(sql, map, params);
    sql = sql + " ORDER BY " + params.getOrderBy() + " " + params.getSort();
    sql = sql + " LIMIT :limit OFFSET :offset";
    map.put("limit", params.getLimit());
    map.put("offset", params.getOffset());

    return namedParameterJdbcTemplate.query(sql, map, new CourtRowMapper());
  }

  @Override
  public Court getCourtById(Integer courtId) {
    String sql =
        "SELECT court_id, court_name, court_type, location, price_per_hour, status, "
            + "description, image_url, created_date, last_modified_date "
            + "FROM court WHERE court_id = :courtId";

    Map<String, Object> map = new HashMap<>();
    map.put("courtId", courtId);

    List<Court> list = namedParameterJdbcTemplate.query(sql, map, new CourtRowMapper());
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public Integer createCourt(CourtRequest courtRequest) {
    String sql =
        "INSERT INTO court(court_name, court_type, location, price_per_hour, status, "
            + "description, image_url, created_date, last_modified_date) "
            + "VALUES(:courtName, :courtType, :location, :pricePerHour, :status, "
            + ":description, :imageUrl, :createdDate, :lastModifiedDate)";

    Map<String, Object> map = new HashMap<>();
    map.put("courtName", courtRequest.getCourtName());
    map.put("courtType", courtRequest.getCourtType().name());
    map.put("location", courtRequest.getLocation());
    map.put("pricePerHour", courtRequest.getPricePerHour());
    map.put("status", courtRequest.getStatus().name());
    map.put("description", courtRequest.getDescription());
    map.put("imageUrl", courtRequest.getImageUrl());

    Date now = new Date();
    map.put("createdDate", now);
    map.put("lastModifiedDate", now);

    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
    return Objects.requireNonNull(keyHolder.getKey()).intValue();
  }

  @Override
  public void updateCourt(Integer courtId, CourtRequest courtRequest) {
    String sql =
        "UPDATE court SET court_name = :courtName, court_type = :courtType, "
            + "location = :location, price_per_hour = :pricePerHour, status = :status, "
            + "description = :description, image_url = :imageUrl, "
            + "last_modified_date = :lastModifiedDate "
            + "WHERE court_id = :courtId";

    Map<String, Object> map = new HashMap<>();
    map.put("courtId", courtId);
    map.put("courtName", courtRequest.getCourtName());
    map.put("courtType", courtRequest.getCourtType().name());
    map.put("location", courtRequest.getLocation());
    map.put("pricePerHour", courtRequest.getPricePerHour());
    map.put("status", courtRequest.getStatus().name());
    map.put("description", courtRequest.getDescription());
    map.put("imageUrl", courtRequest.getImageUrl());
    map.put("lastModifiedDate", new Date());

    namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public void deleteCourtById(Integer courtId) {
    String sql = "DELETE FROM court WHERE court_id = :courtId";
    Map<String, Object> map = new HashMap<>();
    map.put("courtId", courtId);
    namedParameterJdbcTemplate.update(sql, map);
  }

  private String addFilteringSql(String sql, Map<String, Object> map, CourtQueryParams params) {
    if (params.getCourtType() != null) {
      sql = sql + " AND court_type = :courtType";
      map.put("courtType", params.getCourtType().name());
    }
    if (params.getStatus() != null) {
      sql = sql + " AND status = :status";
      map.put("status", params.getStatus().name());
    }
    if (params.getSearch() != null) {
      sql = sql + " AND court_name LIKE :search";
      map.put("search", "%" + params.getSearch() + "%");
    }
    return sql;
  }
}
