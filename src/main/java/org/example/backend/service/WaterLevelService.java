package org.example.backend.service;

import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.model.WaterLevel;
import org.example.backend.repository.WaterLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WaterLevelService {
    @Autowired
    private WaterLevelRepository waterLevelRepository;

    public WaterLevel save(WaterLevel waterLevel) {
        return waterLevelRepository.save(waterLevel);
    }

    public List<WaterLevel> getRecentReadings() {
        return waterLevelRepository.findTop10ByOrderByTimestampDesc();
    }

    public List<WaterLevel> getReadingsBetween(LocalDateTime start, LocalDateTime end) {
        return waterLevelRepository.findByTimestampBetween(start, end);
    }

    public WaterLevel getCurrentWaterLevel() {
        return waterLevelRepository.findTopByOrderByTimestampDesc()
            .orElseThrow(() -> new ResourceNotFoundException("WaterLevel", "current", ""));
    }

    public List<WaterLevel> getAllWaterLevels() {
        return waterLevelRepository.findAll();
    }

    public WaterLevel updateWaterLevel(Double level) {
        WaterLevel waterLevel = new WaterLevel();
        waterLevel.setLevel(level);
        waterLevel.setStatus(determineStatus(level));
        waterLevel.setTimestamp(LocalDateTime.now());
        return waterLevelRepository.save(waterLevel);
    }

    private String determineStatus(Double level) {
        if (level > 80.0) return "HIGH";
        if (level < 20.0) return "LOW";
        return "NORMAL";
    }
} 