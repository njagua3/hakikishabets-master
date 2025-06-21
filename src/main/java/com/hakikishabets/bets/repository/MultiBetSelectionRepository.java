package com.hakikishabets.bets.repository;

import com.hakikishabets.bets.model.MultiBetSelection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MultiBetSelectionRepository extends JpaRepository<MultiBetSelection, Long> {
    List<MultiBetSelection> findByMultiBetId(Long multiBetId);
}
