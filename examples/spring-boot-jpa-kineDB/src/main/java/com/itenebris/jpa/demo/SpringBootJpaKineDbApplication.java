package com.itenebris.jpa.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication//(exclude = TransactionAutoConfiguration.class)
@EnableJpaRepositories //(enableDefaultTransactions = false)
public class SpringBootJpaKineDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJpaKineDbApplication.class, args);
	}

}
