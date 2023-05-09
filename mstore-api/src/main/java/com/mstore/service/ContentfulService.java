package com.mstore.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.contentful.java.cda.CDAClient;

@Service
public class ContentfulService {

  private final CDAClient cdaClient;

  private String url;
  private String accessToken;

  @Autowired
  RestTemplate restTemplate;

  public ContentfulService(@Value("${contentful.delivery.api.token}") final String cfDeliveryToken,
      @Value("${contentful.space.id}") final String cfSpaceId,
      @Value("${contentful.environment.id}") final String cfEnvironmentId) {
    this.url = "https://cdn.contentful.com/spaces/" + cfSpaceId + "/environments/" + cfEnvironmentId
        + "/entries";
    this.accessToken = cfDeliveryToken;
    this.cdaClient = CDAClient.builder().setSpace(cfSpaceId).setEnvironment(cfEnvironmentId)
        .setToken(cfDeliveryToken).build();
  }

  public HashMap fetchContentModel(String contentType) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      HttpEntity<String> entity = new HttpEntity<String>(headers);
      String urlTemplate =
          UriComponentsBuilder.fromHttpUrl(this.url).queryParam("access_token", this.accessToken)
              .queryParam("content_type", contentType).encode().toUriString();
      return this.restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, HashMap.class)
          .getBody();
    } catch (Exception e) {
      throw new NoSuchElementException();
    }
  }

}
