package org.example.backend.controller;
import org.example.backend.model.Humidity;
import org.example.backend.service.WaterManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/humidity")
public class HumidityController {

    @Autowired
    private WaterManagementService waterManagementService;

    @GetMapping("/")
    public List<Humidity> getAllHumidityLevels() {
        return waterManagementService.getAllHumidityLevels();
    }

    @PostMapping("/")
    public Humidity createHumidity(@RequestBody Humidity humidity) {
        return waterManagementService.saveHumidity(humidity);
    }

    @GetMapping("/{id}")
    public Humidity getHumidityById(@PathVariable Long id) {
        return waterManagementService.getHumidityById(id);
    }

    @PutMapping("/{id}")
    public Humidity updateHumidity(@PathVariable Long id, @RequestBody Humidity humidity) {
        return waterManagementService.updateHumidity(id, humidity);
    }

    @DeleteMapping("/{id}")
    public void deleteHumidity(@PathVariable Long id) {
        waterManagementService.deleteHumidity(id);
    }

    @PostMapping
    public ResponseEntity<?> addHumidity(@RequestParam Double value) {
        Humidity humidity = new Humidity();
        humidity.setHumidity(value);
        return ResponseEntity.ok(waterManagementService.saveHumidity(humidity));
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentHumidity() {
        return ResponseEntity.ok(waterManagementService.getCurrentHumidity());
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentHumidity() {
        return ResponseEntity.ok(waterManagementService.getAllHumidityLevels());
    }
}
