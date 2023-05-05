package com.mstore.model;

import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.data.annotation.Id;
import com.mstore.entity.ImagesEntity;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductModel {

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
  private ImagesEntity imagesObj;

  public void setAsin(String asin) {
    this.asin = asin;
    this.id = asin;
  }

  public void setImagesObj(ArrayList<ImagesEntity> variants) {
    for (ImagesEntity img : variants) {
      if (img.getAsin().equalsIgnoreCase(this.asin)) {
        this.imagesObj = img;
        return;
      }
    }
  }
}
