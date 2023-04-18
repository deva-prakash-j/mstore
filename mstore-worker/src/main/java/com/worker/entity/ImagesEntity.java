package com.worker.entity;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.worker.model.ImageModel;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Document("images")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImagesEntity {

  @Id
  private String id;
  private String asin;
  @JsonIgnore
  private String url;
  @JsonIgnore
  private String sfUrl;
  private String title;
  private Object isCurrentProduct;
  private List<ImageModel> images;

  public void setAsin(String asin) {
    this.asin = asin;
    this.id = asin;
  }

}
