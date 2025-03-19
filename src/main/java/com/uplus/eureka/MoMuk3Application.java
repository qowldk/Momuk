package com.uplus.eureka;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan({
    "com.uplus.eureka.domain.vote.repository",
    "com.uplus.eureka.domain.participant.repository"
})
@EnableScheduling
public class MoMuk3Application {

	public static void main(String[] args) {
		SpringApplication.run(MoMuk3Application.class, args);
	}
}
