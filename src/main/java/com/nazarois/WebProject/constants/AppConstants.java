package com.nazarois.WebProject.constants;

public class AppConstants {
  private AppConstants() {}

  public static final String REFRESH_TOKEN = "refresh_token";
  public static final String REFRESH_TOKEN_CLAIM = "custom:is_refresh_token";
  public static final String AUTH_LINK = "/api/v1/auth";
  public static final String ACTION_LINK = "/api/v1/action";
  public static final String SMALL_IMAGE_SIZE = "256x256";
  public static final String MEDIUM_IMAGE_SIZE = "512x512";
  public static final String LARGE_IMAGE_SIZE = "1024x1024";
  public static final String IMAGE_RESPONSE_FORMAT = "b64_json";
  public static final String AMAZON_URL = "https://%s.s3.amazonaws.com/%s";
  public static final String IMAGE_CONTENT_TYPE = "image/png";
}
