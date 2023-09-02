package com.KARUpliftUnity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KarUpliftUnityApplication {

	public static void main(String[] args) {
		SpringApplication.run(KarUpliftUnityApplication.class, args);
	}

}
