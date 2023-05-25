package com.mstore.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FilterTypeEnum {

  AND("and"), OR("or");

  private final String value;

  FilterTypeEnum(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  public static FilterTypeEnum fromValue(String value) {
    for (FilterTypeEnum op : FilterTypeEnum.values()) {

      // Case insensitive operation name
      if (String.valueOf(op.value).equalsIgnoreCase(value)) {
        return op;
      }
    }
    return null;
  }
}
