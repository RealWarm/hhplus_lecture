package com.hhplus.lecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class LectureApplication {

	public static void main(String[] args) {
		SpringApplication.run(LectureApplication.class, args);
	}

}
