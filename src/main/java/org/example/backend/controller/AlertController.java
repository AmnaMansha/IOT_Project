package org.example.backend.controller;

import org.example.backend.dto.AlertResponse;
import org.example.backend.model.Alert;
import org.example.backend.service.WaterManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @Autowired
    private WaterManagementService waterManagementService;

    // Get all alerts
    @GetMapping("/getAlert")
    public ResponseEntity<AlertResponse> getAllAlerts() {
        try {
            List<Alert> alerts = waterManagementService.getAllAlerts();
            if (alerts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(AlertResponse.success("No alerts found", null));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(AlertResponse.success("Alerts retrieved successfully", alerts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AlertResponse.error("Error retrieving alerts: " + e.getMessage()));
        }
    }

    // Create a new alert
    @PostMapping
    public ResponseEntity<AlertResponse> createAlert(@RequestBody Alert alert) {
        try {
            if (alert.getMessage() == null || alert.getMessage().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(AlertResponse.error("Alert message is required"));
            }
            Alert savedAlert = waterManagementService.saveAlert(alert);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AlertResponse.success("Alert created successfully", savedAlert));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AlertResponse.error("Error creating alert: " + e.getMessage()));
        }
    }

    // Get an alert by ID
    @GetMapping("/getAlertby_id/{id}")
    public ResponseEntity<AlertResponse> getAlertById(@PathVariable Long id) {
        try {
            Alert alert = waterManagementService.getAlertById(id);
            if (alert == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(AlertResponse.error("Alert not found with id: " + id));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(AlertResponse.success("Alert retrieved successfully", alert));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AlertResponse.error("Error retrieving alert: " + e.getMessage()));
        }
    }

    // Update an alert by ID
    @PutMapping("/updateAlert/{id}")
    public ResponseEntity<AlertResponse> updateAlert(@PathVariable Long id, @RequestBody Alert alert) {
        try {
            if (alert.getMessage() == null || alert.getMessage().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(AlertResponse.error("Alert message is required"));
            }
            Alert updatedAlert = waterManagementService.updateAlert(id, alert);
            if (updatedAlert == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(AlertResponse.error("Alert not found with id: " + id));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(AlertResponse.success("Alert updated successfully", updatedAlert));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AlertResponse.error("Error updating alert: " + e.getMessage()));
        }
    }

    // Delete an alert by ID
    @DeleteMapping("/deleteAlert/{id}")
    public ResponseEntity<AlertResponse> deleteAlert(@PathVariable Long id) {
        try {
            boolean isDeleted = waterManagementService.deleteAlert(id);
            if (isDeleted) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(AlertResponse.success("Alert deleted successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(AlertResponse.error("Alert not found with id: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AlertResponse.error("Error deleting alert: " + e.getMessage()));
        }
    }

    // Get active alerts
//    @GetMapping("/active")
//    public ResponseEntity<AlertResponse> getActiveAlerts() {
//        try {
//            List<Alert> activeAlerts = waterManagementService.getActiveAlerts();
//            if (activeAlerts.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.OK)
//                        .body(AlertResponse.success("No active alerts found", null));
//            }
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(AlertResponse.success("Active alerts retrieved successfully", activeAlerts));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(AlertResponse.error("Error retrieving active alerts: " + e.getMessage()));
//        }
//    }
    @GetMapping("/active")
    public ResponseEntity<AlertResponse> getActiveAlerts() {
        try {
            List<Alert> activeAlerts = waterManagementService.getActiveAlerts();
            return activeAlerts.isEmpty() ?
                    ResponseEntity.ok(AlertResponse.success("No active alerts found", null)) :
                    ResponseEntity.ok(AlertResponse.success("Active alerts retrieved successfully", activeAlerts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AlertResponse.error("Error retrieving active alerts: " + e.getMessage()));
        }
    }
}
