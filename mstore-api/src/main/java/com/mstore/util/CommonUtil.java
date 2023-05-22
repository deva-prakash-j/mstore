package com.mstore.util;

import java.util.HashMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mstore.model.ExceptionDetailsBean;

public class CommonUtil {

  public static ResponseEntity<String> exceptionResponseEntity(HttpStatus httpStatus,
      String exceptionMsg, MediaType mediaType) throws Exception {
    ResponseEntity<String> respondeEntity = null;
    ObjectMapper mapper = new ObjectMapper();
    String exceptionDetails =
        mapper.writeValueAsString(new ExceptionDetailsBean(String.valueOf(httpStatus.value()),
            httpStatus.getReasonPhrase(), exceptionMsg));
    if (mediaType != null) {
      HttpHeaders header = new HttpHeaders();
      header.setContentType(mediaType);
      respondeEntity = new ResponseEntity<String>(exceptionDetails, header, httpStatus);
    } else {
      respondeEntity = new ResponseEntity<String>(exceptionDetails, httpStatus);
    }
    return respondeEntity;
  }

  public static ResponseEntity<Object> responseHandler(HttpStatus httpStatus, Object data) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("data", data);
    return new ResponseEntity<Object>(map, httpStatus);
  }
}
