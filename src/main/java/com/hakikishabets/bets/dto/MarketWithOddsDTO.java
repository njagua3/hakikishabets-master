package com.hakikishabets.bets.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MarketWithOddsDTO {
    // Getters and setters
    private Long marketId;
    private String marketName;
    private List<OddResponseDTO> odds;

}
