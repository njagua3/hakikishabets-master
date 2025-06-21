package com.hakikishabets.bets.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class MultiBetRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @Positive(message = "Stake must be greater than 0")
    private double stake;

    @NotNull(message = "At least one selection is required")
    private List<MultiBetSelectionDTO> selections;
}
