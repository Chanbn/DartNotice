package com.dartNotice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DartNoticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DartNoticeApplication.class, args);
	}

}
