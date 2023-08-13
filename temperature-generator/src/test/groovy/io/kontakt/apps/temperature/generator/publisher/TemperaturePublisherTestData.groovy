package io.kontakt.apps.temperature.generator.publisher

import io.kontakt.apps.event.TemperatureReading
import io.kontakt.apps.temperature.generator.temperaturegenerator.TemperatureGenerator

import java.time.Instant
import java.util.stream.IntStream

class TemperaturePublisherTestData {

    static Random random = new Random();

    static boolean isBetween(value, min, max) {
        return value > min && value < max
    }

    static TemperatureGenerator testTestTempGenerator = () -> {
        IntStream.rangeClosed(1, 4).boxed().collect{
            new TemperatureReading(generateRandomDoubleValueFromRange(19,20), "r1", "t1", Instant.now())
        }
    }


    static double generateRandomDoubleValueFromRange(double min, double max) {
        return random.nextDouble(max - min + 1) + min;
    }
}
