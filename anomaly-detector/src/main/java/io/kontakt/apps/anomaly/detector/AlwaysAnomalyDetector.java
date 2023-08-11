package io.kontakt.apps.anomaly.detector;

import io.kontakt.apps.anomaly.detector.util.CollectionUtils;
import io.kontakt.apps.event.Anomaly;
import io.kontakt.apps.event.TemperatureReading;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class AlwaysAnomalyDetector implements AnomalyDetector {
    public static final int MAX_TEMP_DEVIATION = 5;
    private static final int READING_SEQUENCE_LENGTH = 10;

    @Override
    public List<Anomaly> apply(List<TemperatureReading> temperatureReadings) {
        List<Anomaly> anomaliesTypeOne = checkAnomalyTypeOne(temperatureReadings);
        List<Anomaly> anomaliesTypeTwo = checkForAnomaly(temperatureReadings);
        return CollectionUtils.union(anomaliesTypeOne, anomaliesTypeTwo);
    }

    private static List<Anomaly> checkAnomalyTypeOne(final List<TemperatureReading> temperatureReadings) {
        return IntStream.range(0, temperatureReadings.size()).boxed()
                .map(i -> getFiveElementsStartingAt(temperatureReadings, i))
                .map(AlwaysAnomalyDetector::checkForAnomaly)
                .flatMap(Collection::stream)
                .toList();
    }

    private static <E> List<E> getFiveElementsStartingAt(final List<E> temperatureReadings,
                                                         final int firstElement) {
        final int lastElement = Math.min(firstElement + READING_SEQUENCE_LENGTH, temperatureReadings.size());
        return temperatureReadings.subList(firstElement, lastElement);
    }

    private static List<Anomaly> checkForAnomaly(final List<TemperatureReading> temperatureReadings) {
        final double mean = temperatureReadings.stream()
                .map(TemperatureReading::temperature)
                .mapToDouble(temp -> temp)
                .average()
                .orElseThrow();
        return temperatureReadings.stream()
                .filter(temperatureReading -> temperatureReading.temperature() > mean + MAX_TEMP_DEVIATION)
                .map(temperatureReading ->
                        new Anomaly(temperatureReading.temperature(),
                                temperatureReading.roomId(),
                                temperatureReading.thermometerId(),
                                temperatureReading.timestamp()))
                .toList();
    }
}
