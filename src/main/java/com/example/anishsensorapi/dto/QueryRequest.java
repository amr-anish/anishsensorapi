package com.example.anishsensorapi.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QueryRequest {

    @NotNull
    private List<String> sensorIds;
    @NotNull
    private List<String> metrics;
    @NotNull
    private String statistic;
    private String period;
}
