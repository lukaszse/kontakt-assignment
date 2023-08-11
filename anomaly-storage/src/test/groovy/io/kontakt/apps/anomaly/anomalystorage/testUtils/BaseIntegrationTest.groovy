package io.kontakt.apps.anomaly.anomalystorage.testUtils


import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@Testcontainers
@SpringBootTest(classes = IntSpecConfig.class)
class BaseIntegrationTest extends Specification {

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"))

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.3")

    @Value('${spring.cloud.stream.bindings.anomaly-storage-in-0.destination}')
    String topic

    def conditions = new PollingConditions(timeout: 5, initialDelay: 1)

    def setupSpec() {
        kafkaContainer.start()
        mongoDBContainer.start()
    }

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers)
        registry.add("spring.data.mongodb.uri", () -> mongoDBContainer.getConnectionString())
    }

    def cleanupSpec() {
        kafkaContainer.stop()
        mongoDBContainer.stop()
    }
}
