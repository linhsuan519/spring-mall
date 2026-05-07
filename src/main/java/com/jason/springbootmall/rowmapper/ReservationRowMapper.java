package com.jason.springbootmall.rowmapper;

import com.jason.springbootmall.model.Reservation;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ReservationRowMapper implements RowMapper<Reservation> {

  @Override
  public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
    Reservation reservation = new Reservation();
    reservation.setReservationId(rs.getInt("reservation_id"));
    reservation.setUserId(rs.getInt("user_id"));
    reservation.setCourtId(rs.getInt("court_id"));
    reservation.setReservationDate(rs.getDate("reservation_date"));
    reservation.setStartTime(rs.getTime("start_time"));
    reservation.setEndTime(rs.getTime("end_time"));
    reservation.setTotalPrice(rs.getInt("total_price"));
    reservation.setStatus(rs.getString("status"));
    reservation.setCreatedDate(rs.getTimestamp("created_date"));
    reservation.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

    // join court 時帶入（欄位不存在時忽略）
    try {
      reservation.setCourtName(rs.getString("court_name"));
      reservation.setLocation(rs.getString("location"));
    } catch (SQLException ignored) {
    }

    return reservation;
  }
}
