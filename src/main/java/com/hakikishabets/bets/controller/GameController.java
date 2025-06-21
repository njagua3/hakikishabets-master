package com.hakikishabets.bets.controller;

import com.hakikishabets.bets.dto.GameRequestDTO;
import com.hakikishabets.bets.dto.GameResponseDTO;
import com.hakikishabets.bets.mapper.GameMapper;
import com.hakikishabets.bets.model.Game;
import com.hakikishabets.bets.service.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // ✅ Get all upcoming matches as DTOs
    @GetMapping("/upcoming")
    public ResponseEntity<List<GameResponseDTO>> getUpcomingMatches() {
        List<Game> upcomingGames = gameService.getUpcomingMatches();
        List<GameResponseDTO> dtos = upcomingGames.stream()
                .map(GameMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ✅ Get all active matches as DTOs
    @GetMapping("/active")
    public ResponseEntity<List<GameResponseDTO>> getActiveMatches() {
        List<Game> activeGames = gameService.getActiveMatches();
        List<GameResponseDTO> dtos = activeGames.stream()
                .map(GameMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ✅ Get match details by ID as a DTO
    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDTO> getMatchById(@PathVariable Long id) {
        Game game = gameService.getMatchById(id);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }
        GameResponseDTO dto = GameMapper.toDTO(game);
        return ResponseEntity.ok(dto);
    }

    // ✅ Create a game (still returns full Game entity)
    @PostMapping
    public ResponseEntity<Game> createGame(@Valid @RequestBody GameRequestDTO gameDto) {
        Game createdGame = gameService.createGame(gameDto);
        return ResponseEntity.ok(createdGame);
    }
}
