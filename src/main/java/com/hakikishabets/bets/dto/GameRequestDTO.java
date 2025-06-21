package com.hakikishabets.bets.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GameRequestDTO {

    @NotBlank(message = "Game name is required")
    private String gameName;

    @NotNull(message = "Start time is required")
    @FutureOrPresent(message = "Start time must be now or in the future")
    private LocalDateTime startTime;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Markets list cannot be null")
    @Size(min = 1, message = "At least one market must be provided")
    @Valid
    private List<MarketDTO> markets;

    @Getter
    @Setter
    public static class MarketDTO {

        @NotBlank(message = "Market name is required")
        private String name;

        @NotNull(message = "Odds list cannot be null")
        @Size(min = 1, message = "At least one odd must be provided for each market")
        @Valid
        private List<OddDTO> odds;
    }

    @Getter
    @Setter
    public static class OddDTO {

        @NotBlank(message = "Selection is required")
        private String selection;

        @NotNull(message = "Odd value is required")
        @DecimalMin(value = "1.01", message = "Odd value must be at least 1.01")
        private Double value;

        private boolean active = true;
    }
}
