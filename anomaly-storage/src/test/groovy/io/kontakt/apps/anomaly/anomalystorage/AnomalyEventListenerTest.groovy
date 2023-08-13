package io.kontakt.apps.anomaly.anomalystorage

import io.kontakt.apps.anomaly.anomalystorage.testUtils.BaseIntegrationTest
import io.kontakt.apps.anomaly.anomalystorage.testUtils.TestKafkaProducer
import io.kontakt.apps.event.Anomaly
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier

import java.time.Instant
import java.util.stream.IntStream

class AnomalyEventListenerTest extends BaseIntegrationTest {

    @Autowired
    AnomalyRepository anomalyRepository

    def "should receive and store message with anomaly"() {

        given: "create test producer"
        TestKafkaProducer producer = new TestKafkaProducer<>(kafkaContainer.getBootstrapServers(), topic)

        and: "emit given number of anomaly events"
        IntStream.rangeClosed(1, numberOfEvents).forEach {
            producer.produce("1", new Anomaly(30, "roomId", "thermometerId", Instant.now()))
        }

        when: "fetch all anomalies from database"
        def createdAnomalyFlux = anomalyRepository.findAll()

        then:
        conditions.eventually {
            assert StepVerifier
                    .create(createdAnomalyFlux)
                    .expectNextCount(numberOfEvents)
                    .expectComplete()
                    .verify()
        }

        cleanup:
        producer.close()
        anomalyRepository.deleteAll().block()

        where:
        numberOfEvents << [1, 5, 8, 12]
    }
}
