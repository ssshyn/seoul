package com.sm.seoulmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SeoulmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeoulmateApplication.class, args);
	}

}
