package com.worker.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.worker.entity.ProductsEntity;
import com.worker.model.ProductInfoModel;
import com.worker.repository.ProductsRepository;

@Service
public class ProductsService {

  @Autowired
  ProductsRepository productsRepository;

  @Autowired
  ContentfulService contentfulService;

  public void storeProductDetails(ProductInfoModel product) {
    ProductsEntity productEntity = new ProductsEntity();
    productEntity.setAsin(product.getAsin());
    productEntity.setPrice(product.getPrice());
    productEntity.setRating(product.getRating());
    productEntity.setTitle(product.getTitle());
    productEntity.setBrand(product.getBrand());
    productEntity.setFeatureBullets(product.getFeatureBullets());
    productEntity.setTechSpecs(product.getTechSpecs());
    ArrayList<Object> variants = product.getProductVariants();
    // productsRepository.save(productEntity);
  }

  public void storeASNID(ArrayList<ProductInfoModel> prodList) {
    ArrayList<ProductsEntity> productsList = new ArrayList<ProductsEntity>();
    ProductsEntity product;
    for (ProductInfoModel productModel : prodList) {
      product = new ProductsEntity();
      product.setAsin(productModel.getAsin());
      productsList.add(product);
    }
    productsRepository.saveAll(productsList);
  }

  public void storeAll(List<ProductsEntity> prodList) {
    this.productsRepository.saveAll(prodList);
  }

  public List<ProductsEntity> getAll() {
    return productsRepository.getProductsByTitle(null);
  }

  public List<ProductsEntity> getProductsByImageUpdate(boolean flag) {
    return productsRepository.getProductsByMainImageUopdated(flag);
  }

  public List<ProductsEntity> getByBrand(String brand) {
    return productsRepository.getProductsByBrand(brand);
  }

  public void saveProduct(ProductsEntity entity) {
    productsRepository.save(entity);
  }

  public List<ProductsEntity> getProductsWithEmptyProductVariants() {
    return productsRepository.getProductsWithEmptyProductVariants();
  }

  public List<ProductsEntity> fetchAll() {
    return productsRepository.findAll();
  }
}
