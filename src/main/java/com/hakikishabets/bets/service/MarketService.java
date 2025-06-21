package com.hakikishabets.bets.service;

import com.hakikishabets.bets.dto.MarketResponseDTO;
import com.hakikishabets.bets.dto.MarketWithOddsDTO;
import com.hakikishabets.bets.exception.ResourceNotFoundException;
import com.hakikishabets.bets.mapper.MarketMapper;
import com.hakikishabets.bets.mapper.OddMapper;
import com.hakikishabets.bets.model.Market;
import com.hakikishabets.bets.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketService {

    private final MarketRepository marketRepository;

    @Autowired
    public MarketService(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    // Get all markets with their odds for a specific match
    public List<MarketWithOddsDTO> getMarketsWithOddsForMatch(Long matchId) {
        List<Market> markets = marketRepository.findByGameId(matchId);
        return markets.stream()
                .map(market -> {
                    MarketWithOddsDTO dto = new MarketWithOddsDTO();
                    dto.setMarketId(market.getId());
                    dto.setMarketName(market.getName());
                    dto.setOdds(market.getOdds().stream()
                            .map(OddMapper::toDTO)
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Get all markets (basic info) for a match
    public List<MarketResponseDTO> getMarketsForMatch(Long matchId) {
        List<Market> markets = marketRepository.findByGameId(matchId);
        return markets.stream()
                .map(MarketMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get market details by ID
    public MarketResponseDTO getMarketDTOById(Long marketId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new RuntimeException("Market not found"));
        return MarketMapper.toDTO(market);
    }

    public Market getMarketById(Long marketId) {
        return marketRepository.findById(marketId)
                .orElseThrow(() -> new RuntimeException("Market not found"));
    }

    // Return all markets matching the name (e.g. "1X2")
    public List<MarketResponseDTO> getAllDefaultMarketsByName(String name) {
        List<Market> markets = marketRepository.findAllByName(name);
        if (markets.isEmpty()) {
            throw new ResourceNotFoundException("Market '" + name + "' not found");
        }
        return markets.stream()
                .map(MarketMapper::toDTO)
                .collect(Collectors.toList());
    }
}
