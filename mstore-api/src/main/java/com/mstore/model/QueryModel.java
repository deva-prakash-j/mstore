package com.mstore.model;

import com.mstore.enums.FilterOperationEnum;
import lombok.Data;

@Data
public class QueryModel {

  private String field;
  private Object value;
  private FilterOperationEnum operator;
}
