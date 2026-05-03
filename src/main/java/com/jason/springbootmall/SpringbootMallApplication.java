package com.jason.springbootmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringbootMallApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringbootMallApplication.class, args);
  }
}
