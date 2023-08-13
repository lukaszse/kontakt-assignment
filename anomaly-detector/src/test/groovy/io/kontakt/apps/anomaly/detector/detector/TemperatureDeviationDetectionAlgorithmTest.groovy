package io.kontakt.apps.anomaly.detector.detector


import io.kontakt.apps.event.TemperatureReading
import spock.lang.Specification

import java.time.Instant
import java.util.stream.IntStream

import static io.kontakt.apps.anomaly.detector.testUtils.TestUtils.*

class TemperatureDeviationDetectionAlgorithmTest extends Specification {

    AnomalyDetectionAlgorithm anomalyDetectionAlgorithm = new TemperatureDeviationDetectionAlgorithm(5)

    def "should detect all anomalies"() {

        given: "prepare input data"
        def correctReading = IntStream.rangeClosed(1, numberOfCorrectReadings).boxed().collect() {
            new TemperatureReading(
                    generateRandomDoubleValueFromRange(19, 20), "r1", "t1", Instant.now())
        }
        def wrongReadings = IntStream.rangeClosed(1, numberOfWrongReadings).boxed().collect {
            new TemperatureReading(
                    it + 30, "r1", "t1", Instant.now())
        }
        def inputReadings = unionAndShuffle(correctReading, wrongReadings)

        when:
        def result = anomalyDetectionAlgorithm.findAnomalies(inputReadings)

        then:
        noExceptionThrown()
        result.size() == numberOfWrongReadings

        where:
        numberOfCorrectReadings | numberOfWrongReadings
        100                     | 10
        200                     | 5
        50                      | 2
        4                       | 1
        8                       | 2
        234                     | 23
    }
}
