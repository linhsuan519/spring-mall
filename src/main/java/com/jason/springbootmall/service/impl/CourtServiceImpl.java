package com.jason.springbootmall.service.impl;

import com.jason.springbootmall.dao.CourtDao;
import com.jason.springbootmall.dto.CourtQueryParams;
import com.jason.springbootmall.dto.CourtRequest;
import com.jason.springbootmall.model.Court;
import com.jason.springbootmall.service.CourtService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CourtServiceImpl implements CourtService {

  private static final Logger log = LoggerFactory.getLogger(CourtServiceImpl.class);

  @Autowired private CourtDao courtDao;

  @Override
  public Integer countCourt(CourtQueryParams params) {
    return courtDao.countCourt(params);
  }

  @Override
  public List<Court> getCourts(CourtQueryParams params) {
    return courtDao.getCourts(params);
  }

  @Override
  @Cacheable(value = "court", key = "#courtId")
  public Court getCourtById(Integer courtId) {
    Court court = courtDao.getCourtById(courtId);
    if (court == null) {
      log.warn("場地 {} 不存在", courtId);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return court;
  }

  @Override
  public Integer createCourt(CourtRequest courtRequest) {
    return courtDao.createCourt(courtRequest);
  }

  @Override
  @CacheEvict(value = "court", key = "#courtId")
  public void updateCourt(Integer courtId, CourtRequest courtRequest) {
    Court court = courtDao.getCourtById(courtId);
    if (court == null) {
      log.warn("場地 {} 不存在，無法更新", courtId);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    courtDao.updateCourt(courtId, courtRequest);
  }

  @Override
  @CacheEvict(value = "court", key = "#courtId")
  public void deleteCourtById(Integer courtId) {
    courtDao.deleteCourtById(courtId);
  }
}
