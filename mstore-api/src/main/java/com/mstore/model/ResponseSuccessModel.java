package com.mstore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseSuccessModel<T> extends ResponseModel {

  public static final String DURATION_FIELD = "duration";
  public static final String DATA_FIELD = "data";
  @JsonProperty("data")
  @JsonInclude(JsonInclude.Include.ALWAYS)
  private T data;
  @JsonProperty("duration")
  private Long duration;
}
