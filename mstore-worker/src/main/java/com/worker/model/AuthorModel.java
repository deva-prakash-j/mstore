package com.worker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorModel {

  private String url;
  private String author;
  private String role;

  public void setRole(String role) {
    role = role.replace("\\(|\\)", "");
    this.role = role;
  }
}
