package com.mstore.util;


public class Constants {

  public static final String FALSE_STRING_LC = "false";

  // API HTTP Error Message Constants
  public static final String API_ERR_405 = "Unsupported HTTP Method";
  public static final String API_ERR_404 = "Invalid Request";
  public static final String API_ERR_415 =
      "Unsupported Content-Type header: supported types are application/json or application/xml";
  public static final String API_ERR_400 = "Malformed JSON request";
  public static final String API_ERR_406 =
      "Invalid Content-Type header: it should be application/json or application/xml";
  public static final String API_ERR_4XX_5XX = "Unknown Error";
  public static final String API_ERR_DB = "DB Error";
  public static final String _415_UNSUPPORTED_MEDIA_TYPE =
      "Invalid Content-Type header: it should be application/json or application/xml";
  public static final String _401_FAILED_AUTHENTICATION_PROCESS =
      "Request failed authentication/authorization process";
  public static final String _401_NO_CREDENTIALS = "No credentials provided";

}
