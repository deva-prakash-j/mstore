package com.worker.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worker.entity.ImagesEntity;
import com.worker.entity.ProductsEntity;
import com.worker.model.Countries;
import com.worker.model.ImageModel;
import com.worker.model.ProductInfoModel;
import com.worker.service.ContentfulService;
import com.worker.service.ImageService;
import com.worker.service.ProductsService;
import com.worker.util.AmazonScraper;
import com.worker.util.CommonUtil;
import io.swagger.annotations.Api;

@RestController
@Api(tags = "Mstore worker API")
@RequestMapping("/mstore")
public class WorkerController {

  @Autowired
  CommonUtil util;

  @Autowired
  ProductsService productService;

  @Autowired
  ContentfulService cfService;

  @Autowired
  ImageService imageService;

  List<Countries> countriesList;

  @GetMapping("/products")
  public ResponseEntity<Object> getProducts(
      @RequestParam(value = "countryCode", defaultValue = "IN") String countryCode,
      @RequestParam String keyword, @RequestParam(defaultValue = "20") int limit) {
    getCountryList();
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("countryCode", countryCode);
    map.put("keyword", keyword);
    map.put("limit", Integer.toString(limit));
    map.put("type", "products");
    AmazonScraper scraper = new AmazonScraper(countriesList, map, cfService);
    ArrayList<ProductInfoModel> prodList = scraper.getProducts(limit);
    this.productService.storeASNID(prodList);
    HashMap<String, Object> outMap = new HashMap<String, Object>();
    outMap.put("total", prodList.size());
    outMap.put("productsList", prodList);
    return new ResponseEntity<Object>(outMap, HttpStatus.OK);
  }

  @GetMapping("/product-details")
  public ResponseEntity<Object> getProductDetails(
      @RequestParam(value = "countryCode", defaultValue = "IN") String countryCode,
      @RequestParam String asin) {
    getCountryList();
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("countryCode", countryCode);
    map.put("type", "asin");
    map.put("asin", asin);
    // map.put("storeToCF", "true");
    AmazonScraper scraper = new AmazonScraper(countriesList, map, cfService);
    HashMap<String, Object> outMap = new HashMap<String, Object>();
    ProductInfoModel product = scraper.getProductDetails();
    this.productService.storeProductDetails(product);
    outMap.put("total", 1);
    outMap.put("productDetail", product);
    return new ResponseEntity<Object>(outMap, HttpStatus.OK);
  }

