package com.turing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.turing.mapper")
public class GoShop12Application {

	public static void main(String[] args) {
		SpringApplication.run(GoShop12Application.class, args);
	}

}
