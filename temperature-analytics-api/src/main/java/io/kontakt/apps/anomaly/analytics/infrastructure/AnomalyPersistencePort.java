package io.kontakt.apps.anomaly.analytics.infrastructure;

import io.kontakt.apps.event.Anomaly;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;

public interface AnomalyPersistencePort {

    Flux<Anomaly> findAll();
    Flux<Anomaly> findAllBy(Pageable pageable);
    Flux<Anomaly> findAllByRoomId(String roomId);
    Flux<Anomaly> findAllByThermometerId(String thermometerId);
    Mono<Long> countAllByThermometerId(String thermometerId);
    Mono<Long> count();
    Mono<Void> deleteAll();
    <S extends Anomaly> Mono<S> save(S entity);
}
