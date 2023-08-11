package io.kontakt.apps.temperature.generator;

import io.kontakt.apps.event.TemperatureReading;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Slf4j
public class PredefinedTemperatureGenerator
        extends AbstractTemperatureGenerator
        implements TemperatureGenerator {

    // This is an alternative temperature generator which does not draw numbers, but just takes them from
    // list of predefined readings provided while instantiation.
    // This list of temperatures can be for example read from any file, as it is shown within the unit test of this generator.

    private final List<Double> predefinedTemperatures;

    protected PredefinedTemperatureGenerator(final int numberOfRooms,
                                             final int numberOfThermometersPerRoom,
                                             final List<Double> predefinedTemperatures) {
        super(numberOfRooms, numberOfThermometersPerRoom);
        this.predefinedTemperatures = predefinedTemperatures;
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
                getNextTemperatureReading(),
                roomUuid.toString(),
                thermometerUuid.toString(),
                Instant.now());
        log.debug("Temperature Reading: {}", temperatureReading);
        return temperatureReading;
    }

    private Double getNextTemperatureReading() {
        if (readingNo > predefinedTemperatures.size() - 1) {
            readingNo = 0L;
        }
        return predefinedTemperatures.get((int) readingNo++);
    }
}
