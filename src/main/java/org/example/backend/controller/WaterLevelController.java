package org.example.backend.controller;

import org.example.backend.model.WaterLevel;
import org.example.backend.service.WaterLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/waterlevel")
public class WaterLevelController {
    @Autowired
    private WaterLevelService waterLevelService;

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentWaterLevel() {
        return ResponseEntity.ok(waterLevelService.getCurrentWaterLevel());
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentReadings() {
        return ResponseEntity.ok(waterLevelService.getAllWaterLevels());
    }

    @PostMapping
    public ResponseEntity<?> addReading(@RequestParam Double level) {
        return ResponseEntity.ok(waterLevelService.updateWaterLevel(level));
    }

    @GetMapping("/between")
    public ResponseEntity<?> getReadingsBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(waterLevelService.getReadingsBetween(start, end));
    }
}
