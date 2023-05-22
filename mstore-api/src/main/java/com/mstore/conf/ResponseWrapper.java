package com.mstore.conf;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import com.mstore.model.ResponseModel;
import com.mstore.model.ResponseSuccessModel;

@RestControllerAdvice
public class ResponseWrapper implements ResponseBodyAdvice {

  private static final Logger log;

  @Override
  public boolean supports(MethodParameter methodParameter, Class converterType) {
    final String classPackage = methodParameter.getContainingClass().getPackage().getName();
    return StringUtils.isEmpty((Object) classPackage) || (!classPackage.startsWith("springfox")
        && !classPackage.startsWith("org.springframework.boot.actuate.endpoint")
        && !classPackage.startsWith("org.springdoc"));
  }

  @Override
  public Object beforeBodyWrite(Object responseData, MethodParameter methodParameter,
      MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest,
      ServerHttpResponse serverHttpResponse) {
    final HttpServletRequest servletRequest =
        ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
    final long duration = this.getDuration(serverHttpRequest);
    if (responseData instanceof Error) {
      return (Error) responseData;
    }
    ResponseModel<Object> response = (ResponseModel<Object>) new ResponseSuccessModel();
    Date timestamp = new Date();
    response.setRequestAt(timestamp);
    ((ResponseSuccessModel) response).setData(responseData);
    ((ResponseSuccessModel) response).setDuration(duration);
    return response;
  }

  private long getDuration(final ServerHttpRequest serverHttpRequest) {
    final HttpServletRequest currentRequest = this.getCurrentRequest();
    if (currentRequest.getAttribute("startTime") == null) {
      return 0L;
    }
    final long startTime = (long) currentRequest.getAttribute("startTime");
    final long duration = System.currentTimeMillis() - startTime;
    ResponseWrapper.log.trace(
        "Request URL::" + currentRequest.getRequestURL().toString() + ":: Time Taken=" + duration);
    currentRequest.setAttribute("duration", (Object) duration);
    return duration;
  }

  public static HttpServletRequest getCurrentRequest() {
    return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        .getRequest();
  }

  static {
    log = LoggerFactory.getLogger((Class) ResponseWrapper.class);
  }

}
