package com.hakikishabets.bets.controller;

import com.hakikishabets.bets.dto.OddRequestDTO;
import com.hakikishabets.bets.dto.OddResponseDTO;
import com.hakikishabets.bets.mapper.OddMapper;
import com.hakikishabets.bets.model.Odd;
import com.hakikishabets.bets.service.OddService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling operations related to Odds within Markets.
 */
@RestController
@RequestMapping("/api/markets")
public class OddController {

    private final OddService oddService;

    /**
     * Constructor-based dependency injection for OddService.
     */
    public OddController(OddService oddService) {
        this.oddService = oddService;
    }

    /**
     * Adds a new Odd to a specific market.
     *
     * @param marketId       ID of the market to which the odd is to be added
     * @param oddRequestDTO  Request a body containing details of the odd to add
     * @return               ResponseEntity containing the created odd as a DTO
     */
    @PostMapping("/{marketId}/odds")
    public ResponseEntity<OddResponseDTO> addOddToMarket(
            @PathVariable Long marketId,
            @Valid @RequestBody OddRequestDTO oddRequestDTO) {

        Odd savedOdd = oddService.addOddToMarket(marketId, oddRequestDTO);
        return ResponseEntity.ok(OddMapper.toDTO(savedOdd));
    }
}
