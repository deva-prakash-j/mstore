package com.mstore.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.mstore.entity.ProductsEntity;

@Repository
public interface ProductsRepository extends MongoRepository<ProductsEntity, String> {

  @Query("{title : ?0}")
  List<ProductsEntity> getProductsByTitle(String title);

  @Query("{brand : ?0}")
  List<ProductsEntity> getProductsByBrand(String brand);

  @Aggregation(pipeline = {"{ '$match': { 'mainImageUopdated' : ?0 } }", "{ '$limit' : 150 }"})
  List<ProductsEntity> getProductsByMainImageUopdated(boolean flag);

  @Query("{productVariants : []}")
  List<ProductsEntity> getProductsWithEmptyProductVariants();

}
