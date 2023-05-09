package com.mstore.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mstore.model.ProductModel;
import com.mstore.service.ProductsService;

@RestController
public class HomeController {

  @Autowired
  ProductsService productService;

  @GetMapping(path = "recommended")
  public List<ProductModel> fetchRecommendedProducts() {
    return this.productService.fetchRecommendedProducts();
  }
}
