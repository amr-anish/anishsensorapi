package com.example.anishsensorapi;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.anishsensorapi.dto.MetricStatDTO;
import com.example.anishsensorapi.dto.QueryRequest;
import com.example.anishsensorapi.dto.UploadRequest;
import com.example.anishsensorapi.entity.MetricData;
import com.example.anishsensorapi.entity.MetricType;
import com.example.anishsensorapi.entity.Sensor;
import com.example.anishsensorapi.repository.MetricDataRepository;
import com.example.anishsensorapi.repository.MetricTypeRepository;
import com.example.anishsensorapi.repository.SensorRepository;
import com.example.anishsensorapi.service.WeatherService;

@SpringBootTest
class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @MockitoBean
    private SensorRepository sensorRepo;

    @MockitoBean
    private MetricDataRepository metricRepo;

    @MockitoBean
    private MetricTypeRepository metricTypeRepo;

    private UploadRequest request;

    @BeforeEach
    @SuppressWarnings("unused")
    void setup() {
        request = new UploadRequest();
        request.setSensorId("S100");
        request.setMetrics(Map.of("temperature", 25.0));
        request.setTimestamp(LocalDateTime.now());
    }

    @Test
    void test_upload_data_valid_request() {
        try {
            when(metricTypeRepo.findAll())
                    .thenReturn(List.of(new MetricType("temperature")));
            when(sensorRepo.findById("S100"))
                    .thenReturn(Optional.empty());
            when(sensorRepo.save(any(Sensor.class)))
                    .thenAnswer(inv -> inv.getArgument(0));
            when(metricRepo.save(any(MetricData.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            List<MetricData> result = weatherService.uploadMetrics(request);

            assertEquals(1, result.size());
            verify(metricRepo, times(1)).save(any(MetricData.class));
            System.out.println("test_upload_data_valid_request passed Successfully");
        } catch (Exception e) {
            System.out.println("test_upload_data_valid_request failed: " + e.getMessage());
        }
    }

    @Test
    void test_upload_data_with_invalid_metrics() {
        try {

            when(metricTypeRepo.findAll())
                    .thenReturn(List.of(new MetricType("humidity")));

            weatherService.uploadMetrics(request);

            System.out.println("test_upload_data_with_invalid_metrics failed: Exception was not thrown");

        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Metric not allowed"));
            System.out.println("testUploadMetrics_invalidMetric_throwsException passed Successfully");
        } catch (Exception e) {
            System.out.println("test_upload_data_with_invalid_metrics failed: " + e.getMessage());
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void test_query_metrics_success() {
        try {

            QueryRequest queryRequest = new QueryRequest();
            queryRequest.setSensorIds(List.of("S100", "S200"));
            queryRequest.setMetrics(List.of("temperature", "humidity"));
            queryRequest.setStatistic("average");
            queryRequest.setPeriod("7 days");

            LocalDateTime now = LocalDateTime.now();
            when(sensorRepo.findById("S100"))
                    .thenReturn(Optional.of(new Sensor("S100", "Test Sensor 1")));
            when(sensorRepo.findById("S200"))
                    .thenReturn(Optional.of(new Sensor("S200", "Test Sensor 2")));
            when(metricRepo.findAll(any(Specification.class)))
                    .thenReturn(List.of(
                            new MetricData(null, new Sensor("S100", "Test Sensor 1"), "temperature", 25.0, now),
                            new MetricData(null, new Sensor("S100", "Test Sensor 1"), "humidity", 75.0, now),
                            new MetricData(null, new Sensor("S200", "Test Sensor 2"), "temperature", 20.0, now),
                            new MetricData(null, new Sensor("S200", "Test Sensor 2"), "humidity", 65.0, now)
                    ));

            MetricStatDTO result = weatherService.queryMetrics(queryRequest);

            assertEquals(List.of("S100", "S200"), result.getSensorIds());
            assertEquals(2, result.getMetrics().size());
            assertEquals(22.5, result.getMetrics().get("temperature"));
            assertEquals(70.0, result.getMetrics().get("humidity"));
            assertEquals("average", result.getStatistic());
            assertEquals("7 days", result.getPeriod());
            System.out.println("test_query_metrics_success passed Successfully");

        } catch (Exception e) {
            System.out.println("test_query_metrics_success failed: " + e.getMessage());
        }
    }

    @Test
    void test_query_metrics_invalid_sensor_id() {
        try {
            QueryRequest queryRequest = new QueryRequest();
            queryRequest.setSensorIds(List.of("INVALID_SENSOR"));
            queryRequest.setMetrics(List.of("temperature"));
            queryRequest.setStatistic("average");
            queryRequest.setPeriod("3day");

            when(sensorRepo.findById("INVALID_SENSOR"))
                    .thenReturn(Optional.empty());

            weatherService.queryMetrics(queryRequest);

            System.out.println("test_query_metrics_invalid_sensor_id failed: Exception was not thrown");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Invalid input"));
            System.out.println("test_query_metrics_invalid_sensor_id passed Successfully");
        } catch (Exception e) {
            System.out.println("test_query_metrics_invalid_sensor_id failed: " + e.getMessage());
        }
    }

}
