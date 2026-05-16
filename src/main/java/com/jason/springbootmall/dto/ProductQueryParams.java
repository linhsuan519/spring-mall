package com.jason.springbootmall.dto;

import com.jason.springbootmall.constant.ProductCategory;
import com.jason.springbootmall.constant.ProductSortField;
import com.jason.springbootmall.constant.SortDirection;

public class ProductQueryParams {

  private ProductCategory category;
  private String search;
  private ProductSortField orderBy;
  private SortDirection sort;
  private Integer limit;
  private Integer offset;

  public ProductCategory getCategory() {
    return category;
  }

  public void setCategory(ProductCategory category) {
    this.category = category;
  }

  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public ProductSortField getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(ProductSortField orderBy) {
    this.orderBy = orderBy;
  }

  public SortDirection getSort() {
    return sort;
  }

  public void setSort(SortDirection sort) {
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
