package org.example.backend.service;

import org.example.backend.model.Alert;
import org.example.backend.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {
    @Autowired
    private AlertRepository alertRepository;

    public Alert createAlert(Alert alert) {
        alert.setTimestamp(LocalDateTime.now());
        alert.setIsResolved(false);
        return alertRepository.save(alert);
    }

    public List<Alert> getActiveAlerts() {
        return alertRepository.findByIsResolvedFalseOrderByTimestampDesc();
    }
} 