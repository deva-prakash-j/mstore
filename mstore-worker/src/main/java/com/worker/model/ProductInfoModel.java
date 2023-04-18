package com.worker.model;

import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document("products")
public class ProductInfoModel {

  private String asin;
  private PriceModel price;
  private RatingModel rating;
  private String url;
  private boolean sponsored = false;
  private boolean amazonChoice = false;
  private boolean bestSeller = false;
  private boolean prime = false;
  private String title;
  private String thumbnail;
  private ArrayList<AuthorModel> authorsList;
  private ArrayList<HashMap<String, String>> categories;
  private boolean itemAvailable = true;
  private ArrayList<Object> productVariants;
  private String description;
  private ArrayList<String> featureBullets;
  private int maxQuantity;
  private String brand;
  private HashMap<String, String> techSpecs;

}
