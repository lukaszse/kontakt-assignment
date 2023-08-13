package io.kontakt.apps.anomaly.detector.detector;

import io.kontakt.apps.event.Anomaly;
import io.kontakt.apps.event.TemperatureReading;

import java.util.List;

public interface AnomalyDetectionAlgorithm {

    List<Anomaly> findAnomalies(List<TemperatureReading> temperatureReadings);
}
