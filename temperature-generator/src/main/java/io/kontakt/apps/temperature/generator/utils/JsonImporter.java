package io.kontakt.apps.temperature.generator.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class JsonImporter {

    private static final String DATE_PATTERN = "MMM dd, yyyy, hh:mm:ss a";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

    public static String getStringFromFile(final String filePath) throws FileNotFoundException {
        final BufferedReader br = new BufferedReader(new FileReader(filePath));
        return br.lines()
                .collect(Collectors.joining("\n"));
    }

    public static String getStringFromFile(final File file) throws FileNotFoundException {
        final BufferedReader br = new BufferedReader(new FileReader(file));
        return br.lines()
                .collect(Collectors.joining("\n"));
    }

    public static <T> T getDataFromFile(final String filePath, final TypeReference<T> valueTypeRef) throws FileNotFoundException, JsonProcessingException {
        final String jsonString = getStringFromFile(filePath);
        return objectMapper().readValue(jsonString, valueTypeRef);
    }

    public static <T> T getDataFromFile(final String filePath, final Class<T> clazz) throws FileNotFoundException, JsonProcessingException {
        final String jsonString = getStringFromFile(filePath);
        return objectMapper().readValue(jsonString, clazz);
    }

    public static <T> T getDataFromFile(final File file, final TypeReference<T> valueTypeRef) throws FileNotFoundException, JsonProcessingException {
        final String jsonString = getStringFromFile(file);
        return objectMapper().readValue(jsonString, valueTypeRef);
    }

    public static <T> T getDataFromFile(final File file, final Class<T> clazz) throws FileNotFoundException, JsonProcessingException {
        final String jsonString = getStringFromFile(file);
        return objectMapper().readValue(jsonString, clazz);
    }

    public static ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .serializationInclusion(JsonInclude.Include.USE_DEFAULTS)
                .addModule(new JavaTimeModule())
                .defaultDateFormat(dateFormat)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true)
                .build();
    }
}
