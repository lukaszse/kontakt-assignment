package io.kontakt.apps.anomaly.detector;

import io.kontakt.apps.anomaly.detector.algorithm.AnomalyDetectionAlgorithm;
import io.kontakt.apps.event.Anomaly;
import io.kontakt.apps.event.TemperatureReading;

import java.util.Arrays;
import java.util.List;

public class TemperatureAnomalyDetector implements AnomalyDetector {

    // I have decided to implement pluggable Algorithms, which allow meeting the Open-Close principle
    // In the future additional algorithms can be added or changed
    // This can be considered as a variation of the Strategy Design Pattern for multiple strategies

    private final List<AnomalyDetectionAlgorithm> anomalyDetectionAlgorithms;

    public TemperatureAnomalyDetector(final AnomalyDetectionAlgorithm ... anomalyDetectionAlgorithms) {
        this.anomalyDetectionAlgorithms = Arrays.asList(anomalyDetectionAlgorithms);
    }
    @Override
    public List<Anomaly> apply(List<TemperatureReading> temperatureReadings) {
        return anomalyDetectionAlgorithms.stream()
                .map(algorithm -> algorithm.findAnomalies(temperatureReadings))
                .flatMap(List::stream)
                .toList();
    }
}
