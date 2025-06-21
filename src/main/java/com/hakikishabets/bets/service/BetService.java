package com.hakikishabets.bets.service;

import com.hakikishabets.bets.dto.BetResponseDTO;
import com.hakikishabets.bets.exception.ResourceNotFoundException;
import com.hakikishabets.bets.mapper.BetMapper;
import com.hakikishabets.bets.model.*;
import com.hakikishabets.bets.repository.BetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BetService {

    private final BetRepository betRepository;
    private final UserService userService;
    private final GameService gameService;
    private final MarketService marketService;
    private final OddService oddService;

    @Autowired
    public BetService(
            BetRepository betRepository,
            UserService userService,
            GameService gameService,
            MarketService marketService,
            OddService oddService
    ) {
        this.betRepository = betRepository;
        this.userService = userService;
        this.gameService = gameService;
        this.marketService = marketService;
        this.oddService = oddService;
    }

    /**
     * Place a single bet for a specific game, market, and odd.
     */
    @Transactional
    public BetResponseDTO placeBet(Long userId, Long matchId, Long marketId, Long oddId, BigDecimal stake) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BigDecimal userBalance = user.getBalance();

        // Validate balance
        if (userBalance.compareTo(stake) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        Game game = gameService.getMatchById(matchId);
        Market market = marketService.getMarketById(marketId);
        Odd odd = oddService.getOddById(oddId);

        // Calculate potential win
        BigDecimal potentialWin = stake.multiply(BigDecimal.valueOf(odd.getValue()));

        // Deduct user balance
        userService.updateUserBalance(userId, stake.negate());

        // Create bet entity
        Bet bet = new Bet(stake, potentialWin, BetStatus.PENDING, LocalDateTime.now(), user, game, market, odd);
        Bet savedBet = betRepository.save(bet);

        return BetMapper.toDTO(savedBet);
    }

    /**
     * Update the status of a bet (e.g., PENDING -> WON/LOST).
     */
    public BetResponseDTO updateBetStatus(Long betId, BetStatus betStatus) {
        Bet bet = betRepository.findById(betId)
                .orElseThrow(() -> new ResourceNotFoundException("Bet not found"));

        bet.setStatus(betStatus);
        return BetMapper.toDTO(betRepository.save(bet));
    }

    /**
     * Get all bets.
     */
    public List<Bet> getAllBets() {
        return betRepository.findAll();
    }

    /**
     * Get all bets placed by a specific user.
     */
    public List<Bet> getUserBets(Long userId) {
        return betRepository.findByUserId(userId);
    }
}
