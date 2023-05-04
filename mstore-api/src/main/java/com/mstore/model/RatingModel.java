package com.mstore.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RatingModel {

  private String totalReview = "0";
  private String reviewRating = "0";
  @JsonInclude(JsonInclude.Include.NON_DEFAULT)
  private int limit;
  private List reviewList;

}
