package io.kontakt.apps.temperature.generator

import com.fasterxml.jackson.core.type.TypeReference
import io.kontakt.apps.temperature.generator.utils.JsonImporter
import spock.lang.Specification

import static io.kontakt.apps.temperature.generator.utils.TestData.isBetween

class PredefinedTemperatureGeneratorTest extends Specification {

    def "should generate reading for all rooms"() {

        given: "prepare input data"
        def predefinedTemperatures = JsonImporter.getDataFromFile("jsonData/predefinedReading.json", new TypeReference<List<Double>>() {
        })

        and: "initialize temperature generator"
        PredefinedTemperatureGenerator predefinedTemperatureGenerator =
                new PredefinedTemperatureGenerator(numberOfRooms, numberOfThermometersPerRoom, predefinedTemperatures)

        when:
        def result = predefinedTemperatureGenerator.generate()

        then:
        noExceptionThrown()
        result.size() == numberOfRooms * numberOfThermometersPerRoom
        def mean = result
                .collect() { it.temperature() }
                .average()
        isBetween(mean, 18, 22)

        where:
        numberOfRooms | numberOfThermometersPerRoom
        2             | 2
        5             | 5
        1             | 2
    }
}
