package com.hakikishabets.bets.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MarketResponseDTO {
    private Long id;
    private String gameName;
    private String name;
            // <-- New field for the game name
    private List<OddResponseDTO> odds;
}
