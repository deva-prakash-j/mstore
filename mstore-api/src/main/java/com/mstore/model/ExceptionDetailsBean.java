package com.mstore.model;

/**
 * This is the Bean class which Maps the Exception details as response
 * 
 * @author Deva Prakash J
 * @version 1.0
 * @since 25-Nov-2020
 *
 * Change Log
 * ***************************************************************************
 * Date              Name          Ver.     Changes desc 
 * ***************************************************************************
 * 25-Nov-2020   Deva Prakash J    1.0     Initial draft 
 */

public class ExceptionDetailsBean {

  private String status;
  private String message;
  private String exception;

  public ExceptionDetailsBean() {
  }

  public ExceptionDetailsBean(String status, String message, String exception) {
    super();
    this.status = status;
    this.message = message;
    this.exception = exception;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n ExceptionJSON.status = ");
    sb.append(status);
    sb.append("\n ExceptionJSON.message = ");
    sb.append(message);
    sb.append("\n ExceptionJSON.exception = ");
    sb.append(exception);
    //
    return sb.toString();
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getException() {
    return exception;
  }

  public void setException(String exception) {
    this.exception = exception;
  }
}
