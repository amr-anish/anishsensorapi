package com.example.anishsensorapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.anishsensorapi.dto.QueryRequest;
import com.example.anishsensorapi.dto.UploadRequest;
import com.example.anishsensorapi.service.WeatherService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class WeatherController {

    /**
     * Controller for managing weather metrics. Provides endpoints to upload and
     * query metrics for sensors.
     */

    private final WeatherService weatherService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody @Valid UploadRequest request) {
        /**
         * Uploads metrics for a specific sensor.
         *
         * @param request The upload request containing sensor ID, metrics, and
         * timestamp.
         * @return ResponseEntity<?>
         * - 200 OK: If the metrics are successfully uploaded. 
         * - 400 Bad Request: If the input is invalid. 
         * - 500 Internal Server Error: If an error occurs during the operation.
         */
        try {
            return ResponseEntity.ok(weatherService.uploadMetrics(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while uploading metrics: " + e.getMessage());
        }
    }

    @PostMapping("/query")
    public ResponseEntity<?> query(@RequestBody @Valid QueryRequest request) {
        /**
         * Queries metrics for specific sensors and metrics types.
         *
         * @param request The query request containing sensor IDs, metric types,
         * statistic, and period.
         * @return ResponseEntity<?>
         * - 200 OK: If the metrics are successfully retrieved. 
         * - 400 Bad Request: If the input is invalid. 
         * - 500 Internal Server Error: If an error occurs during the operation.
         */
        try {
            return ResponseEntity.ok(weatherService.queryMetrics(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while querying metrics: " + e.getMessage());
        }
    }
}
