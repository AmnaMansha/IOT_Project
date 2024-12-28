package org.example.backend.controller;

import org.example.backend.service.WaterManagementService;
import org.example.backend.model.WaterLevel;
import org.example.backend.model.Temperature;
import org.example.backend.model.Humidity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private WaterManagementService waterManagementService;

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        List<WaterLevel> waterLevels = waterManagementService.getAllWaterLevels();
        List<Temperature> temperatures = waterManagementService.getAllTemperatures();
        List<Humidity> humidityLevels = waterManagementService.getAllHumidityLevels();

        model.addAttribute("waterLevels", waterLevels);
        model.addAttribute("temperatures", temperatures);
        model.addAttribute("humidityLevels", humidityLevels);
        return "admin-dashboard";  // Thymeleaf template for Admin Dashboard
    }

    @GetMapping("/user")
    public String userDashboard(Model model) {
        List<WaterLevel> waterLevels = waterManagementService.getAllWaterLevels();
        model.addAttribute("waterLevels", waterLevels);
        return "user-dashboard";  // Thymeleaf template for User Dashboard
    }
}
