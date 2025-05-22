package com.example.anishsensorapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.anishsensorapi.entity.MetricType;
import com.example.anishsensorapi.repository.MetricTypeRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricTypeController {

    /**
     * Controller for managing metric types. Provides endpoints to add,
     * retrieve, and delete metric types in the database.
     */

    private final MetricTypeRepository metricRepo;

    @PostMapping
    public ResponseEntity<?> addMetric(@RequestBody MetricType metricType) {
        /**
         * Adds a new metric type to the database.
         *
         * @param metricType The metric type to be added. It must contain a
         * unique name.
         * @return ResponseEntity<?>
         * - 200 OK: If the metric is successfully added. 
         * - 409 Conflict: If a metric with the same name already exists. 
         * - 500 Internal Server Error: If an error occurs during the operation.
         */
        try {
            if (metricRepo.existsById(metricType.getName())) {
                return ResponseEntity.status(409).body("Metric with name " + metricType.getName() + " already exists.");
            }
            MetricType savedMetric = metricRepo.save(metricType);
            return ResponseEntity.ok(savedMetric);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while saving the metric: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        /**
         * Retrieves all metric types from the database.
         *
         * @return ResponseEntity<?>
         * - 200 OK: A list of all metric types. 
         * - 500 Internal Server Error: If an error occurs during the operation.
         */
        try {
            List<MetricType> metrics = metricRepo.findAll();
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while retrieving metrics: " + e.getMessage());
        }
    }

}
