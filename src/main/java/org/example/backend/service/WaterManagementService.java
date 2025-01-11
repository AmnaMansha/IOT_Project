package org.example.backend.service;

import org.example.backend.Threshold.AlertThresholds;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.model.*;
import org.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class WaterManagementService {
    // Set a predefined threshold for water leakage detection (e.g., 75%)
    private static final double LEAKAGE_THRESHOLD = 75.0;
    @Autowired
    private WaterLevelRepository waterLevelRepository;

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Autowired
    private PumpStatusRepository pumpStatusRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private HumidityRepository humidityRepository;

    @Autowired
    private UserRepository userRepository;

    // Water Level CRUD
    public List<WaterLevel> getAllWaterLevels() {
        return waterLevelRepository.findAll();
    }

    public WaterLevel saveWaterLevel(WaterLevel waterLevel) {
        checkConditionsAndCreateAlerts();
        return waterLevelRepository.save(waterLevel);
    }

    public WaterLevel getWaterLevelById(Long id) {
        return waterLevelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WaterLevel not found"));
    }

    public WaterLevel updateWaterLevel(Long id, WaterLevel waterLevel) {
        WaterLevel existingWaterLevel = getWaterLevelById(id);
        existingWaterLevel.setLevel(waterLevel.getLevel());
        return waterLevelRepository.save(existingWaterLevel);
    }

    public void deleteWaterLevel(Long id) {
        WaterLevel existingWaterLevel = getWaterLevelById(id);
        waterLevelRepository.delete(existingWaterLevel);
    }

    // Temperature CRUD
    public List<Temperature> getAllTemperatures() {
        return temperatureRepository.findAll();
    }

    public Temperature saveTemperature(Temperature temperature) {
        checkConditionsAndCreateAlerts();
        temperature.setTimestamp(LocalDateTime.now());
        return temperatureRepository.save(temperature);
    }

    public Temperature getTemperatureById(Long id) {
        return temperatureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Temperature not found"));
    }

    public Temperature updateTemperature(Long id, Temperature temperature) {
        Temperature existingTemperature = getTemperatureById(id);
        existingTemperature.setTemperature(temperature.getTemperature());
        existingTemperature.setTimestamp(LocalDateTime.now());
        return temperatureRepository.save(existingTemperature);
    }

    public boolean deleteTemperature(Long id) {
        Temperature existingTemperature = getTemperatureById(id);
        temperatureRepository.delete(existingTemperature);
        return true;
    }

    // Humidity CRUD
    public List<Humidity> getAllHumidityLevels() {
        return humidityRepository.findAll();
    }

    public Humidity saveHumidity(Humidity humidity) {
        checkConditionsAndCreateAlerts();
        humidity.setTimestamp(LocalDateTime.now());
        return humidityRepository.save(humidity);
    }

    public Humidity getHumidityById(Long id) {
        return humidityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Humidity not found"));
    }

    public Humidity updateHumidity(Long id, Humidity humidity) {
        Humidity existingHumidity = getHumidityById(id);
        existingHumidity.setHumidity(humidity.getHumidity());
        existingHumidity.setTimestamp(LocalDateTime.now());
        return humidityRepository.save(existingHumidity);
    }

    public void deleteHumidity(Long id) {
        Humidity existingHumidity = getHumidityById(id);
        humidityRepository.delete(existingHumidity);
    }

    // Alert CRUD
    public List<Alert> getAllAlerts() {

        return alertRepository.findAll();
    }
    public List<Alert> getActiveAlerts() {
        checkConditionsAndCreateAlerts();
        return alertRepository.findByIsResolvedFalseOrderByTimestampDesc();
    }
    public Alert saveAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    public Alert getAlertById(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));
    }

    public Alert updateAlert(Long id, Alert alert) {
        Alert existingAlert = getAlertById(id);
        existingAlert.setMessage(alert.getMessage());
        return alertRepository.save(existingAlert);
    }

    public boolean deleteAlert(Long id) {
        Alert existingAlert = getAlertById(id);
        alertRepository.delete(existingAlert);
        return true;
    }

    // Water Level Management
    public WaterLevel updateWaterLevel(Double level) {
        WaterLevel waterLevel = new WaterLevel();
        waterLevel.setLevel(level);
        waterLevel.setStatus(determineWaterStatus(level));

        // Create alert if necessary
        if (!waterLevel.getStatus().equals("NORMAL")) {
            createAlert("WATER_LEVEL",
                    waterLevel.getStatus().equals("HIGH") ? "CRITICAL" : "WARNING",
                    "Water level is " + waterLevel.getStatus());
        }

        return waterLevelRepository.save(waterLevel);
    }

    // Sensor Data Management
    public SensorData updateSensorData(Double temperature, Double humidity) {
        SensorData data = new SensorData();
        data.setTemperature(temperature);
        data.setHumidity(humidity);

        // Check thresholds and create alerts
        checkTemperatureThresholds(temperature);
        checkHumidityThresholds(humidity);

        return sensorDataRepository.save(data);
    }

    // Pump Management
    public PumpStatus updatePumpStatus(Boolean status, String username) {
        PumpStatus pumpStatus = new PumpStatus();
        pumpStatus.setIsRunning(status);
        pumpStatus.setChangedBy(username);
        pumpStatus.setLastStatusChange(LocalDateTime.now());
        return pumpStatusRepository.save(pumpStatus);
    }

    // Alert Management
    public Alert createAlert(String type, String severity, String message) {
        // Check if an identical alert already exists
        boolean alertExists = alertRepository.existsByTypeAndSeverityAndMessageAndIsResolvedFalse(type, severity, message);

        // If the alert does not exist, create and save it
        if (!alertExists) {
            Alert alert = new Alert();
            alert.setType(type);
            alert.setSeverity(severity);
            alert.setMessage(message);
            alert.setIsResolved(false);
            alert.setTimestamp(LocalDateTime.now());
            return alertRepository.save(alert);
        }

        // If an identical alert exists, return null or log a message
        return null;
    }

    private String determineWaterStatus(Double level) {
        if (level > 80.0) return "HIGH";
        if (level < 20.0) return "LOW";
        return "NORMAL";
    }

    private void checkTemperatureThresholds(Double temperature) {
        if (temperature > 30.0) {
            createAlert("TEMPERATURE", "WARNING", "High temperature detected: " + temperature);
        }
    }

    private void checkHumidityThresholds(Double humidity) {
        if (humidity > 80.0) {
            createAlert("HUMIDITY", "WARNING", "High humidity detected: " + humidity);
        }
    }

    public SensorData getCurrentSensorData() {
        return sensorDataRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new ResourceNotFoundException("SensorData not found"));
    }

    public WaterLevel getCurrentWaterLevel() {
        return waterLevelRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new ResourceNotFoundException("Current WaterLevel not found"));
    }

    public Temperature getCurrentTemperature() {
        return temperatureRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new ResourceNotFoundException("Current Temperature not found"));
    }

    public Humidity getCurrentHumidity() {
        return humidityRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new ResourceNotFoundException("Current Humidity not found"));
    }

    public PumpStatus getCurrentPumpStatus() {
        return pumpStatusRepository.findTopByOrderByLastStatusChangeDesc()
                .orElseThrow(() -> new ResourceNotFoundException("PumpStatus not found"));
    }

    public SensorData saveSensorData(SensorData data) {
        data.setTimestamp(LocalDateTime.now());
        return sensorDataRepository.save(data);
    }
    public String checkForWaterLeakage() {
        // Get the current humidity
        Humidity currentHumidity = getCurrentHumidity();

        // If no humidity record is found, return an error message
        if (currentHumidity == null) {
            return "No current humidity data available.";
        }

        // Compare the humidity level with the threshold
        if (currentHumidity.getHumidity() > LEAKAGE_THRESHOLD) {
            return "Warning: Possible water leakage detected!";
        } else {
            return "Humidity is normal, no leakage detected.";
        }
    }

    private Alert findExistingUnresolvedAlert(String type) {
        return alertRepository.findByTypeAndIsResolvedFalse(type)
                .orElse(null);
    }

    private void updateOrCreateAlert(String type, String severity, String message) {
        Alert existingAlert = findExistingUnresolvedAlert(type);

        if (existingAlert != null) {
            // Update existing alert if message is different
            if (!existingAlert.getMessage().equals(message)) {
                existingAlert.setMessage(message);
                existingAlert.setSeverity(severity);
                existingAlert.setTimestamp(LocalDateTime.now());
                alertRepository.save(existingAlert);
            }
        } else {
            // Create new alert only if one doesn't exist
            createAlert(type, severity, message);
        }
    }

    private void resolveExistingAlert(String type) {
        Alert existingAlert = findExistingUnresolvedAlert(type);
        if (existingAlert != null) {
            existingAlert.setIsResolved(true);
            alertRepository.save(existingAlert);
        }
    }

    private void checkConditionsAndCreateAlerts() {
        try {
            // Water level check
            WaterLevel currentWaterLevel = getCurrentWaterLevel();
            if (currentWaterLevel.getLevel() > AlertThresholds.HIGH_WATER_LEVEL) {
                updateOrCreateAlert("WATER_LEVEL", "HIGH",
                        "Water level above threshold: " + currentWaterLevel.getLevel() + "%");
            } else if (currentWaterLevel.getLevel() < AlertThresholds.LOW_WATER_LEVEL) {
                updateOrCreateAlert("WATER_LEVEL", "LOW",
                        "Water level below threshold: " + currentWaterLevel.getLevel() + "%");
            } else {
                resolveExistingAlert("WATER_LEVEL");
            }

            // Temperature check
            Temperature currentTemp = getCurrentTemperature();
            if (currentTemp.getTemperature() > AlertThresholds.HIGH_TEMPERATURE) {
                updateOrCreateAlert("TEMPERATURE", "HIGH",
                        "Temperature above threshold: " + currentTemp.getTemperature() + "°C");
            } else {
                resolveExistingAlert("TEMPERATURE");
            }

            // Leakage check
            String leakageStatus = checkForWaterLeakage();
            if (leakageStatus.contains("Warning")) {
                updateOrCreateAlert("LEAKAGE", "WARNING", "Possible water leakage detected");
            } else {
                resolveExistingAlert("LEAKAGE");
            }
        } catch (ResourceNotFoundException e) {
            // Log error but continue
        }
    }

    // Update your repository to include this method


}
