package com.micnusz.pps;

import org.springframework.boot.SpringApplication;

public class TestPpsApplication {

	public static void main(String[] args) {
		SpringApplication.from(PpsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
