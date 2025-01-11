package org.example.backend.controller;

import org.example.backend.dto.WaterLevelResponse;
import org.example.backend.model.WaterLevel;
import org.example.backend.service.WaterLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/waterlevel")
public class WaterLevelController {

    @Autowired
    private WaterLevelService waterLevelService;

    // Get the current water level
    @GetMapping("/current")
    public ResponseEntity<WaterLevelResponse> getCurrentWaterLevel() {
        try {
            WaterLevel currentWaterLevel = waterLevelService.getCurrentWaterLevel();
            if (currentWaterLevel == null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(WaterLevelResponse.success("No current water level record found", null));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(WaterLevelResponse.success("Current water level retrieved successfully", currentWaterLevel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(WaterLevelResponse.error("Error retrieving current water level: " + e.getMessage()));
        }
    }

    // Get all recent water level readings
    @GetMapping("/all")
    public ResponseEntity<WaterLevelResponse> getRecentReadings() {
        try {
            List<WaterLevel> recentReadings = waterLevelService.getAllWaterLevels();
            if (recentReadings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(WaterLevelResponse.success("No recent water level records found", null));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(WaterLevelResponse.success("Recent water level records retrieved successfully", recentReadings));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(WaterLevelResponse.error("Error retrieving recent water level records: " + e.getMessage()));
        }
    }

    // Add a new water level reading
    @PostMapping
    public ResponseEntity<WaterLevelResponse> addReading(@RequestParam Double level) {
        try {
            if (level == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(WaterLevelResponse.error("Water level value is required"));
            }
            WaterLevel updatedWaterLevel = waterLevelService.updateWaterLevel(level);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(WaterLevelResponse.success("Water level record added successfully", updatedWaterLevel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(WaterLevelResponse.error("Error adding water level record: " + e.getMessage()));
        }
    }

    // Get water level readings between two timestamps
    @GetMapping("/between")
    public ResponseEntity<WaterLevelResponse> getReadingsBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        try {
            if (start == null || end == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(WaterLevelResponse.error("Both start and end timestamps are required"));
            }
            if (start.isAfter(end)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(WaterLevelResponse.error("Start timestamp must be before end timestamp"));
            }
            List<WaterLevel> readingsBetween = waterLevelService.getReadingsBetween(start, end);
            if (readingsBetween.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(WaterLevelResponse.success("No water level records found between the specified timestamps", null));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(WaterLevelResponse.success("Water level records retrieved successfully", readingsBetween));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(WaterLevelResponse.error("Error retrieving water level records: " + e.getMessage()));
        }
    }
}
