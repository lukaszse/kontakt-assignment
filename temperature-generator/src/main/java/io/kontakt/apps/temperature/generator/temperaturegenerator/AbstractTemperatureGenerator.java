package io.kontakt.apps.temperature.generator.temperaturegenerator;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

abstract class AbstractTemperatureGenerator {

    public static long readingNo = 0L;
    protected final List<Room> rooms;

    protected AbstractTemperatureGenerator(final int numberOfRooms, final int numberOfThermometersPerRoom) {
        this.rooms = generateRooms(numberOfRooms, numberOfThermometersPerRoom);
    }

    protected static List<Room> generateRooms(final int numberOfRooms, final int numberOfThermometersPerRoom) {
        return IntStream.rangeClosed(1, numberOfRooms).boxed()
                .map(__ -> generateRoom(numberOfThermometersPerRoom))
                .toList();
    }
    protected static Room generateRoom(final int numberOfThermometersPerRoom) {
        final List<UUID> thermometerIds = IntStream.rangeClosed(1, numberOfThermometersPerRoom).boxed()
                .map(__ -> UUID.randomUUID())
                .toList();
        return Room.of(UUID.randomUUID(), thermometerIds);
    }
}
