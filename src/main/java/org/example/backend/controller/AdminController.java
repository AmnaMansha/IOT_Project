package org.example.backend.controller;

import org.example.backend.service.WaterManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @Autowired
    private WaterManagementService waterManagementService;

    @GetMapping("/system/status")
    public ResponseEntity<?> getSystemStatus() {
        // Return comprehensive system status
        Map<String, Object> status = new HashMap<>();
        // Add system metrics
        return ResponseEntity.ok(status);
    }

    @PostMapping("/pump/control")
    public ResponseEntity<?> controlPump(@RequestParam Boolean status) {
        return ResponseEntity.ok(waterManagementService.updatePumpStatus(status, 
            SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @GetMapping("/alerts/all")
    public ResponseEntity<?> getAllAlerts() {
        return ResponseEntity.ok(waterManagementService.getAllAlerts());
    }
} 