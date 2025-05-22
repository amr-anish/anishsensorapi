package com.example.anishsensorapi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.anishsensorapi.entity.Sensor;
import com.example.anishsensorapi.repository.SensorRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sensors")
@RequiredArgsConstructor
public class SensorController {

    /**
     * Controller for managing sensors. Provides endpoints to add, retrieve all,
     * and retrieve a specific sensor by ID.
     */

    private final SensorRepository sensorRepo;

    @PostMapping
    public ResponseEntity<?> addSensor(@Valid @RequestBody Sensor sensor) {
        /**
         * Adds a new sensor to the database.
         *
         * @param sensor The sensor object to be added. It must contain a unique
         * ID.
         * @return ResponseEntity<?>
         * - 200 OK: If the sensor is successfully added. - 409 Conflict: If a
         * sensor with the same ID already exists. - 500 Internal Server Error:
         * If an error occurs during the operation.
         */
        try {
            if (sensorRepo.existsById(sensor.getId())) {
                return ResponseEntity.status(409).body("Sensor with ID " + sensor.getId() + " already exists.");
            }

            Sensor savedSensor = sensorRepo.save(sensor);
            return ResponseEntity.ok(savedSensor);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while saving the sensor: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllSensors() {
        /**
         * Retrieves all sensors from the database.
         *
         * @return ResponseEntity<?>
         * - 200 OK: A list of all sensors. - 500 Internal Server Error: If an
         * error occurs during the operation.
         */
        try {
            List<Sensor> sensors = sensorRepo.findAll();
            return ResponseEntity.ok(sensors);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while retrieving sensors: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSensorById(@PathVariable String id) {
        /**
         * Retrieves a specific sensor by its ID.
         *
         * @param id The ID of the sensor to be retrieved.
         * @return ResponseEntity<?>
         * - 200 OK: If the sensor is found. - 404 Not Found: If the sensor with
         * the given ID does not exist. - 500 Internal Server Error: If an error
         * occurs during the operation.
         */
        try {
            Optional<Sensor> sensors = sensorRepo.findById(id);
            if (sensors.isPresent()) {
                return ResponseEntity.ok(sensors);
            } else {
                return ResponseEntity.status(404).body("Sensor with ID " + id + " not found.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while retrieving the sensor: " + e.getMessage());
        }
    }

}
