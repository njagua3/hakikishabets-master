package com.hakikishabets.bets.repository;

import com.hakikishabets.bets.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByStatus(String upcoming);
}
