package com.sandstorm.internshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class InternshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternshopApplication.class, args);
	}
}
