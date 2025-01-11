package org.example.backend.controller;

import org.example.backend.model.SensorData;
import org.example.backend.service.WaterManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensor")
public class SensorController {
    @Autowired
    private WaterManagementService waterManagementService;

    @PostMapping("/data")
    public ResponseEntity<?> addSensorData(@RequestBody SensorData data) {
        return ResponseEntity.ok(waterManagementService.saveSensorData(data));
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentData() {
        return ResponseEntity.ok(waterManagementService.getCurrentSensorData());
    }
} 