package com.mstore.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import com.mstore.entity.ProductsEntity;
import com.mstore.model.ProductModel;
import com.mstore.model.QueryModel;
import com.mstore.repository.ProductsRepository;

@Service
public class ProductsService {


  @Autowired
  ProductsRepository repo;

  @Autowired
  FilterBuilderService filterBuilderService;

  @Autowired
  MongoTemplate mongoTemplate;

  public List<ProductModel> fetchRecommendedProducts() {
    Pageable topTen = PageRequest.of(0, 8, Direction.ASC, "title");
    List<ProductsEntity> productList = this.repo.findAll(topTen).getContent();
    List<ProductModel> result = new ArrayList<ProductModel>();
    ProductModel productModel;
    for (ProductsEntity prod : productList) {
      productModel = new ProductModel();
      BeanUtils.copyProperties(prod, productModel);
      productModel.setImagesObj(prod.getProductVariants());
      result.add(productModel);
    }
    return result;
  }

  public List<ProductsEntity> fetchHeaderData() {
    return repo.fetchHeaderData();
  }

  public List<ProductsEntity> fetchFilterData(String categorySlug) {
    return repo.fetchFilterData(categorySlug);
  }

  public Page<ProductsEntity> filterProducts(Integer size, String orders, Integer page,
      List<QueryModel> body) {
    Pageable pageable = filterBuilderService.getPageable(size, page, orders);
    Query query = this.filterBuilderService.generateFilterQuery(body);
    query.with(pageable);
    return PageableExecutionUtils.getPage(mongoTemplate.find(query, ProductsEntity.class), pageable,
        () -> mongoTemplate.count(query.skip(0).limit(0), ProductsEntity.class));
  }
}
