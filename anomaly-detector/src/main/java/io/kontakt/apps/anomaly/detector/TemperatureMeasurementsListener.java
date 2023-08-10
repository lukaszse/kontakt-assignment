package io.kontakt.apps.anomaly.detector;

import io.kontakt.apps.event.Anomaly;
import io.kontakt.apps.event.TemperatureReading;
import org.apache.kafka.streams.kstream.KStream;

import java.util.List;
import java.util.function.Function;

public class TemperatureMeasurementsListener implements Function<KStream<String, TemperatureReading>, KStream<String, Anomaly>> {

    private final AnomalyDetector anomalyDetector;

    public TemperatureMeasurementsListener(AnomalyDetector anomalyDetector) {
        this.anomalyDetector = anomalyDetector;
    }

    @Override
    public KStream<String, Anomaly> apply(KStream<String, TemperatureReading> events) {
        //TODO adapt to Recruitment Task requirements
        return events
                .mapValues((temperatureReading) -> anomalyDetector.apply(List.of(temperatureReading)))
                .filter((s, anomaly) -> anomaly.isPresent())
                .mapValues((s, anomaly) -> anomaly.get())
                .selectKey((s, anomaly) -> anomaly.thermometerId());
    }
}
