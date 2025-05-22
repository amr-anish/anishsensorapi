package com.example.anishsensorapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.anishsensorapi.entity.MetricType;

public interface MetricTypeRepository extends JpaRepository<MetricType, String> {
}
