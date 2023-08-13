package io.kontakt.apps.anomaly.detector.config;

import io.kontakt.apps.anomaly.detector.algorithm.AnomalyDetectionAlgorithm;
import io.kontakt.apps.anomaly.detector.AnomalyDetector;
import io.kontakt.apps.anomaly.detector.TemperatureAnomalyDetector;
import io.kontakt.apps.anomaly.detector.algorithm.TemperatureDeviationDetectionAlgorithm;
import io.kontakt.apps.anomaly.detector.algorithm.TemperatureDeviationInSubsequentReadingsDetectionAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemperatureAnomalyDetectorConfig {

    public static final int MAX_TEMP_DEVIATION = 5;
    public static final int READING_SEQUENCE_LENGTH = 10;

    @Bean
    AnomalyDetector anomalyDetector() {
        final AnomalyDetectionAlgorithm algorithmOne = new TemperatureDeviationDetectionAlgorithm(MAX_TEMP_DEVIATION);
        final AnomalyDetectionAlgorithm algorithmTwo =
                new TemperatureDeviationInSubsequentReadingsDetectionAlgorithm(MAX_TEMP_DEVIATION, READING_SEQUENCE_LENGTH);
        return new TemperatureAnomalyDetector(algorithmOne, algorithmTwo);
    }
}
