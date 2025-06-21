package com.hakikishabets.bets.repository;

import com.hakikishabets.bets.model.MultiBet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MultiBetRepository extends JpaRepository<MultiBet, Long> {
    List<MultiBet> findByUserId(Long userId);
}
