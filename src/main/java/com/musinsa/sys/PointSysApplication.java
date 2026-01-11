package com.musinsa.sys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.musinsa.sys")
public class PointSysApplication {
	public static void main(String[] args) {
		SpringApplication.run(PointSysApplication.class, args);
	}

}
