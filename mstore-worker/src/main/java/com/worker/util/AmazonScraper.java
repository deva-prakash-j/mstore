package com.worker.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worker.model.AuthorModel;
import com.worker.model.Countries;
import com.worker.model.PriceModel;
import com.worker.model.ProductInfoModel;
import com.worker.model.RatingModel;
import com.worker.service.ContentfulService;

public class AmazonScraper {

  private CommonUtil util;

  private String accept =
      "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9";
  private String baseUrl = "https://";
  private String keyword;
  private String type;
  private String asin;
  private Countries country;
  private ContentfulService cfService;
  private boolean storeToCF;

  public AmazonScraper(List<Countries> countriesList, HashMap<String, String> map,
      ContentfulService cfService) {
    String countryCode = map.containsKey("countryCode") ? map.get("countryCode") : "";
    keyword = map.get("keyword");
    type = map.containsKey("type") ? map.get("type") : "";
    asin = map.get("asin");
    storeToCF = map.get("storeToCF") != null ? true : false;
    Countries country =
        countriesList.stream().filter(c -> countryCode.equals(c.getCode())).findFirst().get();
    this.country = country;
    baseUrl += country.getHost();
    util = new CommonUtil();
    this.cfService = cfService;
  }

  public String getUrl() {
    switch (this.type) {
      case "products":
        return "/s";
      case "reviews":
        return "/product-reviews/" + this.asin
            + "/ref=cm_cr_arp_d_viewopt_srt?reviewerType=avp_only_reviews";
      case "asin":
        return "/dp/" + this.asin + "/ref=sspa_dk_detail_3&th=1&psc=1?th=1&psc=1";
      default:
        return "";
    }
  }

  public Element createConnection(HashMap<String, String> qs) {
    Element body = null;
    try {
      Connection connection = Jsoup.connect(baseUrl + getUrl());
      if (qs != null) {
        connection.data(qs);
      }
      connection.userAgent(util.getUserAgent());
      connection.header("accept", accept);
      System.out.println(baseUrl + getUrl());
      Document docCustomConn = connection.get();
      body = docCustomConn.body();
    } catch (Exception e) {
      System.out.println("createConnection - > " + e.getMessage());
    }

    return body;
  }

  public HashMap<String, String> getCategories() {
    Element body = createConnection(null);
    HashMap<String, String> categories = new HashMap<String, String>();
    String key, value;
    body = body.getElementById("searchDropdownBox");
    if (body != null) {
      Elements options = body.children();
      for (Element node : options) {
        key = node.attr("value");
        if (key != null) {
          key = key.replaceAll("search-alias=", "");
        }
        value = node.text();
        categories.put(key, value);
      }
    }
    return categories;
  }

  public ArrayList<ProductInfoModel> getProducts(int limit) {
    int page = 1;
    HashMap<String, String> qs = new HashMap<String, String>();
    HashSet<String> set = new HashSet<String>();
    String asin;
    ProductInfoModel product;
    ArrayList<ProductInfoModel> list = new ArrayList<ProductInfoModel>();
    qs.put("k", keyword);
    while (list.size() < limit) {
      qs.put("page", String.valueOf(page));
      Element body = createConnection(qs);
      if (body != null) {
        Elements results = body.select("div[data-component-type=\"s-search-result\"]");
        if (results.size() <= 1) {
          return list;
        }
        for (Element prod : results) {
          asin = prod.attr("data-asin");
          if (!set.contains(asin)) {
            set.add(asin);
            product = getProductResult(prod);
            if (product != null) {
              list.add(product);
            }
            if (list.size() == limit) {
              return list;
            }
          }
        }
      }
      page++;
    }
    return list;
  }

