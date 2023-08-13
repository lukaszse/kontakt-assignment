package io.kontakt.apps.anomaly.analytics.infrastructure;

import io.kontakt.apps.event.Anomaly;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;

@Repository
 interface AnomalyRepository extends ReactiveSortingRepository<Anomaly, String>, AnomalyPersistencePort {

    Flux<Anomaly> findAllBy(Pageable pageable);
    Flux<Anomaly> findAllByRoomId(String roomId);
    Flux<Anomaly> findAllByThermometerId(String thermometerId);
    Mono<Long> countAllByThermometerId(String thermometerId);
}