  @GetMapping("/load-product-details")
  public ResponseEntity<Object> getProductDetails() {
    List<ProductsEntity> products = this.productService.getAll();
    List<ProductsEntity> productsUpdated = new ArrayList<ProductsEntity>();
    getCountryList();
    HashMap<String, String> map = new HashMap<String, String>();
    AmazonScraper scraper;
    ProductInfoModel productModel;
    map.put("countryCode", "IN");
    map.put("type", "asin");
    ArrayList<HashMap<String, String>> categories;
    int counter = 0;
    System.out.println("total --> " + products.size());
    for (ProductsEntity product : products) {
      if (counter == 200) {
        break;
      }
      System.out.println("count --> " + counter++);
      map.put("asin", product.getAsin());
      try {
        scraper = new AmazonScraper(countriesList, map, cfService);
        HashMap<String, Object> outMap = new HashMap<String, Object>();
        productModel = scraper.getProductDetails();
        product.setPrice(productModel.getPrice());
        product.setRating(productModel.getRating());
        product.setTitle(productModel.getTitle());
        product.setBrand(productModel.getBrand());
        product.setFeatureBullets(productModel.getFeatureBullets());
        product.setTechSpecs(productModel.getTechSpecs());
        product.setProductVariants(productModel.getProductVariants());
        categories = productModel.getCategories();
        if (categories != null && categories.size() > 0) {
          product.setCategory(categories.get(categories.size() - 1).get("category"));
        }
        productsUpdated.add(product);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    this.productService.storeAll(productsUpdated);
    return new ResponseEntity<Object>(productsUpdated, HttpStatus.OK);
  }

  @GetMapping("/load-product-brands")
  public ResponseEntity<Object> getBrandDetails() {
    List<ProductsEntity> products = this.productService.getByBrand(null);
    List<ProductsEntity> productsUpdated = new ArrayList<ProductsEntity>();
    getCountryList();
    HashMap<String, String> map = new HashMap<String, String>();
    AmazonScraper scraper;
    ProductInfoModel productModel;
    map.put("countryCode", "IN");
    map.put("type", "asin");
    int counter = 0;
    System.out.println("total --> " + products.size());
    for (ProductsEntity product : products) {
      // if (counter == 1) {
      // break;
      // }
      System.out.println("count --> " + counter++);
      map.put("asin", product.getAsin());
      try {
        scraper = new AmazonScraper(countriesList, map, cfService);
        product.setBrand(scraper.getBrand());
        productsUpdated.add(product);
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
    this.productService.storeAll(productsUpdated);
    return new ResponseEntity<Object>(productsUpdated, HttpStatus.OK);
  }

  @GetMapping("/load-images")
  public ResponseEntity<Object> getImageDetails() {
    List<ProductsEntity> products = this.productService.getProductsByImageUpdate(false);
    List<ProductsEntity> productsUpdated = new ArrayList<ProductsEntity>();
    ArrayList<Object> productVarients;
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> map;
    ArrayList<Object> variant;
    List<Object> images;
    List<Object> imagesObj;
    Map<String, Object> img;
    String asin;
    ImagesEntity imageEntity;
    String url;
    int counter = 0;
    System.out.println("Count -> " + products.size());
    for (ProductsEntity product : products) {
      System.out.println("counter -> " + ++counter);
      System.out.println("asin -> " + product.getAsin());
      productVarients = product.getProductVariants();
      variant = new ArrayList<Object>();
      for (Object obj : productVarients) {
        map = mapper.convertValue(obj, Map.class);
        asin = (String) map.get("asin");
        Optional<ImagesEntity> image = this.imageService.fintById(asin);
        if (image != null && image.isPresent()) {
          variant.add(image.get());
        } else {
          images = mapper.convertValue(map.get("images"), List.class);
          System.out.println(map);
          imagesObj = new ArrayList<Object>();
          for (Object imageObj : images) {
            img = mapper.convertValue(imageObj, Map.class);
            url = (String) img.get("url");
            imageEntity = this.imageService.findByURL(url);
            if (imageEntity != null) {
              img.put("url", imageEntity.getSfUrl());
            } else {
              imageEntity = new ImagesEntity();
              imageEntity.setUrl(url);
              img.put("url", this.cfService.uploadImage(url, asin));
              imageEntity.setSfUrl((String) img.get("url"));
              this.imageService.saveImage(imageEntity);
            }
            imagesObj.add(img);
          }
          imageEntity = new ImagesEntity();
          imageEntity.setAsin(asin);
          // imageEntity.setImages(imagesObj);
          imageEntity.setTitle((String) map.get("title"));
          imageEntity.setIsCurrentProduct(map.get("isCurrentProduct"));
          this.imageService.saveImage(imageEntity);
          map.put("images", imagesObj);
          variant.add(map);
        }
      }
      product.setProductVariants(variant);
      product.setMainImageUopdated(true);
      this.productService.saveProduct(product);
    }
    return new ResponseEntity<Object>(products, HttpStatus.OK);
  }

  @GetMapping("/reset-product-variants")
  public void resetProductVariants() {
    List<ProductsEntity> products = this.productService.fetchAll();
    ArrayList<Object> productVarients;
    ArrayList<Object> productVarientsUpdated;
    List<ProductsEntity> productsUpdated = new ArrayList<ProductsEntity>();
    String asin;
    ImagesEntity imgEntity;
    ObjectMapper mapper = new ObjectMapper();
    List<ImageModel> images;
    String url;
    ImagesEntity imageEntity;
    boolean update;
    for (ProductsEntity product : products) {
      productVarients = product.getProductVariants();
      productVarientsUpdated = new ArrayList<Object>();
      asin = product.getAsin();
      update = false;
      for (Object obj : productVarients) {
        imgEntity = mapper.convertValue(obj, ImagesEntity.class);
        images = new ArrayList<ImageModel>();
        for (ImageModel img : imgEntity.getImages()) {
          if (img.getUrl().contains("https://m.media-amazon.com/images")) {
            update = true;
            imageEntity = this.imageService.findByURL(img.getUrl());
            if (imageEntity != null) {
              img.setUrl(imageEntity.getSfUrl());
            } else {
              imageEntity = new ImagesEntity();
              imageEntity.setUrl(img.getUrl());
              img.setUrl(this.cfService.uploadImage(img.getUrl(), imgEntity.getAsin()));
              imageEntity.setSfUrl(img.getUrl());
              this.imageService.saveImage(imageEntity);
            }
          }
          images.add(img);
        }
        imgEntity.setImages(images);
        productVarientsUpdated.add(imgEntity);
      }
      product.setProductVariants(productVarientsUpdated);
      if (update) {
        productsUpdated.add(product);
      }
    }
    this.productService.storeAll(productsUpdated);
    System.out.println(products.size());
  }


  public List<Countries> getCountryList() {
    if (countriesList == null) {
      try {
        countriesList = util.getCountries("/static/countries.json");
      } catch (IOException e) {
        System.out.print(e);
      }
    }
    return countriesList;
  }

}
