package com.pollu.demo.repositories;

import com.pollu.demo.entities.AlertHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {
    List<AlertHistory> findByUserIdOrderByTimestampDesc(Long userId);
}