package com.achhecode.SpringBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBotApplication {

	public static void main(String[] args) {

		System.out.println(
                "java.awt.headless = "
                        + System.getProperty("java.awt.headless")
        );

		SpringApplication.run(SpringBotApplication.class, args);
	}

}
