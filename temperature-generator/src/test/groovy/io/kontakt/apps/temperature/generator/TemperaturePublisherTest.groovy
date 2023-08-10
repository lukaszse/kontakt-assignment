package io.kontakt.apps.temperature.generator

import io.kontakt.apps.event.TemperatureReading
import io.kontakt.apps.temperature.generator.utils.TestKafkaConsumer
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired

import java.time.Duration
import java.time.Instant

import static io.kontakt.apps.temperature.generator.SimpleTemperatureGenerator.HARMONIC_CHARACTERISTIC
import static io.kontakt.apps.temperature.generator.utils.TestData.prepareHarmonicTempGenerator

class TemperaturePublisherTest extends BaseIntegrationTest {

    @Autowired
    TemperatureStreamPublisher publisher

    @SpringBean
    TemperatureGenerator temperatureGenerator =
            prepareHarmonicTempGenerator(2, 2, HARMONIC_CHARACTERISTIC, 1, 20)

    @SpringBean
    TemperatureGeneratorJob temperatureGeneratorJob = Mock()

    TestKafkaConsumer<TemperatureReading> consumer

    def setup() {
        consumer = new TestKafkaConsumer<>(
                kafkaContainer.getBootstrapServers(),
                topic,
                TemperatureReading.class)
    }

    def cleanup() {
        consumer.close()
    }

    def "should publish dummy temperature reading"() {

        given:
        TemperatureReading temperatureReading =
                new TemperatureReading(20d, "room", "thermometer", Instant.parse("2023-01-01T00:00:00.000Z"))

        when:
        publisher.publish(temperatureReading)

        and:
        def result =
                consumer.drain(consumerRecords -> consumerRecords.stream()
                        .anyMatch(r -> r.value().thermometerId() == temperatureReading.thermometerId()),
                        Duration.ofSeconds(5))

        then:
        noExceptionThrown()
        result.size() == 1
        result.get(0).roomId() == "room"
    }

    def "should publish generated temperature readings"() {

        given:
        def listOfReadings = temperatureGenerator.generate()

        when:
        listOfReadings.forEach { publisher.publish(it) }

        and:
        def result = consumer.drain(4, Duration.ofSeconds(5))

        then:
        noExceptionThrown()
        result.size() == 4
    }
}
