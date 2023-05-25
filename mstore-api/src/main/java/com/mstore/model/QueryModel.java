package com.mstore.model;

import com.mstore.enums.FilterOperationEnum;
import com.mstore.enums.FilterTypeEnum;
import lombok.Data;

@Data
public class QueryModel {

  private String field;
  private Object value;
  private FilterOperationEnum operator;
  private FilterTypeEnum filterType;
}
