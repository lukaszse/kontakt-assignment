package io.kontakt.apps.temperature.generator.testUtils

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.testcontainers.shaded.org.awaitility.Awaitility
import org.testcontainers.shaded.org.awaitility.Durations

import java.time.Duration
import java.util.function.Predicate
import java.util.stream.Collectors

import static org.junit.jupiter.api.Assertions.assertTrue

class TestKafkaConsumer<V> {

    protected Consumer<String, V> consumer

    TestKafkaConsumer(String bootstrapServers,
                             String outputTopicName,
                             Class<V> valueClass) {
        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps(
                bootstrapServers,
                "testGroup",
                "true")
        JsonDeserializer<V> deserializer = new JsonDeserializer<>(valueClass)
        deserializer.addTrustedPackages("*")
        this.consumer = new DefaultKafkaConsumerFactory<>(consumerProperties,
                new StringDeserializer(),
                deserializer).createConsumer()
        this.consumer.subscribe(List.of(outputTopicName))
    }

    List<V> drain(int expectedRecordCount, Duration waitAtMost) {
        return drainRecords(consumerRecords -> consumerRecords.size() == expectedRecordCount, waitAtMost)
                .stream()
                .map(ConsumerRecord::value)
                .collect(Collectors.toList())
    }

    List<V> drain(Predicate<List<ConsumerRecord<String, V>>> predicate, Duration waitAtMost) {
        return drainRecords(predicate, waitAtMost)
                .stream()
                .map(ConsumerRecord::value)
                .collect(Collectors.toList())
    }

    void assertNoMoreRecords() {
        assertNoMoreRecords(Duration.ofSeconds(1L))
    }

    void assertNoMoreRecords(Duration waitAtMost) {
        assertTrue(consumer.poll(waitAtMost).isEmpty())
    }

    private List<ConsumerRecord<String, V>> drainRecords(Predicate<List<ConsumerRecord<String, V>>> predicate, Duration waitAtMost) {
        List<ConsumerRecord<String, V>> allRecords = new ArrayList<>()
        Awaitility.await()
                .atMost(waitAtMost)
                .pollInterval(Durations.ONE_HUNDRED_MILLISECONDS).until(() -> {
            ConsumerRecords<String, V> poll = consumer.poll(Duration.ofMillis(50))
            poll
                    .iterator()
                    .forEachRemaining(allRecords::add)
            return predicate.test(allRecords)
        })
        return allRecords
    }

    void close() {
        this.consumer.close()
    }
}
