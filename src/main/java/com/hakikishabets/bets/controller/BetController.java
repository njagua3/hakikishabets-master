package com.hakikishabets.bets.controller;

import com.hakikishabets.bets.dto.BetRequestDTO;
import com.hakikishabets.bets.dto.BetResponseDTO;
import com.hakikishabets.bets.mapper.BetMapper;
import com.hakikishabets.bets.model.BetStatus;
import com.hakikishabets.bets.service.BetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bets")
public class BetController {

    private final BetService betService;

    @Autowired
    public BetController(BetService betService) {
        this.betService = betService;
    }

    // Place a new bet
    @PostMapping("/place")
    public ResponseEntity<BetResponseDTO> placeBet(
            @Valid @RequestBody BetRequestDTO betRequestDTO) {

        BetResponseDTO response = betService.placeBet(
                betRequestDTO.getUserId(),
                betRequestDTO.getMatchId(),
                betRequestDTO.getMarketId(),
                betRequestDTO.getOddId(),
                betRequestDTO.getStake()
        );
        return ResponseEntity.ok(response);
    }

    // Get all bets
    @GetMapping("/all")
    public ResponseEntity<List<BetResponseDTO>> getAllBets() {
        List<BetResponseDTO> bets = betService.getAllBets().stream()
                .map(BetMapper::toDTO)  // Replaced lambda with method reference
                .collect(Collectors.toList());
        return ResponseEntity.ok(bets);
    }

    // Get bets for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BetResponseDTO>> getUserBets(@PathVariable Long userId) {
        List<BetResponseDTO> bets = betService.getUserBets(userId).stream()
                .map(BetMapper::toDTO)  // Replaced lambda with method reference
                .collect(Collectors.toList());
        return ResponseEntity.ok(bets);
    }

    // Update the status of a bet (e.g., WON, LOST)
    @PutMapping("/{betId}/status")
    public ResponseEntity<BetResponseDTO> updateBetStatus(
            @PathVariable Long betId,
            @RequestParam BetStatus status
    ) {
        BetResponseDTO response = betService.updateBetStatus(betId, status);
        return ResponseEntity.ok(response);
    }
}
