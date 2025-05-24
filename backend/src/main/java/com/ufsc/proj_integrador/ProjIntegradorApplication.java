package com.ufsc.proj_integrador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjIntegradorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjIntegradorApplication.class, args);
	}

}
