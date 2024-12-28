package org.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pump_status")
public class PumpStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isRunning;
    
    @Column(name = "changed_by")
    private String changedBy;
    
    @Column(name = "last_status_change")
    private LocalDateTime lastStatusChange;

    @PrePersist
    @PreUpdate
    protected void onCreate() {
        if (lastStatusChange == null) {
            lastStatusChange = LocalDateTime.now();
        }
    }
}
