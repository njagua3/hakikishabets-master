package com.hakikishabets.bets.mapper;

import com.hakikishabets.bets.dto.MarketResponseDTO;
import com.hakikishabets.bets.dto.OddResponseDTO;
import com.hakikishabets.bets.model.Market;

import java.util.List;
import java.util.stream.Collectors;

public class MarketMapper {

    public static MarketResponseDTO toDTO(Market market) {
        MarketResponseDTO dto = new MarketResponseDTO();
        dto.setId(market.getId());
        dto.setName(market.getName());

        // Set the game name if available
        if (market.getGame() != null) {
            dto.setGameName(market.getGame().getName());
        }

        List<OddResponseDTO> odds = market.getOdds().stream()
                .map(OddMapper::toDTO)
                .collect(Collectors.toList());

        dto.setOdds(odds);
        return dto;
    }
}