  public ProductInfoModel getProductResult(Element prod) {
    ProductInfoModel productInfo = new ProductInfoModel();
    String asin = prod.attr("data-asin");
    String title;
    if (asin != null && asin != "") {
      productInfo.setAsin(asin);
      productInfo.setUrl(this.baseUrl + "/dp/" + asin);
      productInfo.setPrice(getPrice(prod));
      productInfo.setRating(getRatings(prod));
      if (prod.getElementById(asin + "-amazons-choice") != null) {
        productInfo.setAmazonChoice(true);
      }
      if (prod.getElementById(asin + "-best-seller") != null) {
        productInfo.setBestSeller(true);
      }
      if (prod.getElementsByClass("s-prime") != null) {
        productInfo.setPrime(true);
      }
      prod = prod.selectFirst("[data-image-source-density=\"1\"]");
      if (prod != null) {
        productInfo.setThumbnail(prod.attr("src"));
        title = prod.attr("alt");
        if (title.contains("Sponsored Ad - ")) {
          title = title.replace("Sponsored Ad - ", "");
          productInfo.setSponsored(true);
        }
        productInfo.setTitle(title);
        if (title != null && title.contains("(Renewed)")) {
          return null;
        }
      }
    }
    return productInfo;
  }

  public RatingModel getRatings(Element prod) {
    RatingModel rating = new RatingModel();
    Element elem = prod.getElementsByClass("a-icon-star-small").first();
    if (elem != null) {
      rating.setReviewRating(elem.children().first().text().split(" ")[0]);
      rating.setTotalReview(elem.parent().parent().parent().nextElementSibling().attr("aria-label")
          .replaceAll(",", ""));
    }
    return rating;
  }

  public PriceModel getPrice(Element prod) {
    PriceModel price = new PriceModel();
    Element priceElem = null;
    String query = "span[data-a-size=\"???\"]";
    if (prod.select(query.replace("???", "xl")).size() > 0) {
      priceElem = prod.select(query.replace("???", "xl")).first();
    } else if (prod.select(query.replace("???", "l")).size() > 0) {
      priceElem = prod.select(query.replace("???", "l")).first();
    } else if (prod.select(query.replace("???", "m")).size() > 0) {
      priceElem = prod.select(query.replace("???", "m")).first();
    }

    if (priceElem != null) {
      price.setCurrentPrice(
          price.priceFormat(priceElem.children().first().text(), country.getSymbol()));
      price.setCurrency(country.getCurrency());
    }

    if (prod.select("span[data-a-strike=\"true\"]").size() > 0) {
      priceElem = prod.select("span[data-a-strike=\"true\"]").first().firstElementChild();
      if (priceElem.text() != "") {
        price.setBeforePrice(price.priceFormat(priceElem.text(), country.getSymbol()));
      }
      price.setDiscounted(true);
      price.getSavings();
      if ("0".equals(price.getSavingsAmount())) {
        price.setDiscounted(false);
        price.setBeforePrice("0");
      }
    }
    return price;
  }

