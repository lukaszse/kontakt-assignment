package io.kontakt.apps.temperature.generator.temperaturegenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
class Room {

    private UUID roomUuid;
    private List<UUID> thermometerUuids;
}
