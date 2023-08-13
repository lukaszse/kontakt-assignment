package io.kontakt.apps.anomaly.analytics.testUtils

import groovy.util.logging.Slf4j
import io.kontakt.apps.anomaly.analytics.infrastructure.AnomalyPersistencePort
import io.kontakt.apps.anomaly.analytics.infrastructure.AnomalyRepository
import io.kontakt.apps.event.Anomaly

import java.time.Instant
import java.util.stream.IntStream

@Slf4j
class AnomalyTestUtils {

    static Random random = new Random();
    static def ANOMALIES_URL_PATTERN = "http://localhost:%d/anomalies%s"
    public static final String ROOM_ID = "roomId"


    static def populateDatabase(AnomalyPersistencePort anomalyPersistencePort, int numberOfAnomalies, String thermometerId) {
        def anomaliesToAdd = generateAnomalies(numberOfAnomalies, thermometerId)
        def addedAnomalies = anomaliesToAdd.stream()
                .map { anomalyPersistencePort.save(it) }
                .collect{it.block()}
        log.info("Anomalies added to database: {}", addedAnomalies)
    }

    static List<Anomaly> generateAnomalies(int numberOfReadings, String thermometerId) {
        IntStream.rangeClosed(1, numberOfReadings).boxed()
                .collect { generateAnomaly(thermometerId) }
    }

    static Anomaly generateAnomaly(final String thermometerId) {
        def temp = 30 + random.nextGaussian() * 5
        new Anomaly(temp, ROOM_ID, thermometerId, Instant.now())
    }
}
