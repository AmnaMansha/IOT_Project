package org.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "water_levels")
public class WaterLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double level;
    private String status; // NORMAL, LOW, HIGH
    private LocalDateTime timestamp;

    @PrePersist
    @PreUpdate
    public void updateStatus() {
        this.status = determineStatus(this.level);
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    private String determineStatus(Double level) {
        if (level > 80.0) return "HIGH";
        if (level < 20.0) return "LOW";
        return "NORMAL";
    }
}
