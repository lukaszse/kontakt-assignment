package io.kontakt.apps.event;

import java.time.Instant;

public record TemperatureReading(double temperature, String roomId, String thermometerId, Instant timestamp) {

}
