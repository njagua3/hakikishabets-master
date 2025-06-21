package com.hakikishabets.bets.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GameResponseDTO {

    private Long id;
    private String gameName;
    private LocalDateTime startTime;
    private String status;
    private List<Market> markets;

    @Getter
    @Setter
    public static class Market {
        private String name;
        private List<Odd> odds;
    }

    @Getter
    @Setter
    public static class Odd {
        private String selection;
        private Double value;
        private boolean active;
    }
}
