package com.stepuro.aviatickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AviaticketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AviaticketsApplication.class, args);
	}

}
