package com.jason.springbootmall.dao;

import com.jason.springbootmall.dto.CourtQueryParams;
import com.jason.springbootmall.dto.CourtRequest;
import com.jason.springbootmall.model.Court;
import java.util.List;

public interface CourtDao {

  Integer countCourt(CourtQueryParams courtQueryParams);

  List<Court> getCourts(CourtQueryParams courtQueryParams);

  Court getCourtById(Integer courtId);

  Integer createCourt(CourtRequest courtRequest);

  void updateCourt(Integer courtId, CourtRequest courtRequest);

  void deleteCourtById(Integer courtId);
}
