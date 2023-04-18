package com.worker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Countries {

  private String country;
  private String code;
  private String currency;
  private String symbol;
  private String host;

  @JsonIgnore
  private ProductInformation productInformation;

}
