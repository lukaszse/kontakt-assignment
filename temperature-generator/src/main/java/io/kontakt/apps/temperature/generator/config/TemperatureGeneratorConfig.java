package io.kontakt.apps.temperature.generator.config;

import io.kontakt.apps.temperature.generator.SimpleTemperatureGenerator;
import io.kontakt.apps.temperature.generator.TemperatureGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.kontakt.apps.temperature.generator.SimpleTemperatureGenerator.*;

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
