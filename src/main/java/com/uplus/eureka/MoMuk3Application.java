package com.uplus.eureka;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.uplus.eureka.dao")  // DAO 패키지 경로
public class MoMuk3Application {

	public static void main(String[] args) {
		SpringApplication.run(MoMuk3Application.class, args);
	}

}
