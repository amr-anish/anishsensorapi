package com.example.anishsensorapi.dto;

import java.time.LocalDateTime;
import java.util.Map;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UploadRequest {

    @NotNull
    private String sensorId;
    @NotNull
    private Map<String, Double> metrics;
    @NotNull
    private LocalDateTime timestamp;
}
