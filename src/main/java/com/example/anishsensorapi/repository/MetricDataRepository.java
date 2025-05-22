package com.example.anishsensorapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.anishsensorapi.entity.MetricData;

public interface MetricDataRepository extends JpaRepository<MetricData, Long>,
        JpaSpecificationExecutor<MetricData> {
}
