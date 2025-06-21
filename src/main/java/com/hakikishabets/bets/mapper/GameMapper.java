package com.hakikishabets.bets.mapper;

import com.hakikishabets.bets.dto.GameResponseDTO;
import com.hakikishabets.bets.model.Game;
import com.hakikishabets.bets.model.Market;
import com.hakikishabets.bets.model.Odd;

import java.util.List;
import java.util.stream.Collectors;

public class GameMapper {

    // Static utility method to convert Game entity to GameResponseDTO
    public static GameResponseDTO toDTO(Game game) {
        GameResponseDTO dto = new GameResponseDTO();
        dto.setId(game.getId());
        dto.setGameName(game.getGameName());
        dto.setStartTime(game.getStartTime());
        dto.setStatus(game.getStatus());

        // Map Markets
        List<GameResponseDTO.Market> marketDTOs = game.getMarkets().stream()
                .map(GameMapper::mapMarket)
                .collect(Collectors.toList());

        dto.setMarkets(marketDTOs);
        return dto;
    }

    // Helper method to map Market entity to DTO
    private static GameResponseDTO.Market mapMarket(Market market) {
        GameResponseDTO.Market marketDTO = new GameResponseDTO.Market();
        marketDTO.setName(market.getName());

        List<GameResponseDTO.Odd> oddDTOs = market.getOdds().stream()
                .map(GameMapper::mapOdd)
                .collect(Collectors.toList());

        marketDTO.setOdds(oddDTOs);
        return marketDTO;
    }

    // Helper method to map MarketOdd entity to DTO
    private static GameResponseDTO.Odd mapOdd(Odd odd) {
        GameResponseDTO.Odd oddDTO = new GameResponseDTO.Odd();
        oddDTO.setSelection(odd.getSelection());
        oddDTO.setValue(odd.getValue());
        oddDTO.setActive(odd.isActive());
        return oddDTO;
    }
}
