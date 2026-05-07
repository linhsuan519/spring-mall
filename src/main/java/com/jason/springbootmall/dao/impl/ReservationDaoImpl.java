package com.jason.springbootmall.dao.impl;

import com.jason.springbootmall.dao.ReservationDao;
import com.jason.springbootmall.dto.CreateReservationRequest;
import com.jason.springbootmall.dto.ReservationQueryParams;
import com.jason.springbootmall.model.Reservation;
import com.jason.springbootmall.rowmapper.ReservationRowMapper;
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
public class ReservationDaoImpl implements ReservationDao {

  @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public Integer countReservation(ReservationQueryParams params) {
    String sql = "SELECT count(1) FROM reservation WHERE 1=1";
    Map<String, Object> map = new HashMap<>();
    sql = addFilteringSql(sql, map, params);
    return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
  }

  @Override
  public List<Reservation> getReservations(ReservationQueryParams params) {
    String sql =
        "SELECT r.reservation_id, r.user_id, r.court_id, r.reservation_date, "
            + "r.start_time, r.end_time, r.total_price, r.status, "
            + "r.created_date, r.last_modified_date, c.court_name, c.location "
            + "FROM reservation r "
            + "LEFT JOIN court c ON r.court_id = c.court_id "
            + "WHERE 1=1";

    Map<String, Object> map = new HashMap<>();
    sql = addFilteringSql(sql, map, params);
    sql = sql + " ORDER BY r.reservation_date DESC, r.start_time ASC";
    sql = sql + " LIMIT :limit OFFSET :offset";
    map.put("limit", params.getLimit());
    map.put("offset", params.getOffset());

    return namedParameterJdbcTemplate.query(sql, map, new ReservationRowMapper());
  }

  @Override
  public Reservation getReservationById(Integer reservationId) {
    String sql =
        "SELECT r.reservation_id, r.user_id, r.court_id, r.reservation_date, "
            + "r.start_time, r.end_time, r.total_price, r.status, "
            + "r.created_date, r.last_modified_date, c.court_name, c.location "
            + "FROM reservation r "
            + "LEFT JOIN court c ON r.court_id = c.court_id "
            + "WHERE r.reservation_id = :reservationId";

    Map<String, Object> map = new HashMap<>();
    map.put("reservationId", reservationId);

    List<Reservation> list = namedParameterJdbcTemplate.query(sql, map, new ReservationRowMapper());
    return list.isEmpty() ? null : list.get(0);
  }

  // 時段重疊條件: existing.start_time < new.end_time AND existing.end_time > new.start_time
  @Override
  public boolean hasConflict(CreateReservationRequest request, Integer excludeReservationId) {
    String sql =
        "SELECT count(1) FROM reservation "
            + "WHERE court_id = :courtId "
            + "AND reservation_date = :reservationDate "
            + "AND status != 'CANCELLED' "
            + "AND start_time < :endTime "
            + "AND end_time > :startTime";

    Map<String, Object> map = new HashMap<>();
    map.put("courtId", request.getCourtId());
    map.put("reservationDate", request.getReservationDate());
    map.put("startTime", request.getStartTime());
    map.put("endTime", request.getEndTime());

    if (excludeReservationId != null) {
      sql = sql + " AND reservation_id != :excludeId";
      map.put("excludeId", excludeReservationId);
    }

    Integer count = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
    return count != null && count > 0;
  }

  @Override
  public Integer createReservation(
      Integer userId, CreateReservationRequest request, Integer totalPrice) {
    String sql =
        "INSERT INTO reservation(user_id, court_id, reservation_date, start_time, end_time, "
            + "total_price, status, created_date, last_modified_date) "
            + "VALUES(:userId, :courtId, :reservationDate, :startTime, :endTime, "
            + ":totalPrice, 'PENDING', :createdDate, :lastModifiedDate)";

    Map<String, Object> map = new HashMap<>();
    map.put("userId", userId);
    map.put("courtId", request.getCourtId());
    map.put("reservationDate", request.getReservationDate());
    map.put("startTime", request.getStartTime());
    map.put("endTime", request.getEndTime());
    map.put("totalPrice", totalPrice);

    Date now = new Date();
    map.put("createdDate", now);
    map.put("lastModifiedDate", now);

    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
    return Objects.requireNonNull(keyHolder.getKey()).intValue();
  }

  @Override
  public void updateStatus(Integer reservationId, String status) {
    String sql =
        "UPDATE reservation SET status = :status, last_modified_date = :lastModifiedDate "
            + "WHERE reservation_id = :reservationId";

    Map<String, Object> map = new HashMap<>();
    map.put("reservationId", reservationId);
    map.put("status", status);
    map.put("lastModifiedDate", new Date());

    namedParameterJdbcTemplate.update(sql, map);
  }

  private String addFilteringSql(
      String sql, Map<String, Object> map, ReservationQueryParams params) {
    if (params.getUserId() != null) {
      sql = sql + " AND r.user_id = :userId";
      map.put("userId", params.getUserId());
    }
    if (params.getCourtId() != null) {
      sql = sql + " AND r.court_id = :courtId";
      map.put("courtId", params.getCourtId());
    }
    if (params.getReservationDate() != null) {
      sql = sql + " AND r.reservation_date = :reservationDate";
      map.put("reservationDate", params.getReservationDate());
    }
    if (params.getStatus() != null) {
      sql = sql + " AND r.status = :status";
      map.put("status", params.getStatus().name());
    }
    return sql;
  }
}
