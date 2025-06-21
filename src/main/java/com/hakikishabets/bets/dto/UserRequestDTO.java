package com.hakikishabets.bets.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserRequestDTO {
    private String phoneNumber;
    private String password;
    private BigDecimal balance;
}