  public ProductInfoModel getProductDetails() {
    ProductInfoModel productInfo = new ProductInfoModel();
    productInfo.setAsin(asin);
    Element body = createConnection(null);
    Element temp, prod;

    // fetch Author
    prod = body.getElementById("bylineInfo");
    if (prod != null && prod.childrenSize() > 0) {
      ArrayList<AuthorModel> authorList = new ArrayList<AuthorModel>();
      AuthorModel authorObj;
      for (Element author : prod.children()) {
        try {
          authorObj = new AuthorModel();
          if (author.tagName() == "span" && author.hasClass("author")) {
            authorObj.setRole(author.select("span.contribution").text());
            if (author.children().first().tagName() == "a") {
              temp = author.children().first();
            } else {
              temp = author.getElementsByClass("a-declarative").first()
                  .getElementsByClass("a-link-normal contributorNameID").first();
            }
            if (temp.attr("href") != "#") {
              authorObj.setUrl(baseUrl + temp.attr("href"));
            }
            authorObj.setAuthor(temp.text());
            authorList.add(authorObj);
          }
        } catch (Exception e) {
          System.out.println("Error in Author Section");
        }
      }
      productInfo.setAuthorsList(authorList);
    }

    // extract categories
    prod = body;
    ArrayList<HashMap<String, String>> catList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> catMap;
    temp = prod.getElementById("wayfinding-breadcrumbs_feature_div");
    if (temp != null && temp.childrenSize() > 0) {
      temp = temp.selectFirst("ul");
      for (Element child : temp.children()) {
        try {
          if (child.classNames().size() == 0) {
            child = child.selectFirst("a");
            catMap = new HashMap<String, String>();
            catMap.put("url", baseUrl + child.attr("href"));
            catMap.put("category", child.text());
            catList.add(catMap);
          }
        } catch (Exception e) {
          System.out.println("Error in categories section");
        }
      }
    } else {
      temp = prod.getElementById("nav-subnav").getElementsByClass("nav-b").first();
      if (temp != null) {
        catMap = new HashMap<String, String>();
        catMap.put("category", temp.text());
        catMap.put("url", baseUrl + temp.attr("href"));
        catList.add(catMap);
      }
    }
    if (catList.size() > 0) {
      productInfo.setCategories(catList);
    }

    // set itemAvailable
    if (prod.getElementsByClass("qa-availability-message").first() != null) {
      productInfo.setItemAvailable(false);
    }

    // Product Variants
    extractProductVariants(body, productInfo);

    // product description
    temp = prod.getElementById("productDescription");
    if (temp != null) {
      productInfo.setDescription(temp.text());
    } else if (prod.getElementById("bookDescription_feature_div") != null) {
      temp = prod.getElementById("bookDescription_feature_div");
      productInfo.setDescription(temp.text());
    }

    // feature bullets
    productInfo.setFeatureBullets(getFeatureBullets(prod));

    // max order quantity
    temp = prod.getElementById("quantity");
    if (temp != null && temp.tagName() == "select") {
      productInfo.setMaxQuantity(temp.childrenSize());
    } else {
      productInfo.setMaxQuantity(1);
    }

    // title
    temp = prod.getElementById("productTitle");
    if (temp != null) {
      productInfo.setTitle(temp.text());
    } else {
      temp = prod.getElementsByClass("qa-title-text").first();
      if (temp != null) {
        productInfo.setTitle(temp.text());
      }
    }

    // review Data
    try {
      RatingModel rating = new RatingModel();
      temp = prod.getElementById("acrCustomerReviewText");
      if (temp != null) {
        rating.setTotalReview(temp.text().split(" ")[0].replaceAll(",", ""));
      }

      temp = prod.selectFirst("span.reviewCountTextLinkedHistogram.noUnderline");
      if (temp != null) {
        rating.setReviewRating(temp.text().split(" ")[0]);
      }
      productInfo.setRating(rating);
    } catch (Exception e) {
    }

    // Price Data
    try {
      PriceModel price = new PriceModel();
      price.setCurrency(country.getCurrency());
      temp = prod.selectFirst("span.a-price.priceToPay");
      if (temp != null) {
        temp = temp.firstElementChild();
      } else if (prod.selectFirst("span.a-price.apexPriceToPay") != null) {
        temp = prod.selectFirst("span.a-price.apexPriceToPay").firstElementChild();
      } else {
        temp = prod.getElementsByClass("a-price").first().firstElementChild();
      }
      price.setCurrentPrice(price.priceFormat(temp.text(), country.getSymbol()));

      temp = prod.selectFirst("span[data-a-strike=\"true\"]");
      if (temp != null) {
        temp = temp.firstElementChild();
        price.setBeforePrice(price.priceFormat(temp.text(), country.getSymbol()));
        price.setDiscounted(true);

        price.getSavings();
        if ("0".equals(price.getSavingsAmount())) {
          price.setDiscounted(false);
          price.setBeforePrice("0");
        }
      }

      productInfo.setPrice(price);
    } catch (Exception e) {
    }

    // brand
    try {
      if (prod.getElementsByClass("po-brand") != null) {
        temp = prod.getElementsByClass("po-brand").first().getElementsByTag("td").last();
        productInfo.setBrand(temp.text());
      } else if (prod.getElementById("bylineInfo") != null) {
        temp = prod.getElementById("bylineInfo");
        productInfo.setBrand(temp.text().replaceAll("Visit the ", "").replaceAll(" Store", ""));
      }
    } catch (Exception e) {
    }

    // other flags
    try {
      productInfo.setAmazonChoice(prod.selectFirst("div.ac-badge-wrapper") != null);
      productInfo.setPrime(prod.getElementById("priceBadging_feature_div") != null);
      productInfo.setBestSeller(prod.selectFirst("i.p13n-best-seller-badge") != null);
    } catch (Exception e) {
    }

    // extract specs
    extractFeatures(prod, productInfo);

    return productInfo;
  }

