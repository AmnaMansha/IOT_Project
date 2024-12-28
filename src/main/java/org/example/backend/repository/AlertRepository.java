package org.example.backend.repository;

import org.example.backend.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByIsResolvedFalseOrderByTimestampDesc();
}
