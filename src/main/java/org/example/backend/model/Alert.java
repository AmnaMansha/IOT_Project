package org.example.backend.model;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Data
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // WATER_LEVEL, TEMPERATURE, HUMIDITY
    private String severity; // INFO, WARNING, CRITICAL
    private String message;
    @Column(nullable = false)
    private Boolean isResolved = false;
    private LocalDateTime timestamp;
    private String currentValue;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }
}