  public void extractProductVariants(Element prod, ProductInfoModel productInfo) {
    ArrayList<Object> list = new ArrayList<Object>();
    HashMap<String, Object> variant = null;
    Map<String, Object> map = null;
    try {
      Element script = prod.getElementById("imageBlockVariations_feature_div")
          .getElementsByTag("script").first();
      Matcher m = Pattern.compile("jQuery\\.parseJSON\\('(.*?)'\\)").matcher(script.toString());
      if (m.find()) {
        ObjectMapper mapper = new ObjectMapper();
        map = mapper.readValue(m.group(1), Map.class);
      }
    } catch (Exception e) {
      System.out.println("Error in json parser");
    }
    if (map != null && !map.isEmpty()) {
      HashMap colorToAsin = (HashMap) map.get("colorToAsin");
      HashMap colorImages = (HashMap) map.get("colorImages");
      for (Object key : colorToAsin.keySet()) {
        // System.out.println(key);
        try {
          variant = new HashMap<String, Object>();
          variant.put("asin", ((HashMap) colorToAsin.get(key)).get("asin"));
          if (colorImages.containsKey(key)) {
            ArrayList<HashMap<String, Object>> imageList = new ArrayList<HashMap<String, Object>>();
            for (HashMap<String, Object> image : (ArrayList<HashMap<String, Object>>) colorImages
                .get(key)) {
              HashMap<String, Object> imgObj = new HashMap<String, Object>();
              String url = (String) image.get("large");
              // if (this.storeToCF) {
              // String imgUrl = this.cfService.uploadImage(url);
              // if (imgUrl != null) {
              // url = imgUrl;
              // }
              // }
              imgObj.put("url", url);
              imgObj.put("isMain", "MAIN".equals(image.get("variant")));
              imageList.add(imgObj);
            }
            variant.put("images", imageList);
          }
          variant.put("title", key);
          variant.put("isCurrentProduct", this.asin.equals(variant.get("asin")));
        } catch (Exception e) {

        }
        if (variant != null && !variant.isEmpty()) {
          list.add(variant);
        }
      }
    }
    productInfo.setProductVariants(list);
  }

  public ArrayList<String> getFeatureBullets(Element prod) {
    ArrayList<String> list = new ArrayList<String>();
    Element temp = prod.getElementById("feature-bullets");
    try {
      if (temp != null) {
        temp = temp.selectFirst("ul");
        for (Element elem : temp.children()) {
          list.add(elem.text());
        }
      }
    } catch (Exception e) {
    }

    try {
      temp = prod.getElementById("feature-bullets").selectFirst("div[aria-expanded=\"false\"]");
      if (temp != null) {
        temp = temp.selectFirst("ul");
        for (Element elem : temp.children()) {
          list.add(elem.text());
        }
      }
    } catch (Exception e) {
    }

    return list;
  }

  public void extractFeatures(Element prod, ProductInfoModel productInfo) {
    Element temp;
    Elements eleArr = new Elements();
    List<String> avoidList = Arrays.asList("ASIN", "Customer Reviews", "Best Sellers Rank",
        "Manufacturer", "Packer", "Item Dimensions LxWxH");
    HashMap<String, String> map = new HashMap<String, String>();
    if (prod.getElementById("productDetails_techSpec_section_1") != null) {
      eleArr.add(prod.getElementById("productDetails_techSpec_section_1"));
    }
    if (prod.getElementById("productDetails_techSpec_section_2") != null) {
      eleArr.add(prod.getElementById("productDetails_techSpec_section_2"));
    }
    if (prod.getElementById("productDetails_detailBullets_sections1") != null) {
      eleArr.add(prod.getElementById("productDetails_detailBullets_sections1"));
    }
    if (prod.getElementsByClass("block-type-table") != null) {
      for (Element elem : prod.getElementsByClass("block-type-table")) {
        temp = elem.selectFirst("table");
        if (temp != null) {
          eleArr.add(temp);
        }
      }
    }

    for (Element table : eleArr) {
      if (table != null) {
        if (table.firstElementChild().tagName() == "tbody") {
          table = table.firstElementChild();
        }
        for (Element tr : table.children()) {
          if (tr.childrenSize() == 2 && !avoidList.contains(tr.child(0).text())) {
            map.put(tr.child(0).text(), tr.child(1).text());
          }
        }
      }
    }

    if (map.size() > 0) {
      productInfo.setTechSpecs(map);
    }
  }

