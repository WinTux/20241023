package com.wintux._3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	/*
	 * Para compilar en maven (jar):
	 * mvnw clean install
	 * 
	 * 
	 * Para compilar en maven (war):
	 * mvnw package
	 * Para compilar en gradle (war):
	 * gradlew build
	 * 
	 * */
}
