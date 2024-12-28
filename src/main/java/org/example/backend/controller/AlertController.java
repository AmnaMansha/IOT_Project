package org.example.backend.controller;
import org.example.backend.model.Alert;
import org.example.backend.service.WaterManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @Autowired
    private WaterManagementService waterManagementService;

    @GetMapping("/getAlert")
    public List<Alert> getAllAlerts() {
        return waterManagementService.getAllAlerts();
    }

    @PostMapping
    public ResponseEntity<?> createAlert(@RequestBody Alert alert) {
        return ResponseEntity.ok(waterManagementService.saveAlert(alert));
    }

    @GetMapping("/getAlertby_id/{id}")
    public Alert getAlertById(@PathVariable Long id) {
        return waterManagementService.getAlertById(id);
    }

    @PutMapping("/updateAlert/{id}")
    public Alert updateAlert(@PathVariable Long id, @RequestBody Alert alert) {
        return waterManagementService.updateAlert(id, alert);
    }

    @DeleteMapping("/deleteAlert/{id}")
    public void deleteAlert(@PathVariable Long id) {
        waterManagementService.deleteAlert(id);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveAlerts() {
        return ResponseEntity.ok(waterManagementService.getActiveAlerts());
    }
}
