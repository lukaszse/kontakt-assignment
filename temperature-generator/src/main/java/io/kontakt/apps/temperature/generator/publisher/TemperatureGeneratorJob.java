package io.kontakt.apps.temperature.generator.publisher;

import io.kontakt.apps.temperature.generator.temperaturegenerator.TemperatureGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
class TemperatureGeneratorJob {

    private final TemperatureGenerator generator;
    private final TemperatureStreamPublisher publisher;


    @Scheduled(fixedRateString = "${temperature-generator.rate.seconds}", timeUnit = TimeUnit.SECONDS)
    public void generateDataAndSend() {
        generator.generate().forEach(publisher::publish);
    }
}
