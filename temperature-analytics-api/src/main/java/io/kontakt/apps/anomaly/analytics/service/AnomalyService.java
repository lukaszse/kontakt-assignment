package io.kontakt.apps.anomaly.analytics.service;

import io.kontakt.apps.anomaly.analytics.exception.NotFoundException;
import io.kontakt.apps.anomaly.analytics.infrastructure.AnomalyPersistencePort;
import io.kontakt.apps.event.Anomaly;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.awt.print.Pageable;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
class AnomalyService implements AnomalyServicePort {

    private static final String NOT_FOUND_ERROR_MESSAGE = "No anomalies with meeting criteria found. %s";

    private final AnomalyPersistencePort anomalyPersistencePort;


    public Mono<Tuple2<List<Anomaly>, Long>> findAllAnomalies(final Pageable pageable) {
        return anomalyPersistencePort.findAllBy(pageable)
                .switchIfEmpty(Mono.error(new NotFoundException(NOT_FOUND_ERROR_MESSAGE)))
                .doOnError(error -> log.error("Error while reading anomalies. Error={}", error.getMessage()))
                .collectList()
                .zipWith(anomalyPersistencePort.count());
    }

    public Mono<List<Anomaly>> findByThermometerId(final String thermometerId) {
        return anomalyPersistencePort.findAllByThermometerId(thermometerId)
                .switchIfEmpty(Mono.error(
                        new NotFoundException(NOT_FOUND_ERROR_MESSAGE.formatted("Criteria: thermometerId=%s".formatted(thermometerId)))))
                .doOnError(error -> log.error("Error while reading anomalies where thermometerId={}. Error={}", thermometerId, error.getMessage()))
                .collectList();
    }

    public Mono<List<Anomaly>> findByRoomId(final String roomId) {
        return anomalyPersistencePort.findAllByRoomId(roomId)
                .switchIfEmpty(Mono.error(
                        new NotFoundException(NOT_FOUND_ERROR_MESSAGE.formatted("Criteria: thermometerId=%s".formatted(roomId)))))
                .doOnError(error -> log.error("Error while reading anomalies where roomId={}. . Error={}", roomId, error.getMessage()))
                .collectList();
    }

    // This aggregation can be done also directly on the database, in that case, the MongoTemplate and built-in query mechanism can be used,
    // annotation @Query"{MongoQuery}" or original MongoDb driver which allows using native MongoDB queries can be used.
    // Following solution is not optimal because uses two separate request to database, while solution with aggregation on database
    // would result  only single request.
    public Mono<List<String>> findThermometersWithNumberOfAnomaliesHigherThan(final int numberOfAnomaliesThreshold) {
        final Flux<String> thermometerIds = anomalyPersistencePort.findAll()
                .map(Anomaly::thermometerId)
                .distinct();
        return thermometerIds
                .flatMap(thermometerId -> anomalyPersistencePort.countAllByThermometerId(thermometerId)
                        .filter(anomaliesForThermometer -> anomaliesForThermometer >= numberOfAnomaliesThreshold)
                        .switchIfEmpty(Mono.error(
                                new NotFoundException(NOT_FOUND_ERROR_MESSAGE.formatted("Criteria: number of anomalies > %s"
                                        .formatted(numberOfAnomaliesThreshold)))))
                        .doOnError(error -> log.error("Error while reading thermometer IDs for anomaly threshold. Error: {}", error.getMessage()))
                        .map(anomalies -> thermometerId))
                .collectList();
    }
}

