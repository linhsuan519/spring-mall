package com.jason.springbootmall.constant;

import java.util.Arrays;
import java.util.Optional;

public enum ProductSortField {
  CREATED_DATE("createdDate", "created_date"),
  PRICE("price", "price"),
  PRODUCT_NAME("productName", "product_name"),
  STOCK("stock", "stock");

  private final String apiValue;
  private final String columnName;

  ProductSortField(String apiValue, String columnName) {
    this.apiValue = apiValue;
    this.columnName = columnName;
  }

  public String toColumnName() {
    return columnName;
  }

  public static Optional<ProductSortField> from(String value) {
    if (value == null) {
      return Optional.empty();
    }

    return Arrays.stream(values()).filter(field -> field.apiValue.equals(value)).findFirst();
  }
}
