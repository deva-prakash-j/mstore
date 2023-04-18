package com.worker.model;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Data;

@Data
public class ProductInformation {

  private ArrayList<String> id;
  private HashMap<String, FieldInfo> fields;

}


@Data
class FieldInfo {
  private String key;
  boolean rank;
}
