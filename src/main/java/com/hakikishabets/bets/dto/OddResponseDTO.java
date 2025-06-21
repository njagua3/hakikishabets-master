package com.hakikishabets.bets.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OddResponseDTO {
    private Long id;
    private String selection;
    private Double value;
}
