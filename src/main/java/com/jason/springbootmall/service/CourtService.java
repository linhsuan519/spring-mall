package com.jason.springbootmall.service;

import com.jason.springbootmall.dto.CourtQueryParams;
import com.jason.springbootmall.dto.CourtRequest;
import com.jason.springbootmall.model.Court;
import java.util.List;

public interface CourtService {

  Integer countCourt(CourtQueryParams params);

  List<Court> getCourts(CourtQueryParams params);

  Court getCourtById(Integer courtId);

  Integer createCourt(CourtRequest courtRequest);

  void updateCourt(Integer courtId, CourtRequest courtRequest);

  void deleteCourtById(Integer courtId);
}
