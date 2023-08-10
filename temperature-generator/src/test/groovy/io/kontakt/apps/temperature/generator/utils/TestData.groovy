package io.kontakt.apps.temperature.generator.utils

import io.kontakt.apps.temperature.generator.SimpleTemperatureGenerator

import java.util.function.BiFunction

import static io.kontakt.apps.temperature.generator.SimpleTemperatureGenerator.BASIC_ANOMALY_GENERATOR
import static io.kontakt.apps.temperature.generator.SimpleTemperatureGenerator.BASIC_NOISE_GENERATOR

class TestData {

    static boolean isBetween(value, min, max) {
        return value > min && value < max
    }

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
