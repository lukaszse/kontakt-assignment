package io.kontakt.apps.temperature.generator.publisher;

import io.kontakt.apps.event.TemperatureReading;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
class TemperatureStreamPublisher {

    private final Sinks.Many<Message<TemperatureReading>> messageProducer = Sinks.many().multicast().onBackpressureBuffer();

    public Flux<Message<TemperatureReading>> getMessageProducer() {
        return messageProducer.asFlux();
    }

    public void publish(TemperatureReading temperatureReading) {
        messageProducer.tryEmitNext(
                MessageBuilder.withPayload(temperatureReading)
                        .setHeader("identifier", temperatureReading.thermometerId())
                        .build()
        );
    }
}
