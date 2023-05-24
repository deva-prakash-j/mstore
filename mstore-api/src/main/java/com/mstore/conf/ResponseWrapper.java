package com.mstore.conf;

import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import com.mstore.model.ResponseErrorModel;
import com.mstore.model.ResponseModel;
import com.mstore.model.ResponsePageableModel;
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
    if (responseData instanceof Map) {
      final Map<String, Object> obj = (Map<String, Object>) responseData;
      if (!StringUtils.isEmpty(obj.get("status"))) {
        final int status = Integer.parseInt(obj.get("status").toString());
        if (status < 200 || status >= 300) {
          response = (ResponseModel<Object>) new ResponseErrorModel();
          timestamp = ((obj.get("timestamp") != null) ? (Date) obj.get("timestamp") : null);
          ((ResponseErrorModel) response).setCode(status);
          ((ResponseErrorModel) response)
              .setMessage(obj.get("message") != null ? (String) obj.get("message") : "");
          ((ResponseErrorModel) response)
              .setError(obj.get("error") != null ? (String) obj.get("error") : "");
          ((ResponseErrorModel) response)
              .setPath(obj.get("path") != null ? (String) obj.get("path") : "");
        }
      }
    } else if (responseData instanceof Page) {
      response = (ResponseModel<Object>) new ResponsePageableModel(((Page) responseData));
      ((ResponsePageableModel) response).setDuration(duration);
    }
    response.setRequestAt(timestamp);
    if (response instanceof ResponseSuccessModel) {
      ((ResponseSuccessModel) response).setData(responseData);
      ((ResponseSuccessModel) response).setDuration(duration);
    }
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
