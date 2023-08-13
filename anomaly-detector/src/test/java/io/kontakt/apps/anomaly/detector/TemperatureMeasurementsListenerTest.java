package io.kontakt.apps.anomaly.detector;

import io.kontakt.apps.anomaly.detector.testUtils.AbstractIntegrationTest;
import io.kontakt.apps.anomaly.detector.testUtils.TestKafkaConsumer;
import io.kontakt.apps.anomaly.detector.testUtils.TestKafkaProducer;
import io.kontakt.apps.anomaly.detector.testUtils.TestUtils;
import io.kontakt.apps.event.Anomaly;
import io.kontakt.apps.event.TemperatureReading;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

public class TemperatureMeasurementsListenerTest extends AbstractIntegrationTest {

    @Value("${spring.cloud.stream.bindings.anomalyDetectorProcessor-in-0.destination}")
    private String inputTopic;

    @Value("${spring.cloud.stream.bindings.anomalyDetectorProcessor-out-0.destination}")
    private String outputTopic;

    @Test
    void testInOutFlow() {
        try (TestKafkaConsumer<Anomaly> consumer = new TestKafkaConsumer<>(
                kafkaContainer.getBootstrapServers(),
                outputTopic,
                Anomaly.class
        );
             TestKafkaProducer<TemperatureReading> producer = new TestKafkaProducer<>(
                     kafkaContainer.getBootstrapServers(),
                     inputTopic
             )) {
            IntStream.rangeClosed(1, 100).boxed().forEach(i -> {
                double temperature = i % 10 == 0 ? 30 : TestUtils.generateRandomDoubleValueFromRange(19, 21);
                TemperatureReading temperatureReading = new TemperatureReading(temperature,
                        "room", "thermometer", Instant.parse("2023-01-01T00:00:00.000Z"));
                producer.produce(temperatureReading.thermometerId(), temperatureReading);
            });
            consumer.drain(c -> c.size() > 1, Duration.ofSeconds(15)
            );
        }
    }
}
