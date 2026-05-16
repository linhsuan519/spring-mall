package com.jason.springbootmall.constant;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum SortDirection {
  ASC("asc"),
  DESC("desc");

  private final String sqlKeyword;

  SortDirection(String sqlKeyword) {
    this.sqlKeyword = sqlKeyword;
  }

  public String getSqlKeyword() {
    return sqlKeyword;
  }

  public static Optional<SortDirection> from(String value) {
    if (value == null) {
      return Optional.empty();
    }

    String normalizedValue = value.toLowerCase(Locale.ROOT);

    return Arrays.stream(values())
        .filter(direction -> direction.sqlKeyword.equals(normalizedValue))
        .findFirst();
  }
}
