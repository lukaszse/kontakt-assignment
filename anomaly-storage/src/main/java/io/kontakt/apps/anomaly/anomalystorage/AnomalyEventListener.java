package io.kontakt.apps.anomaly.anomalystorage;

import io.kontakt.apps.event.Anomaly;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class AnomalyEventListener {

    private final AnomalyService anomalyService;

    @KafkaListener(
            topics = "temperature-anomalies",
            containerFactory = "anomalyKafkaListenerContainerFactory")
    public void listener(Anomaly anomaly) {
        log.info("Anomaly received: {}", anomaly);
        anomalyService.create(anomaly)
                .subscribe();
    }
}
