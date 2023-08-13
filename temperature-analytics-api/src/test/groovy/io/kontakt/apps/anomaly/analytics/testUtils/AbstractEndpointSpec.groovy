package io.kontakt.apps.anomaly.analytics.testUtils


import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.web.client.RestTemplate
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@Testcontainers
@SpringBootTest(classes = IntSpecConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AbstractEndpointSpec extends Specification {

    @LocalServerPort
    int port

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.3")


    @Shared
    def synchronousClient = new RestTemplate()

    def conditions = new PollingConditions(timeout: 5, initialDelay: 1)

    def setupSpec() {
        mongoDBContainer.start()
    }

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongoDBContainer.getConnectionString())
    }

    def cleanupSpec() {
        mongoDBContainer.stop()
    }
}