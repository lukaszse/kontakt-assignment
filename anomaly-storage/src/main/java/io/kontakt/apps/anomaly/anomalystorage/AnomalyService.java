package io.kontakt.apps.anomaly.anomalystorage;

import io.kontakt.apps.event.Anomaly;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
class AnomalyService {

    private final AnomalyRepository anomalyRepository;

    public Mono<Anomaly> create(final Anomaly anomaly) {
        return anomalyRepository.save(anomaly)
                .doOnError(error -> log.error("Error while anomaly creation. Error {}", error.getMessage()))
                .doOnSuccess(createdAnomaly ->
                        log.debug("Anomaly successfully created in database. Anomaly: {}", createdAnomaly.toString()));
    }
}
