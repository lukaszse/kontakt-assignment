package io.kontakt.apps.anomaly.detector;

import io.kontakt.apps.event.Anomaly;
import io.kontakt.apps.event.TemperatureReading;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;

import java.time.Duration;
import java.util.ArrayList;
import java.util.function.Function;

public class TemperatureMeasurementsListener implements Function<KStream<String, TemperatureReading>, KStream<String, Anomaly>> {

    private final AnomalyDetector anomalyDetector;

    public TemperatureMeasurementsListener(final AnomalyDetector anomalyDetector) {
        this.anomalyDetector = anomalyDetector;
    }

    @Override
    public KStream<String, Anomaly> apply(final KStream<String, TemperatureReading> events) {
        return events
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(5)))
                .aggregate(() -> new ArrayList<TemperatureReading>(),
                        (key, value, aggregate) -> aggregateToList(value, aggregate))
                .toStream()
                .mapValues(anomalyDetector::apply)
                .flatMapValues((s, anomaly) -> anomaly)
                .selectKey((s, anomaly) -> anomaly.thermometerId());
    }

    private static ArrayList<TemperatureReading> aggregateToList(final TemperatureReading value,
                                                                 final ArrayList<TemperatureReading> aggregate) {
        aggregate.add(value);
        return aggregate;
    }
}
