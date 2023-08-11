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

    // This was the most time-consuming part of the task for me since I have never used either Kafka or Kafka Streams,
    // And that's why it is still not working, probably due to missing appropriate serializer or some configuration.
    // I'm aware also that this can be not an optimal solution, but I decided to aggregate all TemperatureReadings
    // for each 10-second windows, and then perform an anomaly check for aggregated List of TemperatureReadings.
    // That's why I decided to change returned element from Optional<Anomaly> to List<Anomaly>,
    // however the operation could be also implemented within the aggregation operation.

    @Override
    public KStream<String, Anomaly> apply(final KStream<String, TemperatureReading> events) {
        return events
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(10)))
                .aggregate(() -> new ArrayList<TemperatureReading>(),
                        (key, value, aggregate) -> aggregateToList(value, aggregate))
                .mapValues(anomalyDetector::apply)
                .toStream()
                .flatMapValues((s, anomaly) -> anomaly)
                .selectKey((s, anomaly) -> anomaly.thermometerId());
    }

    private static ArrayList<TemperatureReading> aggregateToList(final TemperatureReading value,
                                                                 final ArrayList<TemperatureReading> aggregate) {
        aggregate.add(value);
        return aggregate;
    }
}
