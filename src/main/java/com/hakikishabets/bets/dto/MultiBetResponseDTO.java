package com.hakikishabets.bets.dto;

import com.hakikishabets.bets.model.BetStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MultiBetResponseDTO {
    private Long multiBetId;
    private Long userId;
    private double stake;
    private double potentialWin;
    private BetStatus status;
    private LocalDateTime placedAt;

    private List<MultiBetSelectionDTO> selections; // ✅ Correct DTO type
}
