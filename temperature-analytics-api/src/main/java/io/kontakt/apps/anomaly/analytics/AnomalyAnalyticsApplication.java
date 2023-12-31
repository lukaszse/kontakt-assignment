package io.kontakt.apps.anomaly.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
class AnomalyAnalyticsApplication {
        public static void main(String[] args) {
            SpringApplication.run(AnomalyAnalyticsApplication.class, args);
        }
}
