package com.mstore.util;

import java.util.NoSuchElementException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler {


  private ResponseEntity<String> buildResponseEntity(final HttpStatus httpStatus,
      final Exception ex, final String errorMsg) {
    ex.printStackTrace();
    try {
      return CommonUtil.exceptionResponseEntity(httpStatus, ex.getMessage(),
          MediaType.APPLICATION_JSON_UTF8);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @ExceptionHandler(value = {HttpMessageNotReadableException.class})
  protected ResponseEntity<String> handle400(final HttpMessageNotReadableException ex) {
    return buildResponseEntity(HttpStatus.BAD_REQUEST, ex, Constants.API_ERR_400);
  }

  @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<String> handle405(final HttpRequestMethodNotSupportedException ex) {
    return buildResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, ex, Constants.API_ERR_405);
  }

  @ExceptionHandler(value = DataAccessException.class)
  public ResponseEntity<String> handleDBException(final DataAccessException ex) {
    return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex, Constants.API_ERR_DB);
  }

  @ExceptionHandler(value = NoHandlerFoundException.class)
  protected ResponseEntity<String> handleNoHandlerFoundException(final NoHandlerFoundException ex) {
    return buildResponseEntity(HttpStatus.NOT_FOUND, ex, Constants.API_ERR_404);
  }

  @ExceptionHandler(value = NoSuchElementException.class)
  protected ResponseEntity<String> handleNoSuchElementException(final NoSuchElementException ex) {
    return buildResponseEntity(HttpStatus.NOT_FOUND, ex, Constants.API_ERR_404);
  }

  @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
  protected ResponseEntity<String> handleHttpMediaTypeNotSupported(
      final HttpMediaTypeNotSupportedException ex) {
    return buildResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex, Constants.API_ERR_415);
  }

  @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
  protected ResponseEntity<String> handleHttpMediaTypeNotAcceptable(
      final HttpMediaTypeNotAcceptableException ex) {
    return buildResponseEntity(HttpStatus.NOT_ACCEPTABLE, ex, Constants.API_ERR_406);
  }

  @ExceptionHandler(value = RuntimeException.class)
  public ResponseEntity<String> handleOtherException(final RuntimeException ex) {
    return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex, Constants.API_ERR_4XX_5XX);
  }
}
