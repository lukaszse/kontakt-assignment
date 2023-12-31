package io.kontakt.apps.anomaly.anomalystorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
class AnomalyStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnomalyStorageApplication.class, args);
    }
}
