package org.example.backend.controller;
import org.example.backend.model.Temperature;
import org.example.backend.service.WaterManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/temperature")
public class TemperatureController {

    @Autowired
    private WaterManagementService waterManagementService;

    @GetMapping("/")
    public List<Temperature> getAllTemperatures() {
        return waterManagementService.getAllTemperatures();
    }

    @PostMapping("/")
    public Temperature createTemperature(@RequestBody Temperature temperature) {
        return waterManagementService.saveTemperature(temperature);
    }

    @GetMapping("/{id}")
    public Temperature getTemperatureById(@PathVariable Long id) {
        return waterManagementService.getTemperatureById(id);
    }

    @PutMapping("/{id}")
    public Temperature updateTemperature(@PathVariable Long id, @RequestBody Temperature temperature) {
        return waterManagementService.updateTemperature(id, temperature);
    }

    @DeleteMapping("/{id}")
    public void deleteTemperature(@PathVariable Long id) {
        waterManagementService.deleteTemperature(id);
    }

    @PostMapping
    public ResponseEntity<?> addTemperature(@RequestParam Double value) {
        Temperature temp = new Temperature();
        temp.setTemperature(value);
        return ResponseEntity.ok(waterManagementService.saveTemperature(temp));
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentTemperature() {
        return ResponseEntity.ok(waterManagementService.getCurrentTemperature());
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentTemperatures() {
        return ResponseEntity.ok(waterManagementService.getAllTemperatures());
    }
}
