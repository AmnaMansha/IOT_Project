package org.example.backend.controller;

import org.example.backend.service.WaterManagementService;
import org.example.backend.model.PumpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pump")
public class PumpController {
    @Autowired
    private WaterManagementService waterManagementService;

    @PostMapping("/control")
    public ResponseEntity<?> controlPump(@RequestParam Boolean status, @RequestParam String username) {
        return ResponseEntity.ok(waterManagementService.updatePumpStatus(status, username));
    }

    @GetMapping("/status")
    public ResponseEntity<?> getCurrentStatus() {
        return ResponseEntity.ok(waterManagementService.getCurrentPumpStatus());
    }
} 