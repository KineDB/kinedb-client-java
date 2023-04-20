package com.itenebris.kinedb.sdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SdkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdkApplication.class, args);
	}

}
