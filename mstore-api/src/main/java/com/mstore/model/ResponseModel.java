package com.mstore.model;

import java.util.Date;
import org.slf4j.MDC;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ResponseModel<T> {

  private static final String TRANSACTION_ID_FIELD = "transactionId";
  private static final String TIMESTAMP_FIELD = "timestamp";
  @JsonProperty("transactionId")
  private String transactionID;
  @JsonProperty("timestamp")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd'T'HH:mm:ss.SSS'Z'")
  private Date requestAt;

  ResponseModel() {
    this.transactionID = MDC.get(TRANSACTION_ID_FIELD);
  }

}
