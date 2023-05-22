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

  @Aggregation(pipeline = {"{ $facet: {\r\n"
      + "      categories: [{ $group: { _id: \"categories\", categories:{$addToSet:\"$category\"} } }, { \"$project\": {_id: 0}}],\r\n"
      + "      brands:     [{ $group: { _id: \"brands\",    brands:{$addToSet:\"$brand\"} } }, { \"$project\": {_id: 0}}],\r\n"
      + "      brandsWithCategory:[{ $group: { _id: {category: \"$category\", brand:\"$brand\"}, \"count\": { \"$sum\": 1 }  } }, { $group: { _id: \"$_id.category\", brands:{$addToSet:\"$_id.brand\"} } }]\r\n"
      + "  }}"})
  public List<ProductsEntity> fetchHeaderData();

}
