package org.example.backend.repository;

import org.example.backend.model.WaterLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WaterLevelRepository extends JpaRepository<WaterLevel, Long> {
    List<WaterLevel> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<WaterLevel> findTop10ByOrderByTimestampDesc();
    Optional<WaterLevel> findTopByOrderByTimestampDesc();
}
