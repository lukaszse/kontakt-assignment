package io.kontakt.apps.temperature.generator


import spock.lang.Specification

import java.util.stream.IntStream

import static io.kontakt.apps.temperature.generator.SimpleTemperatureGenerator.CONSTANT_CHARACTERISTIC
import static io.kontakt.apps.temperature.generator.SimpleTemperatureGenerator.HARMONIC_CHARACTERISTIC
import static io.kontakt.apps.temperature.generator.utils.TestData.isBetween
import static io.kontakt.apps.temperature.generator.utils.TestData.prepareHarmonicTempGenerator

class SimpleTemperatureGeneratorTest extends Specification {


    def "Generate"() {
    }

    def "should generate series of single reading"() {

        given: "initialize temperature generator"
        SimpleTemperatureGenerator simpleTemperatureGenerator =
                prepareHarmonicTempGenerator(1, 1, characteristic, standardDeviation, intMeanValie)

        and: "prepare room and thermometer uuids"
        def roomUuid = UUID.randomUUID()
        def thermometerUuid = UUID.randomUUID()

        when: "generate series of temperature readings"
        def result = IntStream.rangeClosed(1, 20).boxed()
                .collect() { simpleTemperatureGenerator.generateSingleReading(roomUuid, thermometerUuid) }

        then:
        noExceptionThrown()
        result.every { isBetween(it.temperature(), min, max) }

        where:
        characteristic          | standardDeviation | intMeanValie | min | max
        HARMONIC_CHARACTERISTIC | 0.5               | 20           | 15  | 25
        HARMONIC_CHARACTERISTIC | 1.0               | 50           | 44  | 56
        CONSTANT_CHARACTERISTIC | 0.5               | 30           | 25  | 35
        CONSTANT_CHARACTERISTIC | 4.0               | 100          | 88  | 112
    }

    def "should generate reading for all rooms"() {

        given: "initialize temperature generator"
        SimpleTemperatureGenerator simpleTemperatureGenerator =
                prepareHarmonicTempGenerator(numberOfRooms, numberOfThermometersPerRoom, characteristic, standardDeviation, intMeanValie)

        when:
        def result = simpleTemperatureGenerator.generate()

        then:
        noExceptionThrown()
        result.size() == numberOfRooms * numberOfThermometersPerRoom

        where:
        characteristic          | standardDeviation | intMeanValie | min | max | numberOfRooms | numberOfThermometersPerRoom
        HARMONIC_CHARACTERISTIC | 0.5               | 20           | 15  | 25  | 2             | 2
        HARMONIC_CHARACTERISTIC | 1.0               | 50           | 44  | 56  | 3             | 3
        CONSTANT_CHARACTERISTIC | 0.5               | 20           | 16  | 24  | 1             | 2
        CONSTANT_CHARACTERISTIC | 4.0               | 100          | 88  | 112 | 3             | 1
    }
}
