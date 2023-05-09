package com.mstore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentfulController {


  @GetMapping(path = "get-contentful-data")
  public void getContentfulData(@RequestParam String modelType) {

  }
}
