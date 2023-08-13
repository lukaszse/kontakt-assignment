package io.kontakt.apps.temperature.generator.temperaturegenerator

import java.util.function.BiFunction

import static io.kontakt.apps.temperature.generator.temperaturegenerator.SimpleTemperatureGenerator.BASIC_ANOMALY_GENERATOR
import static io.kontakt.apps.temperature.generator.temperaturegenerator.SimpleTemperatureGenerator.BASIC_NOISE_GENERATOR

class TemperatureGeneratorTestData {

    static SimpleTemperatureGenerator prepareHarmonicTempGenerator(int numberOfRooms,
                                                                   int numberOfThermometersPerRoom,
                                                                   BiFunction<Long, Double, Double> characteristic,
                                                                   double standardDeviation,
                                                                   double intMeanValue) {
        new SimpleTemperatureGenerator(
                numberOfRooms,
                numberOfThermometersPerRoom,
                standardDeviation,
                intMeanValue,
                characteristic,
                BASIC_NOISE_GENERATOR,
                BASIC_ANOMALY_GENERATOR)
    }
}
