package com.hakikishabets.bets.dto;

import com.hakikishabets.bets.model.BetStatus;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class BetResponseDTO {
    private Long id;

    @NotNull(message = "Stake must be > zero")
    @Positive
    private BigDecimal stake;

    private BigDecimal potentialWin;
    private BetStatus status;
    private LocalDateTime placedAt;

    private Long userId;
    private Long matchId;
    private Long marketId;
    private Long oddId;
}
