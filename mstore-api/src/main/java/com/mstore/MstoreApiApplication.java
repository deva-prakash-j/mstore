package com.mstore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MstoreApiApplication extends SpringBootServletInitializer {

  private static final Logger LOGGER = LogManager.getLogger(MstoreApiApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(MstoreApiApplication.class, args);
    LOGGER.info("Info level log message");
    LOGGER.debug("Debug level log message");
    LOGGER.error("Error level log message");
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(MstoreApiApplication.class);
  }

  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }

}
