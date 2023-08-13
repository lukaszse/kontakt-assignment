package io.kontakt.apps.anomaly.analytics;

import io.kontakt.apps.event.Anomaly;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;

@Repository
public interface AnomalyRepository extends ReactiveSortingRepository<Anomaly, String> {

    Flux<Anomaly> findAllBy(Pageable pageable);
    Flux<Anomaly> findAllByRoomId(String roomId);
    Flux<Anomaly> findAllByThermometerId(String thermometerId);
    Mono<Long> countAllByThermometerId(String thermometerId);
}
