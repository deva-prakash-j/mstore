package com.mstore.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mstore.entity.ModelsEntity;
import com.mstore.entity.ProductsEntity;
import com.mstore.model.ProductModel;
import com.mstore.model.QueryModel;
import com.mstore.service.FilterBuilderService;
import com.mstore.service.ModelsService;
import com.mstore.service.ProductsService;

@RestController
public class HomeController {

  @Autowired
  ProductsService productService;

  @Autowired
  ModelsService modelsService;

  @Autowired
  FilterBuilderService filterBuilderService;

  @GetMapping(path = "recommended")
  public List<ProductModel> fetchRecommendedProducts() {
    return this.productService.fetchRecommendedProducts();
  }

  @GetMapping(path = "get-model-data")
  public List<ModelsEntity> fetchModelsData(@RequestParam String name) {
    return this.modelsService.fetchModel(name);
  }

  @GetMapping(path = "get-header-data")
  public List<ProductsEntity> fetchHeaderData() {
    return this.productService.fetchHeaderData();
  }

  @PostMapping(
      value = {"get-products-list", "get-products-list/{page}", "get-products-list/{page}/{size}"})
  public Object fetchProductsList(@PathVariable(value = "page", required = false) Integer page,
      @PathVariable(value = "size", required = false) Integer size,
      @RequestParam(value = "orders", required = false) String orders,
      @RequestBody List<QueryModel> body) {
    page = page != null ? page - 1 : 0;
    size = size != null ? size : 12;
    System.out.println(body);
    return productService.filterProducts(size, orders, page, body);
  }

  @GetMapping(path = "get-products-list/{categorySlug}")
  public Object fetchProductsList(
      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "12") Integer size,
      @RequestParam(value = "filter", required = false) String filter,
      @RequestParam(value = "orders", required = false) String orders,
      @PathVariable String categorySlug) {
    filter = "categorySlug$eq$" + categorySlug + "##" + filter;
    return productService.filterProducts(size, orders, page,
        this.filterBuilderService.generateFilterModel(filter));
  }

  @GetMapping(path = "get-filter-data/{categorySlug}")
  public Object fetchFilterData(@PathVariable String categorySlug) {
    return productService.fetchFilterData(categorySlug);
  }


}
