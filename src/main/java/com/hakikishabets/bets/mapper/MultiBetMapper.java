package com.hakikishabets.bets.mapper;

import com.hakikishabets.bets.dto.MultiBetRequestDTO;
import com.hakikishabets.bets.dto.MultiBetResponseDTO;
import com.hakikishabets.bets.dto.MultiBetSelectionDTO;
import com.hakikishabets.bets.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MultiBetMapper {

    // Convert from entity to response DTO
    public static MultiBetResponseDTO toResponseDTO(MultiBet multiBet) {
        MultiBetResponseDTO dto = new MultiBetResponseDTO();
        dto.setMultiBetId(multiBet.getId());
        dto.setUserId(multiBet.getUser().getId());

        dto.setStake(multiBet.getStake() != null ? multiBet.getStake().doubleValue() : 0.0);
        dto.setPotentialWin(multiBet.getPotentialWin() != null ? multiBet.getPotentialWin().doubleValue() : 0.0);

        dto.setStatus(multiBet.getStatus());
        dto.setPlacedAt(multiBet.getPlacedAt());

        List<MultiBetSelectionDTO> selectionDTOs = multiBet.getSelections().stream().map(selection -> {
            MultiBetSelectionDTO sDto = new MultiBetSelectionDTO();
            sDto.setGameId(selection.getGame().getId());
            sDto.setMarketId(selection.getMarket().getId());
            sDto.setOddId(selection.getOdd().getId());
            return sDto;
        }).collect(Collectors.toList());

        dto.setSelections(selectionDTOs);
        return dto;
    }

    // Convert from request DTO to entity
    public static MultiBet toEntity(MultiBetRequestDTO request, User user, List<MultiBetSelection> selections) {
        MultiBet entity = new MultiBet();
        entity.setUser(user);

        entity.setStake(BigDecimal.valueOf(request.getStake()));

        entity.setStatus(BetStatus.PENDING);
        entity.setPlacedAt(LocalDateTime.now());

        entity.setSelections(selections);

        // Compute total potential win using BigDecimal arithmetic
        BigDecimal totalOdds = selections.stream()
                .map(s -> BigDecimal.valueOf(s.getOdd().getValue()))
                .reduce(BigDecimal.ONE, BigDecimal::multiply);

        entity.setPotentialWin(BigDecimal.valueOf(request.getStake()).multiply(totalOdds));

        return entity;
    }
}
