package com.jason.springbootmall.dto;

import com.jason.springbootmall.constant.CourtStatus;
import com.jason.springbootmall.constant.CourtType;

public class CourtQueryParams {

  private CourtType courtType;
  private CourtStatus status;
  private String search;
  private String orderBy;
  private String sort;
  private Integer limit;
  private Integer offset;

  public CourtType getCourtType() {
    return courtType;
  }

  public void setCourtType(CourtType courtType) {
    this.courtType = courtType;
  }

  public CourtStatus getStatus() {
    return status;
  }

  public void setStatus(CourtStatus status) {
    this.status = status;
  }

  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public String getSort() {
    return sort;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }
}
