package io.kontakt.apps.temperature.generator.temperaturegenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.kontakt.apps.temperature.generator.temperaturegenerator.SimpleTemperatureGenerator.BASIC_ANOMALY_GENERATOR;
import static io.kontakt.apps.temperature.generator.temperaturegenerator.SimpleTemperatureGenerator.BASIC_NOISE_GENERATOR;
import static io.kontakt.apps.temperature.generator.temperaturegenerator.SimpleTemperatureGenerator.HARMONIC_CHARACTERISTIC;

@Configuration
class TemperatureGeneratorConfig {

    @Bean
    TemperatureGenerator temperatureGenerator() {
        return new SimpleTemperatureGenerator(
                3,
                3,
                0.5,
                22,
                HARMONIC_CHARACTERISTIC,
                BASIC_NOISE_GENERATOR,
                BASIC_ANOMALY_GENERATOR);
    }
}
