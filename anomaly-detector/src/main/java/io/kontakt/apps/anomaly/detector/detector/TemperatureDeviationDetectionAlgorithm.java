package io.kontakt.apps.anomaly.detector.detector;

import io.kontakt.apps.anomaly.detector.util.CollectionUtils;
import io.kontakt.apps.event.Anomaly;
import io.kontakt.apps.event.TemperatureReading;

import java.util.List;

class TemperatureDeviationDetectionAlgorithm implements AnomalyDetectionAlgorithm {

    private final int maxTempDeviation;

    public TemperatureDeviationDetectionAlgorithm(int maxTempDeviation) {
        this.maxTempDeviation = maxTempDeviation;
    }

    @Override
    public List<Anomaly> findAnomalies(List<TemperatureReading> temperatureReadings) {
        return findAnomaliesInAllReadings(temperatureReadings);
    }

    protected List<Anomaly> findAnomaliesInAllReadings(final List<TemperatureReading> temperatureReadings) {
        final List<Double> temperatures = extractTemperatures(temperatureReadings);
        final double mean = CollectionUtils.calculateMeanValue(temperatures);
        return temperatureReadings.stream()
                .filter(temperatureReading -> temperatureReading.temperature() > mean + maxTempDeviation)
                .map(temperatureReading ->
                        new Anomaly(temperatureReading.temperature(),
                                temperatureReading.roomId(),
                                temperatureReading.thermometerId(),
                                temperatureReading.timestamp()))
                .toList();
    }

    protected static List<Double> extractTemperatures(List<TemperatureReading> temperatureReadings) {
        return temperatureReadings.stream()
                .map(TemperatureReading::temperature)
                .toList();
    }
}
