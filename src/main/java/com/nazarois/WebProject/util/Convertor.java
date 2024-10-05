package com.nazarois.WebProject.util;


import java.util.Base64;

public class Convertor {
  private Convertor() {}

  public static byte[] convertBase64ToString(String base64) {
    return Base64.getDecoder().decode(base64);
  }

}
