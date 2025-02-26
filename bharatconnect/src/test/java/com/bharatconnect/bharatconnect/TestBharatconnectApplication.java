package com.bharatconnect.bharatconnect;

import org.springframework.boot.SpringApplication;

public class TestBharatconnectApplication {

	public static void main(String[] args) {
		SpringApplication.from(BharatconnectApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
