package io.kontakt.apps.anomaly.detector.algorithm;

import io.kontakt.apps.event.Anomaly;
import io.kontakt.apps.event.TemperatureReading;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class TemperatureDeviationInSubsequentReadingsDetectionAlgorithm
        extends TemperatureDeviationDetectionAlgorithm {

    private final int readingSequenceLength;

    public TemperatureDeviationInSubsequentReadingsDetectionAlgorithm(int maxTempDeviation, int readingSequenceLength) {
        super(maxTempDeviation);
        this.readingSequenceLength = readingSequenceLength;
    }

    @Override
    public List<Anomaly> findAnomalies(List<TemperatureReading> temperatureReadings) {
        return findAnomaliesInSeriesOfSubsequentReadings(temperatureReadings);
    }

    private List<Anomaly> findAnomaliesInSeriesOfSubsequentReadings(final List<TemperatureReading> temperatureReadings) {
        return IntStream.range(0, temperatureReadings.size()).boxed()
                .map(i -> getElementSequenceStartingAt(temperatureReadings, i))
                .map(this::findAnomaliesInAllReadings)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<TemperatureReading> getElementSequenceStartingAt(final List<TemperatureReading> temperatureReadings,
                                                                  final int firstElement) {
        final int lastElement = Math.min(firstElement + readingSequenceLength, temperatureReadings.size());
        return temperatureReadings.subList(firstElement, lastElement);
    }
}
