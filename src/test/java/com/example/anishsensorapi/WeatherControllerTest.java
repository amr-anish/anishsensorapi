package com.example.anishsensorapi;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.anishsensorapi.dto.QueryRequest;
import com.example.anishsensorapi.dto.UploadRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testUploadEndpoint() {
        try {
            UploadRequest uploadRequest = new UploadRequest();
            uploadRequest.setSensorId("S100");
            uploadRequest.setMetrics(Map.of("temperature", 23.5));
            uploadRequest.setTimestamp(LocalDateTime.now());
    
            mockMvc.perform(post("/metrics/upload")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(uploadRequest)))
                    .andExpect(status().isOk());
    
            System.out.println("testUploadEndpoint passed Successfully");
        } catch (Exception e) {
            System.out.println("testUploadEndpoint failed: " + e.getMessage());
        }
    }

    @Test
    void testQueryEndpoint() {
        try {
            String sensorId = "S100";
            String metricType = "temperature";
    
            QueryRequest queryRequest = new QueryRequest();
            queryRequest.setSensorIds(List.of(sensorId));
            queryRequest.setMetrics(List.of(metricType));
            queryRequest.setStatistic("average");
            queryRequest.setPeriod("3day");
    
            mockMvc.perform(post("/metrics/query")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(queryRequest)))
                    .andExpect(status().isOk());
    
            System.out.println("testQueryEndpoint passed Successfully");
        } catch (Exception e) {
            System.out.println("testQueryEndpoint failed: " + e.getMessage());
        }
    }
}
