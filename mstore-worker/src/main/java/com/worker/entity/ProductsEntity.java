package com.worker.entity;

import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.worker.model.PriceModel;
import com.worker.model.RatingModel;
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
  private ArrayList<Object> productVariants;
  private boolean mainImageUopdated = false;

  public void setAsin(String asin) {
    this.asin = asin;
    this.id = asin;
  }

  public void setProductVariants(ArrayList<Object> variants) {
    this.productVariants = variants;
  }

}
