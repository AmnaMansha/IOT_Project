package org.example.backend.repository;

import org.example.backend.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface AlertRepository extends JpaRepository<Alert, Long> {
    boolean existsByTypeAndSeverityAndMessageAndIsResolvedFalse(String type, String severity, String message);

    // Find all unresolved alerts ordered by timestamp in descending order
  List<Alert> findByIsResolvedFalseOrderByTimestampDesc();
    Optional<Alert> findByTypeAndSeverityAndMessageAndIsResolvedFalse(String type, String severity, String message);
    Optional<Alert> findByTypeAndIsResolvedFalse(String type);
}
