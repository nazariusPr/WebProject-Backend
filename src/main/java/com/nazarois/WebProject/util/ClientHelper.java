package com.nazarois.WebProject.util;

public class ClientHelper {
  private ClientHelper() {}

  private static final String CLIENT_URL = "http://localhost:9090";
  private static final String VERIFY_EMAIL_URL_FORMAT = "%s/verify?token=%s";

  public static String getVerificationUrl(String token) {
    return String.format(VERIFY_EMAIL_URL_FORMAT, CLIENT_URL, token);
  }
}