package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//是SpringBoot的入口程序，在Spring程序启动时，会进行扫描，扫描带有特殊注解的类
@SpringBootApplication
public class NiubikeApplication {

	public static void main(String[] args) {
		SpringApplication.run(NiubikeApplication.class, args);
	}

}
