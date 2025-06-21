package com.hakikishabets.bets.mapper;

import com.hakikishabets.bets.dto.BetResponseDTO;
import com.hakikishabets.bets.model.Bet;

public class BetMapper {
    public static BetResponseDTO toDTO(Bet bet) {
        BetResponseDTO dto = new BetResponseDTO();
        dto.setId(bet.getId());
        dto.setStake(bet.getStake());
        dto.setPotentialWin(bet.getPotentialWin());
        dto.setStatus(bet.getStatus());
        dto.setPlacedAt(bet.getPlacedAt());
        dto.setUserId(bet.getUser().getId());
        dto.setMatchId(bet.getGame().getId());
        dto.setMarketId(bet.getMarket().getId());
        dto.setOddId(bet.getOdd().getId());
        return dto;
    }
}
