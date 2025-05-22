package com.example.anishsensorapi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.anishsensorapi.dto.MetricStatDTO;
import com.example.anishsensorapi.dto.QueryRequest;
import com.example.anishsensorapi.dto.UploadRequest;
import com.example.anishsensorapi.entity.MetricData;
import com.example.anishsensorapi.entity.MetricType;
import com.example.anishsensorapi.entity.Sensor;
import com.example.anishsensorapi.repository.MetricDataRepository;
import com.example.anishsensorapi.repository.MetricTypeRepository;
import com.example.anishsensorapi.repository.SensorRepository;
import com.example.anishsensorapi.utility.DateTimeParser;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherService {

    /**
     * Service for managing weather metrics. Provides functionality to upload
     * metrics for sensors and query aggregated metrics.
     */
    private final SensorRepository sensorRepo;
    private final MetricDataRepository metricRepo;
    private final MetricTypeRepository metricTypeRepo;

    private void validateMetrics(List<String> metrics) {
        /**
         * Validates the provided metrics against the allowed metrics in the
         * system.
         *
         * @param metrics A list of metric names to validate.
         * @throws IllegalArgumentException If any of the provided metrics are
         * not allowed.
         */
        List<String> allowedMetrics = metricTypeRepo.findAll().stream()
                .map(MetricType::getName).toList();

        for (String metric : metrics) {
            if (!allowedMetrics.contains(metric)) {
                throw new IllegalArgumentException("Metric not allowed: " + metric);
            }
        }
    }

    public List<MetricData> uploadMetrics(UploadRequest req) {
        /**
         * Uploads metrics for a specific sensor.
         *
         * @param req The upload request containing sensor ID, metrics, and
         * timestamp.
         * @return A list of saved MetricData objects.
         * @throws IllegalArgumentException If the input metrics are invalid.
         * @throws RuntimeException If an error occurs during the upload
         * process.
         */
        try {

            validateMetrics(req.getMetrics().keySet().stream().toList());

            Sensor sensor = sensorRepo.findById(req.getSensorId())
                    .orElseGet(() -> sensorRepo.save(new Sensor(req.getSensorId(), req.getSensorId())));

            List<MetricData> savedMetrics = new ArrayList<>();

            req.getMetrics().forEach((metric, value) -> {
                MetricData data = new MetricData(null, sensor, metric, value, req.getTimestamp());
                MetricData savedData = metricRepo.save(data);
                savedMetrics.add(savedData);
            });

            return savedMetrics;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid input: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while uploading metrics: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings({"CollectionsToArray", "unused"})
    public MetricStatDTO queryMetrics(QueryRequest req) {
        /**
         * Queries aggregated metrics for specific sensors and metric types.
         *
         * @param req The query request containing sensor IDs, metric types,
         * statistic, and period.
         * @return A MetricStatDTO object containing aggregated metrics for the
         * specified criteria.
         * @throws IllegalArgumentException If the input metrics or statistic
         * are invalid.
         * @throws RuntimeException If an error occurs during the query process.
         */
        try {

            // Validate the input metrics
            validateMetrics(req.getMetrics());

            // Validating the time period
            LocalDateTime[] range = req.getPeriod() != null
                    ? DateTimeParser.parse(req.getPeriod())
                    : new LocalDateTime[]{LocalDateTime.now().minusDays(1), LocalDateTime.now()};

            LocalDateTime start = range[0];
            LocalDateTime end = range[1];

            // Create a specification to filter the data based on sensor IDs, Metric and timestamp
            Specification<MetricData> spec = (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(root.get("sensor").get("id").in(req.getSensorIds()));
                predicates.add(root.get("metricType").in(req.getMetrics()));
                predicates.add(cb.between(root.get("timestamp"), start, end));
                return cb.and(predicates.toArray(new Predicate[0]));
            };

            List<MetricData> data = metricRepo.findAll(spec);

            Map<String, Double> combinedMetrics = new HashMap<>();

            // Calculate the statistics for each metric type
            for (String metric : req.getMetrics()) {
                List<MetricData> filtered = data.stream()
                        .filter(d -> d.getMetricType().equals(metric))
                        .toList();

                double value = switch (req.getStatistic().toLowerCase()) {
                    case "average" ->
                        filtered.stream().mapToDouble(MetricData::getValue).average().orElse(0.0);
                    case "sum" ->
                        filtered.stream().mapToDouble(MetricData::getValue).sum();
                    case "min" ->
                        filtered.stream().mapToDouble(MetricData::getValue).min().orElse(0.0);
                    case "max" ->
                        filtered.stream().mapToDouble(MetricData::getValue).max().orElse(0.0);
                    default ->
                        throw new IllegalArgumentException("Unsupported statistic: " + req.getStatistic());
                };

                combinedMetrics.put(metric, value);
            }

            return new MetricStatDTO(req.getSensorIds(), combinedMetrics, req.getStatistic(), req.getPeriod());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid input: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while querying metrics: " + e.getMessage(), e);
        }
    }

}
