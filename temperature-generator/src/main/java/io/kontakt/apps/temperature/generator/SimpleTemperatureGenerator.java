package io.kontakt.apps.temperature.generator;

import io.kontakt.apps.event.TemperatureReading;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

import static io.kontakt.apps.temperature.generator.AbstractTemperatureGenerator.generateRooms;

@Slf4j
public class SimpleTemperatureGenerator
        extends AbstractTemperatureGenerator
        implements TemperatureGenerator {

    public static final BiFunction<Long, Double, Double> CONSTANT_CHARACTERISTIC = (measurementNo, meanValue) -> meanValue;
    public static final BiFunction<Double, Double, Double> BASIC_NOISE_GENERATOR = SimpleTemperatureGenerator::generateNoise;
    public static final BiFunction<Long, Double, Double> HARMONIC_CHARACTERISTIC = prepareHarmonicCharacteristic();
    public static final Function<Double, Double> BASIC_ANOMALY_GENERATOR = SimpleTemperatureGenerator::generateAnomaly;
    private static final Random random = new Random();
    public static Long readingNo = 0L;
    private final double standardDeviation;
    private final double initMeanValue;
    private final BiFunction<Long, Double, Double> characteristic;
    private final BiFunction<Double, Double, Double> noiseGenerator;
    private final Function<Double, Double> anomalyGenerator;

    public SimpleTemperatureGenerator(final int numberOfRooms,
                                      final int numberOfThermometersPerRoom,
                                      final double standardDeviation,
                                      final double initMeanValue,
                                      final BiFunction<Long, Double, Double> characteristic,
                                      final BiFunction<Double, Double, Double> noiseGenerator,
                                      final Function<Double, Double> anomalyGenerator) {
        super(numberOfRooms, numberOfThermometersPerRoom);
        this.standardDeviation = standardDeviation;
        this.initMeanValue = initMeanValue;
        this.characteristic = characteristic;
        this.noiseGenerator = noiseGenerator;
        this.anomalyGenerator = anomalyGenerator;
    }


    public static SimpleTemperatureGenerator of(final int numberOfRooms,
                                         final int numberOfThermometersPerRoom,
                                         final double standardDeviation,
                                         final double initMeanValue,
                                         final BiFunction<Long, Double, Double> characteristic,
                                         final BiFunction<Double, Double, Double> noiseGenerator,
                                         final Function<Double, Double> anomalyGenerator) {
        return new SimpleTemperatureGenerator(numberOfRooms, numberOfThermometersPerRoom, standardDeviation, initMeanValue, characteristic, noiseGenerator, anomalyGenerator);
    }

    @Override
    public List<TemperatureReading> generate() {
        return rooms.stream()
                .map(this::generateTemperaturesForRoom)
                .flatMap(List::stream)
                .toList();
    }

    private List<TemperatureReading> generateTemperaturesForRoom(final Room room) {
        return room.getThermometerUuids().stream()
                .map(thermometerUuid -> generateSingleReading(room.getRoomUuid(), thermometerUuid))
                .toList();
    }

    private TemperatureReading generateSingleReading(final UUID roomUuid, final UUID thermometerUuid) {
        TemperatureReading temperatureReading = new TemperatureReading(
                generateTemperature(),
                roomUuid.toString(),
                thermometerUuid.toString(),
                Instant.now());
        log.debug("Temperature Reading: {}", temperatureReading);
        return temperatureReading;
    }
    private double generateTemperature() {
        final double temperature = characteristic.apply(readingNo, initMeanValue);
        final double temperatureWithNoise = noiseGenerator.apply(temperature, standardDeviation);
        return anomalyGenerator.apply(temperatureWithNoise);
    }

    private static BiFunction<Long, Double, Double> prepareHarmonicCharacteristic() {
        return (measurementNo, meanValue) -> {
            double amplitude = 2.0;
            double frequency = 0.1;
            return amplitude * Math.sin(measurementNo * frequency) + meanValue;
        };
    }

    private static double generateNoise(final Double temperature, final Double standardDeviation) {
        return temperature + random.nextGaussian() * standardDeviation;
    }

    private static Double generateAnomaly(final Double temperature) {
        final int randomNumber = generateRandomIntValueFromRange(1, 15);
        double disruption = randomNumber == 1 ? (double) generateRandomIntValueFromRange(100, 1000) / 250 : 0.0;
        return temperature + disruption;
    }

    private static int generateRandomIntValueFromRange(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
