package com.mstore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PriceModel {

  private boolean discounted = false;
  private String currency;
  private String currentPrice = "0";
  private String beforePrice = "0";
  private String savingsAmount = "0";
  private String savingsPercent = "0";

  public String priceFormat(String price, String symbol) {
    return price.replace(symbol, "").replaceAll(",", "");
  }

  public void getSavings() {
    Double currentPrice = Double.parseDouble(this.currentPrice);
    Double beforePrice = Double.parseDouble(this.beforePrice);
    Double savings;
    if (this.beforePrice != "0") {
      savings = beforePrice - currentPrice;
      this.savingsAmount = String.format("%.2f", savings);
      this.savingsPercent = String.format("%.2f", (100 / beforePrice) * savings);
    }
  }

}
