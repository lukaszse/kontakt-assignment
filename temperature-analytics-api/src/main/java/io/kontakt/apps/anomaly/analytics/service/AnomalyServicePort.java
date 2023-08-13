package io.kontakt.apps.anomaly.analytics.service;

import io.kontakt.apps.event.Anomaly;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.awt.print.Pageable;
import java.util.List;

public interface AnomalyServicePort {

    Mono<Tuple2<List<Anomaly>, Long>> findAllAnomalies(final Pageable pageable);

    Mono<List<Anomaly>> findByThermometerId(final String thermometerId);

    Mono<List<Anomaly>> findByRoomId(final String roomId);

    Mono<List<String>> findThermometersWithNumberOfAnomaliesHigherThan(final int numberOfAnomaliesThreshold);
}
