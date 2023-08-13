package io.kontakt.apps.anomaly.anomalystorage;

import io.kontakt.apps.event.Anomaly;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AnomalyRepository extends ReactiveCrudRepository<Anomaly, String> {
}
