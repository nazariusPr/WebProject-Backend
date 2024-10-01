package com.nazarois.WebProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "client")
public class WebProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebProjectApplication.class, args);
  }
}
