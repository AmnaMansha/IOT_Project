package org.example.backend.controller;

import org.example.backend.dto.TemperatureResponse;
import org.example.backend.model.Temperature;
import org.example.backend.service.WaterManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/temperature")
public class TemperatureController {

    @Autowired
    private WaterManagementService waterManagementService;

    // Get all temperature records
    @GetMapping("/")
    public ResponseEntity<TemperatureResponse> getAllTemperatures() {
        try {
            List<Temperature> temperatures = waterManagementService.getAllTemperatures();
            if (temperatures.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(TemperatureResponse.success("No temperature records found", null));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(TemperatureResponse.success("Temperature records retrieved successfully", temperatures));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TemperatureResponse.error("Error retrieving temperature records: " + e.getMessage()));
        }
    }

    // Create a new temperature record
    @PostMapping("/")
    public ResponseEntity<TemperatureResponse> createTemperature(@RequestBody Temperature temperature) {
        try {
            if (temperature.getTemperature() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(TemperatureResponse.error("Temperature value is required"));
            }
            Temperature savedTemperature = waterManagementService.saveTemperature(temperature);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(TemperatureResponse.success("Temperature record created successfully", savedTemperature));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TemperatureResponse.error("Error creating temperature record: " + e.getMessage()));
        }
    }

    // Get a temperature record by ID
    @GetMapping("/{id}")
    public ResponseEntity<TemperatureResponse> getTemperatureById(@PathVariable Long id) {
        try {
            Temperature temperature = waterManagementService.getTemperatureById(id);
            if (temperature == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(TemperatureResponse.error("Temperature record not found with id: " + id));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(TemperatureResponse.success("Temperature record retrieved successfully", temperature));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TemperatureResponse.error("Error retrieving temperature record: " + e.getMessage()));
        }
    }

    // Update a temperature record by ID
    @PutMapping("/{id}")
    public ResponseEntity<TemperatureResponse> updateTemperature(@PathVariable Long id, @RequestBody Temperature temperature) {
        try {
            if (temperature.getTemperature() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(TemperatureResponse.error("Temperature value is required"));
            }
            Temperature updatedTemperature = waterManagementService.updateTemperature(id, temperature);
            if (updatedTemperature == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(TemperatureResponse.error("Temperature record not found with id: " + id));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(TemperatureResponse.success("Temperature record updated successfully", updatedTemperature));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TemperatureResponse.error("Error updating temperature record: " + e.getMessage()));
        }
    }

    // Delete a temperature record by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<TemperatureResponse> deleteTemperature(@PathVariable Long id) {
        try {
            boolean isDeleted = waterManagementService.deleteTemperature(id);
            if (isDeleted) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(TemperatureResponse.success("Temperature record deleted successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(TemperatureResponse.error("Temperature record not found with id: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TemperatureResponse.error("Error deleting temperature record: " + e.getMessage()));
        }
    }

    // Add a new temperature record with a value
    @PostMapping
    public ResponseEntity<TemperatureResponse> addTemperature(@RequestParam Double value) {
        try {
            if (value == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(TemperatureResponse.error("Temperature value is required"));
            }
            Temperature temp = new Temperature();
            temp.setTemperature(value);
            Temperature savedTemperature = waterManagementService.saveTemperature(temp);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(TemperatureResponse.success("Temperature record added successfully", savedTemperature));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TemperatureResponse.error("Error adding temperature record: " + e.getMessage()));
        }
    }

    // Get the current temperature
    @GetMapping("/current")
    public ResponseEntity<TemperatureResponse> getCurrentTemperature() {
        try {
            Temperature currentTemperature = waterManagementService.getCurrentTemperature();
            if (currentTemperature == null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(TemperatureResponse.success("No current temperature record found", null));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(TemperatureResponse.success("Current temperature retrieved successfully", currentTemperature));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TemperatureResponse.error("Error retrieving current temperature: " + e.getMessage()));
        }
    }

    // Get all recent temperature records
    @GetMapping("/all")
    public ResponseEntity<TemperatureResponse> getRecentTemperatures() {
        try {
            List<Temperature> recentTemperatures = waterManagementService.getAllTemperatures();
            if (recentTemperatures.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(TemperatureResponse.success("No recent temperature records found", null));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(TemperatureResponse.success("Recent temperature records retrieved successfully", recentTemperatures));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TemperatureResponse.error("Error retrieving recent temperature records: " + e.getMessage()));
        }
    }
}
