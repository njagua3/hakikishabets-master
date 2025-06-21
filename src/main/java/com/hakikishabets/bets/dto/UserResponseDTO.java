package com.hakikishabets.bets.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserResponseDTO {
    private Long id;
    private String phoneNumber;
    private BigDecimal balance;
}
