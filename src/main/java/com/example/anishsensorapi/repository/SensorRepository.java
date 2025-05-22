package com.example.anishsensorapi.repository;

import com.example.anishsensorapi.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, String> {}
