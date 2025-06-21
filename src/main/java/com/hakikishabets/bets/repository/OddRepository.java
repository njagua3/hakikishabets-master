package com.hakikishabets.bets.repository;

import com.hakikishabets.bets.model.Odd;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OddRepository extends JpaRepository<Odd, Long> {
}
