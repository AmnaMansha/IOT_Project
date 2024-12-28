package org.example.backend.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.backend.model.PumpStatus;

@Repository
public interface PumpStatusRepository extends JpaRepository<PumpStatus, Long> {
    Optional<PumpStatus> findTopByOrderByLastStatusChangeDesc();
} 