package io.kontakt.apps.anomaly.analytics.api;

import io.kontakt.apps.anomaly.analytics.service.AnomalyServicePort;
import io.kontakt.apps.event.Anomaly;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@RestController
@RequestMapping("/anomalies")
@RequiredArgsConstructor
@Validated
class AnomalyEndpoint {

    private static final String X_TOTAL_COUNT_HEADER = "x-total-count";

    private final AnomalyServicePort anomalyServicePort;


    // Pagination implemented only for one endpoint (taking into consideration the potential huge number of data, it should be implemented for all endpoints which return a list of objects)
    // Endpoint with pagination returns also a total number of elements within x-total-count, which can be used on pagination on the frontend app.
    // Swagger annotations create automatically the OpenAPI documentation.

    @Operation(summary = "Find all anomalies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Anomalies found",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Anomaly.class)))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content)})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Anomaly>>> findAll(final Pageable pageable) {
        log.info("Find all anomalies for thermometerId");
        return anomalyServicePort.findAllAnomalies(pageable)
                .doOnSuccess(anomalies -> log.info("{} anomalies found.", anomalies.size()))
                .map(tuple -> ResponseEntity.ok().headers(prepareXTotalCountHeader(tuple.getT2())).body(tuple.getT1()));
    }

    @Operation(summary = "Find anomalies by thermometer ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Anomalies found",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Anomaly.class)))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content)})
    @GetMapping(value = "/{thermometerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Anomaly>>> findAnomaliesByThermometerId(@PathVariable final String thermometerId) {
        log.info("Find anomalies for thermometerId={}", thermometerId);
        return anomalyServicePort.findByThermometerId(thermometerId)
                .doOnSuccess(anomalies -> log.info("{} anomalies for thermometerId={} found.", anomalies.size(), thermometerId))
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Find anomalies by room ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Anomalies found",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Anomaly.class)))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content)})
    @GetMapping(value = "/rooms/{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Anomaly>>> findAnomaliesByRoomId(@PathVariable final String roomId) {
        log.info("Find anomalies for roomId={}", roomId);
        return anomalyServicePort.findByRoomId(roomId)
                .doOnSuccess(anomalies -> log.info("{} anomalies for thermometerId={} found.", anomalies.size(), roomId))
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Find thermometers where anomaly number exceeds threshold")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thermometer IDs found",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = String.class)))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content)})
    @GetMapping(value = "/thermometers", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<String>>> findThermometerWhereNumberOfAnomaliesIsGreaterThan(@RequestParam final int anomaliesThreshold) {
        log.info("Find thermometers where number of anomalies is greater than {}", anomaliesThreshold);
        return anomalyServicePort.findThermometersWithNumberOfAnomaliesHigherThan(anomaliesThreshold)
                .doOnSuccess(anomalies -> log.info("{} anomalies for thermometerId={} found.", anomalies.size(), anomaliesThreshold))
                .map(ResponseEntity::ok);
    }

    private static HttpHeaders prepareXTotalCountHeader(final Long count) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set(X_TOTAL_COUNT_HEADER, String.valueOf(count));
        headers.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, X_TOTAL_COUNT_HEADER);
        return headers;
    }
}
