package com.hakikishabets.bets.mapper;

import com.hakikishabets.bets.dto.OddResponseDTO;
import com.hakikishabets.bets.model.Odd;

public class OddMapper {
    public static OddResponseDTO toDTO(Odd odd) {
        OddResponseDTO dto = new OddResponseDTO();
        dto.setId(odd.getId());
        dto.setSelection(odd.getSelection());
        dto.setValue(odd.getValue());
        return dto;
    }
}
