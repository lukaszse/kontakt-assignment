package io.kontakt.apps.anomaly.analytics.testUtils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

import java.util.stream.Collectors

class JsonImporter {

    // This is class is copied twice, but normally I use external library when this utils (written in Java and in Groovy as well)are deployed.
    // I have written it mainly for importing test data in integration and unit tests.

    private static final String TEST_RESOURCE_FILE_PATH_PATTERN = "src/test/resources/%s"

    static def objectMapper() {
        JsonMapper.builder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .addModule(new JavaTimeModule())
                .build();
    }

    static String getStringFromFile(final String resourceFilePath) {
        String filePath = getFileResourcePath(resourceFilePath)
        final BufferedReader br = new BufferedReader(new FileReader(filePath))
        return br.lines()
                .collect(Collectors.joining("\n"))
    }

    static String getFileResourcePath(String resourceFilePath) {
        final String filePath = String.format(TEST_RESOURCE_FILE_PATH_PATTERN, resourceFilePath)
        filePath
    }

    static <T> T getDataFromFile(final String filePath, final TypeReference<T> valueTypeRef) {
        def jsonString = getStringFromFile(filePath)
        def elements = objectMapper().readValue(jsonString, valueTypeRef)
        elements as T
    }

    static <T> T getDataFromFile(final String filePath, final Class<T> clazz) {
        def jsonString = getStringFromFile(filePath)
        def elements = objectMapper().readValue(jsonString, clazz)
        elements as T
    }
}
