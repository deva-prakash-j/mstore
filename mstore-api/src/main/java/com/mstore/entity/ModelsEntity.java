package com.mstore.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Document("models")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelsEntity {

  @Id
  private String id;
  private String name;
  private Object banner;

}
