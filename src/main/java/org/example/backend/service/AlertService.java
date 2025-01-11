package org.example.backend.service;

import org.example.backend.Threshold.AlertThresholds;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.model.Alert;
import org.example.backend.model.Temperature;
import org.example.backend.model.WaterLevel;
import org.example.backend.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {
    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private WaterManagementService waterManagementService;

    public List<Alert> getActiveAlerts() {
        checkConditionsAndCreateAlerts();
        return alertRepository.findByIsResolvedFalseOrderByTimestampDesc();
    }

    private void checkConditionsAndCreateAlerts() {
        try {
            // Check water level
            WaterLevel currentWaterLevel = waterManagementService.getCurrentWaterLevel();
            if (currentWaterLevel.getLevel() > AlertThresholds.HIGH_WATER_LEVEL) {
                createAlert("WATER_LEVEL", "HIGH", "Water level above threshold: " + currentWaterLevel.getLevel() + "%");
            } else if (currentWaterLevel.getLevel() < AlertThresholds.LOW_WATER_LEVEL) {
                createAlert("WATER_LEVEL", "LOW", "Water level below threshold: " + currentWaterLevel.getLevel() + "%");
            }

            // Check temperature
            Temperature currentTemp = waterManagementService.getCurrentTemperature();
            if (currentTemp.getTemperature() > AlertThresholds.HIGH_TEMPERATURE) {
                createAlert("TEMPERATURE", "HIGH", "Temperature above threshold: " + currentTemp.getTemperature() + "°C");
            }

            // Check leakage
            String leakageStatus = waterManagementService.checkForWaterLeakage();
            if (leakageStatus.contains("Warning")) {
                createAlert("LEAKAGE", "WARNING", "Possible water leakage detected");
            }
        } catch (ResourceNotFoundException e) {
            // Log error but don't throw to prevent API failure
        }
    }

    public Alert createAlert(String type, String severity, String message) {
        Alert alert = new Alert();
        alert.setType(type);
        alert.setSeverity(severity);
        alert.setMessage(message);
        alert.setTimestamp(LocalDateTime.now());
        alert.setIsResolved(false);
        return alertRepository.save(alert);
    }
}