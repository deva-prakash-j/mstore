package com.worker.util;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worker.model.Countries;

@Service
public class CommonUtil {

  public <T> List<T> jsonToJava(String fileName, Class<T> classType) throws IOException {
    List<T> list = null;
    Resource resource = new ClassPathResource(fileName);
    File file = resource.getFile();
    ObjectMapper objectMapper = new ObjectMapper();
    list = objectMapper.readValue(file, new TypeReference<List<T>>() {});
    return list;
  }

  public List<Countries> getCountries(String fileName) throws IOException {
    List<Countries> list = null;
    Resource resource = new ClassPathResource(fileName);
    File file = resource.getFile();
    ObjectMapper objectMapper = new ObjectMapper();
    list = objectMapper.readValue(file, new TypeReference<List<Countries>>() {});
    return list;
  }

  public ResponseEntity<Object> sendResponse(List list) {
    return new ResponseEntity<Object>(list, HttpStatus.ACCEPTED);
  }

  public String getUserAgent() {
    String[] os = new String[] {"Macintosh; Intel Mac OS X 10_15_7",
        "Macintosh; Intel Mac OS X 10_15_5", "Macintosh; Intel Mac OS X 10_11_6",
        "Macintosh; Intel Mac OS X 10_6_6", "Macintosh; Intel Mac OS X 10_9_5",
        "Macintosh; Intel Mac OS X 10_10_5", "Macintosh; Intel Mac OS X 10_7_5",
        "Macintosh; Intel Mac OS X 10_11_3", "Macintosh; Intel Mac OS X 10_10_3",
        "Macintosh; Intel Mac OS X 10_6_8", "Macintosh; Intel Mac OS X 10_10_2",
        "Macintosh; Intel Mac OS X 10_10_3", "Macintosh; Intel Mac OS X 10_11_5",
        "Windows NT 10.0; Win64; x64", "Windows NT 10.0; WOW64", "Windows NT 10.0"};
    return "Mozilla/5.0 (" + os[(int) Math.floor(Math.random() * os.length)]
        + ") AppleWebKit/537.36 (KHTML, like Gecko) Chrome/"
        + ((int) Math.floor(Math.random() * 4) + 100) + ".0."
        + ((int) Math.floor(Math.random() * 190) + 4100) + "."
        + ((int) Math.floor(Math.random() * 50) + 140) + " Safari/537.36";
  }

  public void displayReq(HttpServletRequest request, Object body) {
    StringBuilder reqMessage = new StringBuilder();
    Map<String, String> parameters = getParameters(request);

    reqMessage.append("REQUEST ");
    reqMessage.append("method = [").append(request.getMethod()).append("]");
    reqMessage.append(" path = [").append(request.getRequestURI()).append("] ");

    if (!parameters.isEmpty()) {
      reqMessage.append(" parameters = [").append(parameters).append("] ");
    }

    if (!Objects.isNull(body)) {
      reqMessage.append(" body = [").append(body).append("]");
    }
  }

  private Map<String, String> getParameters(HttpServletRequest request) {
    Map<String, String> parameters = new HashMap<>();
    Enumeration<String> params = request.getParameterNames();
    while (params.hasMoreElements()) {
      String paramName = params.nextElement();
      String paramValue = request.getParameter(paramName);
      parameters.put(paramName, paramValue);
    }
    return parameters;
  }

}
