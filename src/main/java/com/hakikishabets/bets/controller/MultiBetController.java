package com.hakikishabets.bets.controller;

import com.hakikishabets.bets.dto.MultiBetRequestDTO;
import com.hakikishabets.bets.dto.MultiBetResponseDTO;
import com.hakikishabets.bets.service.MultiBetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/multibets")
@RequiredArgsConstructor
public class MultiBetController {

    private final MultiBetService multiBetService;

    /* ────────────────────────────────
       CREATE
       ──────────────────────────────── */

    @PostMapping
    public ResponseEntity<MultiBetResponseDTO> placeMultiBet(
            @Valid @RequestBody MultiBetRequestDTO request) {

        MultiBetResponseDTO created = multiBetService.placeMultiBet(request);
        URI location = URI.create("/api/multibets/" + created.getMultiBetId());
        return ResponseEntity.created(location).body(created);
    }

    /* ────────────────────────────────
       READ
       ──────────────────────────────── */

    // 1. Get one by ID
    @GetMapping("/{id}")
    public MultiBetResponseDTO getMultiBetById(@PathVariable Long id) {
        return multiBetService.getMultiBetById(id);
    }

    // 2. Get all multi-bets (admin endpoint)
    @GetMapping
    public List<MultiBetResponseDTO> getAllMultiBets() {
        return multiBetService.getAllMultiBets();
    }

    // 3. Get multibets for a specific user
    @GetMapping("/user/{userId}")
    public List<MultiBetResponseDTO> getUserMultiBets(@PathVariable Long userId) {
        return multiBetService.getUserMultiBets(userId);
    }

    /* ────────────────────────────────
       UPDATE
       ──────────────────────────────── */

    /**
     * Full replace – expects the same body shape you used to create.
     * If you only want to update status or stake, create a smaller DTO.
     */
    @PutMapping("/{id}")
    public MultiBetResponseDTO updateMultiBet(
            @PathVariable Long id,
            @Valid @RequestBody MultiBetRequestDTO request) {

        return multiBetService.updateMultiBet(id, request);
    }


}
