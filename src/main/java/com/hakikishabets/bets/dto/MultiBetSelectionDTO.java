package com.hakikishabets.bets.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MultiBetSelectionDTO {

    @NotNull(message = "Game ID is required")
    private Long gameId;

    @NotNull(message = "Market ID is required")
    private Long marketId;

    @NotNull(message = "Odd ID is required")
    private Long oddId;
}
