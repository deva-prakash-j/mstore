package com.mstore.entity;

import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.mstore.model.PriceModel;
import com.mstore.model.RatingModel;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Document("products")
public class ProductsEntity {

  @Id
  private String id;
  private String asin;
  private PriceModel price;
  private RatingModel rating;
  private String category;
  private String title;
  private String brand;
  private ArrayList<String> featureBullets;
  private HashMap<String, String> techSpecs;
  private ArrayList<ImagesEntity> productVariants;
  private boolean mainImageUopdated = false;

  public void setAsin(String asin) {
    this.asin = asin;
    this.id = asin;
  }

  public void setProductVariants(ArrayList<ImagesEntity> variants) {
    this.productVariants = variants;
  }

}
