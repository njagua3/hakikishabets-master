package com.hakikishabets.bets.repository;

import com.hakikishabets.bets.model.Market;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long> {

    // Find all markets by game ID (match)
    List<Market> findByGameId(Long id);

    // Find all markets matching the given name (could be multiple)
    List<Market> findAllByName(String name);

}
