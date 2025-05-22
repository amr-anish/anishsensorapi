package com.example.anishsensorapi.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetricStatDTO {
    private List<String> sensorIds; // List of sensor IDs
    private Map<String, Double> metrics; // Combined metrics map
    private String statistic; // Statistic type (e.g., "average", "max", "min")
    private String period; // Time period for the statistic (e.g., "hour", "day", "week")
}