package org.example.backend.service;
import org.example.backend.exception.ResourceNotFoundException;
import  org.example.backend.model.*;
import org.example.backend.repository.TemperatureRepository;
import org.example.backend.repository.HumidityRepository;
import  org.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.backend.repository.UserRepository;
import java.util.List;
import java.time.LocalDateTime;

@Service
@Transactional
public class WaterManagementService {

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
        return waterLevelRepository.save(waterLevel);
    }

    public WaterLevel getWaterLevelById(Long id) {
        return waterLevelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("WaterLevel", "id", id));
    }

    public WaterLevel updateWaterLevel(Long id, WaterLevel waterLevel) {
        WaterLevel existingWaterLevel = getWaterLevelById(id);
        existingWaterLevel.setLevel(waterLevel.getLevel());
//        existingWaterLevel.setLocation(waterLevel.getLocation());
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
        return temperatureRepository.save(temperature);
    }

    public Temperature getTemperatureById(Long id) {
        return temperatureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Temperature", "id", id));
    }

    public Temperature updateTemperature(Long id, Temperature temperature) {
        Temperature existingTemperature = getTemperatureById(id);
        existingTemperature.setTemperature(temperature.getTemperature());
//        existingTemperature.setSensorLocation(temperature.getSensorLocation());
        return temperatureRepository.save(existingTemperature);
    }

    public void deleteTemperature(Long id) {
        Temperature existingTemperature = getTemperatureById(id);
        temperatureRepository.delete(existingTemperature);
    }

    // Humidity CRUD
    public List<Humidity> getAllHumidityLevels() {
        return humidityRepository.findAll();
    }

    public Humidity saveHumidity(Humidity humidity) {
        return humidityRepository.save(humidity);
    }

    public Humidity getHumidityById(Long id) {
        return humidityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Humidity", "id", id));
    }

    public Humidity updateHumidity(Long id, Humidity humidity) {
        Humidity existingHumidity = getHumidityById(id);
        existingHumidity.setHumidity(humidity.getHumidity());
//        existingHumidity.setSensorLocation(humidity.getSensorLocation());
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

    public Alert saveAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    public Alert getAlertById(Long id) {
        return alertRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Alert", "id", id));
    }

    public Alert updateAlert(Long id, Alert alert) {
        Alert existingAlert = getAlertById(id);
        existingAlert.setMessage(alert.getMessage());
        return alertRepository.save(existingAlert);
    }

    public void deleteAlert(Long id) {
        Alert existingAlert = getAlertById(id);
        alertRepository.delete(existingAlert);
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
        Alert alert = new Alert();
        alert.setType(type);
        alert.setSeverity(severity);
        alert.setMessage(message);
        alert.setIsResolved(false);
        return alertRepository.save(alert);
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
            .orElseThrow(() -> new ResourceNotFoundException("SensorData", "current", ""));
    }

    public List<Alert> getActiveAlerts() {
        return alertRepository.findByIsResolvedFalseOrderByTimestampDesc();
    }

    public WaterLevel getCurrentWaterLevel() {
        return waterLevelRepository.findTopByOrderByTimestampDesc()
            .orElseThrow(() -> new ResourceNotFoundException("WaterLevel", "current", ""));
    }

    public Temperature getCurrentTemperature() {
        return temperatureRepository.findTopByOrderByTimestampDesc()
            .orElseThrow(() -> new ResourceNotFoundException("Temperature", "current", ""));
    }

    public Humidity getCurrentHumidity() {
        return humidityRepository.findTopByOrderByTimestampDesc()
            .orElseThrow(() -> new ResourceNotFoundException("Humidity", "current", ""));
    }

    public PumpStatus getCurrentPumpStatus() {
        return pumpStatusRepository.findTopByOrderByLastStatusChangeDesc()
            .orElseThrow(() -> new ResourceNotFoundException("PumpStatus", "current", ""));
    }

    public SensorData saveSensorData(SensorData data) {
        data.setTimestamp(LocalDateTime.now());
        return sensorDataRepository.save(data);
    }
}

