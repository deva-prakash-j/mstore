package com.mstore.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import com.mstore.entity.ProductsEntity;
import com.mstore.model.ProductModel;
import com.mstore.repository.ProductsRepository;

@Service
public class ProductsService {


  @Autowired
  ProductsRepository repo;

  public List<ProductModel> fetchRecommendedProducts() {
    Pageable topTen = PageRequest.of(0, 8, Direction.ASC, "title");
    List<ProductsEntity> productList = this.repo.findAll(topTen).getContent();
    List<ProductModel> result = new ArrayList<ProductModel>();
    ProductModel productModel;
    for (ProductsEntity prod : productList) {
      productModel = new ProductModel();
      BeanUtils.copyProperties(prod, productModel);
      productModel.setImages(prod.getProductVariants());
      result.add(productModel);
    }
    return result;
  }
}
