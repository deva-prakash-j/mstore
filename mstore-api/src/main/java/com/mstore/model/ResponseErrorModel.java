package com.mstore.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ResponseErrorModel<T> extends ResponseModel {

  @JsonProperty("code")
  private int code;

  @JsonProperty("message")
  private String message;

  @JsonProperty("error")
  private String error;

  @JsonProperty("path")
  private String path;
}