  public RatingModel getReviews(int limit) {
    int page = 1;
    List<Object> reviewList = new ArrayList<Object>();
    RatingModel ratingModel = new RatingModel();
    HashMap<String, String> qs = new HashMap<String, String>();
    String text;
    Element body = createConnection(null);
    Element temp = body.getElementsByClass("AverageCustomerReviews").first();
    if (temp != null) {
      temp = temp.firstElementChild().select("div").last();
      if (temp != null) {
        text = temp.selectFirst("span").text();
        if (text != null && text.toLowerCase().contains("out of")) {
          ratingModel.setReviewRating(text.split(" ")[0]);
        }
      }
    }

    temp = body.select("#filter-info-section").first();
    if (temp != null) {
      temp = temp.select("div").last();
      if (temp != null) {
        try {
          text = temp.text();
          text = text.split(", ")[1];
          ratingModel.setTotalReview(text.split(" ")[0]);
        } catch (Exception e) {
        }
      }
    }

    while (reviewList.size() < limit) {
      qs.put("pageNumber", String.valueOf(page));
      body = createConnection(qs);
      temp = body.selectFirst("#cm_cr-review_list");
      Map<String, String> review;
      if (temp != null) {
        if (temp.children().select(".review").isEmpty()) {
          break;
        }
        for (Element div : temp.children().select(".review")) {
          try {
            if (div.tagName() == "div") {
              review = new LinkedHashMap<String, String>();
              review.put("id", div.attr("id"));
              if (div.selectFirst(".review-rating") != null) {
                text = div.selectFirst(".review-rating").text();
                if (text != null && text.toLowerCase().contains("out of")) {
                  review.put("rating", text.split(" ")[0]);
                }
              }
              if (div.selectFirst(".review-title-content") != null) {
                text = div.selectFirst(".review-title-content").text();
                review.put("title", text);
              }
              if (div.selectFirst(".a-profile-name") != null) {
                review.put("userName", div.selectFirst(".a-profile-name").text());
              }
              if (div.selectFirst(".review-date") != null) {
                review.put("date", div.selectFirst(".review-date").text().split("on ")[1]);
              }
              if (div.selectFirst(".review-text-content") != null) {
                review.put("review", div.selectFirst(".review-text-content").text());
              }

              reviewList.add(review);
              if (reviewList.size() == limit) {
                break;
              }
            }
          } catch (Exception e) {
          }
        }
        if (reviewList.size() == limit) {
          break;
        }
      }
      page++;
    }
    ratingModel.setReviewList(reviewList);
    ratingModel.setLimit(reviewList.size());

    return ratingModel;
  }

  public String getBrand() {
    Element body = createConnection(null);
    Element temp, prod;
    String brand = null;
    // prod = body.getElementById("bylineInfo");
    prod = body;
    try {
      if (prod.getElementsByClass("po-brand") != null) {
        temp = prod.getElementsByClass("po-brand").first().getElementsByTag("td").last();
        brand = temp.text();
      } else if (prod.getElementById("bylineInfo") != null) {
        temp = prod.getElementById("bylineInfo");
        brand = temp.text().replaceAll("Visit the ", "").replaceAll(" Store", "");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return brand;
  }

  public ArrayList<Object> getProductVariants() {
    Element body = createConnection(null);
    Element temp, prod;
    String brand = null;
    // prod = body.getElementById("bylineInfo");
    prod = body;
    ProductInfoModel product = new ProductInfoModel();
    extractProductVariants(prod, product);
    return product.getProductVariants();
  }

}
