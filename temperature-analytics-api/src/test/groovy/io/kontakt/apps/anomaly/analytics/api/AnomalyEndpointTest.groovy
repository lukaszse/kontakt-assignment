package io.kontakt.apps.anomaly.analytics.api

import io.kontakt.apps.anomaly.analytics.infrastructure.AnomalyPersistencePort
import io.kontakt.apps.anomaly.analytics.testUtils.AbstractEndpointSpec
import io.kontakt.apps.anomaly.analytics.testUtils.AnomalyTestUtils
import io.kontakt.apps.event.Anomaly
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.web.client.HttpClientErrorException

import static io.kontakt.apps.anomaly.analytics.testUtils.AnomalyTestUtils.ANOMALIES_URL_PATTERN
import static io.kontakt.apps.anomaly.analytics.testUtils.AnomalyTestUtils.ROOM_ID
import static io.kontakt.apps.anomaly.analytics.testUtils.AnomalyTestUtils.populateDatabase

class AnomalyEndpointTest extends AbstractEndpointSpec {

    @Autowired
    AnomalyPersistencePort anomalyPersistencePort

    def cleanup() {
        anomalyPersistencePort.deleteAll().block()
    }

    def "check is Spook int tests works :)"() {

        expect:
        1 == 1
    }

    def 'should find thermometers where anomaly number exceeds threshold'() {

        given: 'populate database with test data'
        populateDatabase(anomalyPersistencePort, numberOfAnomaliesPerThermometer, "thermometerId")

        and: 'prepare request to get anomalies'
        def request =
                RequestEntity.get(AnomalyTestUtils.ANOMALIES_URL_PATTERN.formatted(port, "/thermometers?anomaliesThreshold=%d".formatted(threshold)))
                        .accept(MediaType.APPLICATION_JSON)
                        .build()

        when: 'should fetch anomalies'
        def response = synchronousClient.exchange(request, new ParameterizedTypeReference<List<String>>() {})

        then:
        conditions.eventually {
            assert response != null
            assert response.getStatusCode() == HttpStatus.OK
            def fetchedAnomalies = response.getBody()
            assert fetchedAnomalies.size() == 1
        }

        where:
        numberOfAnomaliesPerThermometer | threshold
        2                               | 1
        10                              | 9
        3                               | 1
    }

    def 'should throw exception when no results found'() {

        given: 'populate database with test data'

        populateDatabase(anomalyPersistencePort, numberOfAnomaliesPerThermometer, "thermometerId")

        and: 'prepare request to get anomalies'
        def request =
                RequestEntity.get(AnomalyTestUtils.ANOMALIES_URL_PATTERN.formatted(port, "/thermometers?anomaliesThreshold=%d".formatted(threshold)))
                        .accept(MediaType.APPLICATION_JSON)
                        .build()

        when: 'should fetch anomalies'
        def response = synchronousClient.exchange(request, new ParameterizedTypeReference<List<String>>() {})

        then:
        def exception = thrown(HttpClientErrorException)
        conditions.eventually {
            assert exception.statusCode == HttpStatus.NOT_FOUND
        }

        where:
        numberOfAnomaliesPerThermometer | threshold
        5                               | 10
        25                              | 26
        225                             | 300
    }

    def 'should fetch anomalies by thermometerID'() {

        given: 'populate database with test data'

        def thermometerID = "thermometerID"
        populateDatabase(anomalyPersistencePort, numberOfAnomaliesInDatabase, thermometerID)

        and: 'prepare request to get anomalies'
        def request =
                RequestEntity.get(ANOMALIES_URL_PATTERN.formatted(port, "/%s".formatted(thermometerID)))
                        .accept(MediaType.APPLICATION_JSON)
                        .build()

        when: 'should fetch anomalies'
        def response = synchronousClient.exchange(request, new ParameterizedTypeReference<List<Anomaly>>() {})

        then:
        conditions.eventually {
            assert response != null
            assert response.getStatusCode() == HttpStatus.OK
            def fetchedAnomalies = response.getBody()
            assert fetchedAnomalies.size() == numberOfAnomaliesInDatabase
            assert fetchedAnomalies.every { it.roomId() == ROOM_ID }
        }

        where:
        numberOfAnomaliesInDatabase << [2, 5, 234 , 3423, 8]
    }
}
