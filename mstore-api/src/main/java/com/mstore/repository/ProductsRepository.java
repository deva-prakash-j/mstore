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

  @Query("{productVariants : []}")
  List<ProductsEntity> getProductsWithEmptyProductVariants();

  @Aggregation(pipeline = {"{ $facet: {\r\n"
      + "      categories: [{ $group: { _id: \"$categorySlug\", categories:{$addToSet:\"$category\"} }},{ \"$project\": {_id: 0, \"urlSlug\": \"$_id\", \"displayText\": { $first: \"$categories\" }}}],\r\n"
      + "      brands:     [{ $group: { _id: \"$brandSlug\", brands:{$addToSet:\"$brand\"} } }, { \"$project\": {_id: 0, \"urlSlug\": \"$_id\", \"displayText\": { $first: \"$brands\" }}}],\r\n"
      + "      brandsWithCategory:[\r\n"
      + "      { $group: { _id: {categorySlug: \"$categorySlug\", category: \"$category\", brand: {urlSlug: \"$brandSlug\", displayText:\"$brand\"}}, \"count\": { \"$sum\": 1 }}},\r\n"
      + "      { $group: { _id: \"$_id.category\", categorySlugs:{$addToSet:\"$_id.categorySlug\"},brands:{$addToSet:\"$_id.brand\"}}},\r\n"
      + "      { \"$project\": {_id: 0, \"urlSlug\":{ $first: \"$categorySlugs\" }, \"displayText\": \"$_id\", brands: \"$brands\"}}\r\n"
      + "      ]\r\n" + "  }}"})
  public List<ProductsEntity> fetchHeaderData();

}
