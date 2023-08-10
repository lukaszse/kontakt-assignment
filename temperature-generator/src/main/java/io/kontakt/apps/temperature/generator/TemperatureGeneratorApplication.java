package io.kontakt.apps.temperature.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
class TemperatureGeneratorApplication {
	public static void main(String[] args) {
		SpringApplication.run(TemperatureGeneratorApplication.class, args);
	}
}