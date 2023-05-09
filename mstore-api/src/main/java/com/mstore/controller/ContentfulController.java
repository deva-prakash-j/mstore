package com.mstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mstore.service.ContentfulService;

@RestController
public class ContentfulController {

  @Autowired
  ContentfulService contentfulService;

  @GetMapping(path = "get-contentful-data")
  public ResponseEntity<Object> getContentfulData(@RequestParam String contentType) {
    return new ResponseEntity<Object>(this.contentfulService.fetchContentModel(contentType),
        HttpStatus.OK);
  }
}
