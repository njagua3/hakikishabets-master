package com.hakikishabets.bets.service;

import com.hakikishabets.bets.dto.GameRequestDTO;
import com.hakikishabets.bets.model.Game;
import com.hakikishabets.bets.model.Market;
import com.hakikishabets.bets.model.Odd;
import com.hakikishabets.bets.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // ✅ Create a new game with markets and odds
    public Game createGame(GameRequestDTO gameDto) {
        Game game = new Game();
        game.setGameName(gameDto.getGameName());
        game.setStartTime(gameDto.getStartTime());
        game.setStatus(gameDto.getStatus());

        List<Market> marketList = gameDto.getMarkets().stream().map(marketDTO -> {
            Market market = new Market();
            market.setName(marketDTO.getName());
            market.setGame(game);  // Set relationship to game

            List<Odd> odds = marketDTO.getOdds().stream().map(oddDTO -> {
                Odd odd = new Odd();
                odd.setSelection(oddDTO.getSelection());
                odd.setValue(oddDTO.getValue());
                odd.setActive(oddDTO.isActive());
                odd.setMarket(market);  // Set relationship to market
                return odd;
            }).toList();

            market.setOdds(odds);
            return market;
        }).toList();

        game.setMarkets(marketList);
        return gameRepository.save(game);
    }

    // Get all upcoming matches
    public List<Game> getUpcomingMatches() {
        return gameRepository.findByStatus("UPCOMING");
    }

    // Get all active matches
    public List<Game> getActiveMatches() {
        return gameRepository.findByStatus("ACTIVE");
    }

    // Get match details by ID
    public Game getMatchById(Long matchId) {
        return gameRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
    }
}
