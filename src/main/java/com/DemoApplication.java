package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {
	public static void main(String[] args) {
		TimeZone t = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(t);
		SpringApplication.run(DemoApplication.class, args);
	}
}