package com.hakikishabets.bets.controller;

import com.hakikishabets.bets.dto.MarketResponseDTO;
import com.hakikishabets.bets.dto.MarketWithOddsDTO;
import com.hakikishabets.bets.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/markets")
public class MarketController {

    private final MarketService marketService;

    @Autowired
    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    // ✅ Get all default markets named "1X2"
    @GetMapping("/default")
    public ResponseEntity<List<MarketResponseDTO>> getDefaultMarkets() {
        List<MarketResponseDTO> markets = marketService.getAllDefaultMarketsByName("1X2");
        return ResponseEntity.ok(markets);
    }

    // ✅ Get all markets with their odds for a given match
    @GetMapping("/match/{matchId}/with-odds")
    public ResponseEntity<List<MarketWithOddsDTO>> getMarketsWithOdds(@PathVariable Long matchId) {
        List<MarketWithOddsDTO> marketsWithOdds = marketService.getMarketsWithOddsForMatch(matchId);
        return ResponseEntity.ok(marketsWithOdds);
    }

    // ✅ Get all markets (basic info) for a match
    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<MarketResponseDTO>> getMarketsForMatch(@PathVariable Long matchId) {
        List<MarketResponseDTO> markets = marketService.getMarketsForMatch(matchId);
        return ResponseEntity.ok(markets);
    }

    // ✅ Get market details by ID
    @GetMapping("/{id}")
    public ResponseEntity<MarketResponseDTO> getMarketById(@PathVariable Long id) {
        MarketResponseDTO market = marketService.getMarketDTOById(id);
        return ResponseEntity.ok(market);
    }
}
