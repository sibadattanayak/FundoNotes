package com.bridgelabz.fundonotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FundoNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundoNotesApplication.class, args);
	}
}
