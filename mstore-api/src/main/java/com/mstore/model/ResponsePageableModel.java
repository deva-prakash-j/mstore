package com.mstore.model;

import java.util.List;
import org.springframework.data.domain.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponsePageableModel<T> extends ResponseModel<T> {

  private int totalPages;
  private long totalElements;
  private int size;
  private int currentPage;
  private int numberOfElements;
  private boolean first;
  private boolean last;
  private boolean empty;
  @JsonProperty("data")
  @JsonInclude(JsonInclude.Include.ALWAYS)
  private List<T> data;
  @JsonProperty("duration")
  private Long duration;

  public ResponsePageableModel(Page pg) {
    this.data = pg.getContent();
    this.totalPages = pg.getTotalPages();
    this.totalElements = pg.getTotalElements();
    this.size = pg.getSize();
    this.currentPage = pg.getNumber() + 1;
    this.numberOfElements = pg.getNumberOfElements();
    this.first = pg.isFirst();
    this.last = pg.isLast();
    this.empty = pg.isEmpty();
  }
}
