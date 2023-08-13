package io.kontakt.apps.anomaly.analytics.service;

import io.kontakt.apps.anomaly.analytics.infrastructure.AnomalyPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AnomalyServiceConfig {

    private final AnomalyPersistencePort anomalyPersistencePort;

    @Bean
    AnomalyServicePort anomalyServicePort() {
        return new AnomalyService(anomalyPersistencePort);
    }
}
