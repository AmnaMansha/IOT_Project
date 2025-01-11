package org.example.backend.controller;

import org.example.backend.dto.HumidityResponse;
import org.example.backend.model.Humidity;
import org.example.backend.service.WaterManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/humidity")
public class HumidityController {

    @Autowired
    private WaterManagementService waterManagementService;
    private static final double LEAKAGE_THRESHOLD = 75.0;

    // Get all humidity levels
    @GetMapping("/")
    public ResponseEntity<HumidityResponse> getAllHumidityLevels() {
        try {
            List<Humidity> humidityLevels = waterManagementService.getAllHumidityLevels();
            if (humidityLevels.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(HumidityResponse.success("No humidity records found", null));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(HumidityResponse.success("Humidity records retrieved successfully", humidityLevels));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HumidityResponse.error("Error retrieving humidity records: " + e.getMessage()));
        }
    }

    // Create a new humidity record
    @PostMapping("/")
    public ResponseEntity<HumidityResponse> createHumidity(@RequestBody Humidity humidity) {
        try {
            if (humidity.getHumidity() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(HumidityResponse.error("Humidity value is required"));
            }
            Humidity savedHumidity = waterManagementService.saveHumidity(humidity);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(HumidityResponse.success("Humidity record created successfully", savedHumidity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HumidityResponse.error("Error creating humidity record: " + e.getMessage()));
        }
    }

    // Get a humidity record by ID
    @GetMapping("/{id}")
    public ResponseEntity<HumidityResponse> getHumidityById(@PathVariable Long id) {
        try {
            Humidity humidity = waterManagementService.getHumidityById(id);
            if (humidity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(HumidityResponse.error("Humidity record not found with id: " + id));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(HumidityResponse.success("Humidity record retrieved successfully", humidity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HumidityResponse.error("Error retrieving humidity record: " + e.getMessage()));
        }
    }

    // Update a humidity record by ID
    @PutMapping("/{id}")
    public ResponseEntity<HumidityResponse> updateHumidity(@PathVariable Long id, @RequestBody Humidity humidity) {
        try {
            if (humidity.getHumidity() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(HumidityResponse.error("Humidity value is required"));
            }
            Humidity updatedHumidity = waterManagementService.updateHumidity(id, humidity);
            if (updatedHumidity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(HumidityResponse.error("Humidity record not found with id: " + id));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(HumidityResponse.success("Humidity record updated successfully", updatedHumidity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HumidityResponse.error("Error updating humidity record: " + e.getMessage()));
        }
    }



    // Add a new humidity record with a value
    @PostMapping
    public ResponseEntity<HumidityResponse> addHumidity(@RequestParam Double value) {
        try {
            if (value == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(HumidityResponse.error("Humidity value is required"));
            }
            Humidity humidity = new Humidity();
            humidity.setHumidity(value);
            Humidity savedHumidity = waterManagementService.saveHumidity(humidity);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(HumidityResponse.success("Humidity record added successfully", savedHumidity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HumidityResponse.error("Error adding humidity record: " + e.getMessage()));
        }
    }

    // Get the current humidity
    @GetMapping("/current")
    public ResponseEntity<HumidityResponse> getCurrentHumidity() {
        try {
            Humidity currentHumidity = waterManagementService.getCurrentHumidity();
            if (currentHumidity == null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(HumidityResponse.success("No current humidity record found", null));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(HumidityResponse.success("Current humidity retrieved successfully", currentHumidity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HumidityResponse.error("Error retrieving current humidity: " + e.getMessage()));
        }
    }

    // Get all recent humidity records
    @GetMapping("/all")
    public ResponseEntity<HumidityResponse> getRecentHumidity() {
        try {
            List<Humidity> recentHumidity = waterManagementService.getAllHumidityLevels();
            if (recentHumidity.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(HumidityResponse.success("No recent humidity records found", null));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(HumidityResponse.success("Recent humidity records retrieved successfully", recentHumidity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HumidityResponse.error("Error retrieving recent humidity records: " + e.getMessage()));
        }
    }

    @GetMapping("/check-leakage")
    public ResponseEntity<HumidityResponse> checkWaterLeakage() {
        try {
            // Call the service method to check for water leakage
            String leakageStatus = waterManagementService.checkForWaterLeakage();

            // Return the response based on the result
            return ResponseEntity.status(HttpStatus.OK)
                    .body(HumidityResponse.success(leakageStatus, null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HumidityResponse.error("Error checking for water leakage: " + e.getMessage()));
        }
    }

}
