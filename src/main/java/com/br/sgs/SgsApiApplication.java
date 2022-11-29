package com.br.sgs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan("com.br.sgs")
public class SgsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgsApiApplication.class, args);
	}

}
