package com.npst.evok.api.evok_apis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
@ConditionalOnClass
@SpringBootApplication
public class EvokApisApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvokApisApplication.class, args);
	}

}
