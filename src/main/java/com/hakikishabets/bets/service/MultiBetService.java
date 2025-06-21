package com.hakikishabets.bets.service;

import com.hakikishabets.bets.dto.MultiBetRequestDTO;
import com.hakikishabets.bets.dto.MultiBetResponseDTO;
import com.hakikishabets.bets.dto.MultiBetSelectionDTO;
import com.hakikishabets.bets.exception.ResourceNotFoundException;
import com.hakikishabets.bets.mapper.MultiBetMapper;
import com.hakikishabets.bets.model.*;
import com.hakikishabets.bets.repository.MultiBetRepository;
import com.hakikishabets.bets.repository.MultiBetSelectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MultiBetService {

    private final MultiBetRepository multiBetRepo;
    private final MultiBetSelectionRepository selectionRepo;
    private final UserService userService;
    private final GameService gameService;
    private final MarketService marketService;
    private final OddService oddService;

    /**
     * Ensures that the market belongs to the game and the odd belongs to the market.
     */
    private void validateSelectionConsistency(Game game, Market market, Odd odd) {
        if (!market.getGame().getId().equals(game.getId())) {
            throw new IllegalArgumentException("Market (ID: " + market.getId() + ") does not belong to Game (ID: " + game.getId() + ")");
        }
        if (!odd.getMarket().getId().equals(market.getId())) {
            throw new IllegalArgumentException("Odd (ID: " + odd.getId() + ") does not belong to Market (ID: " + market.getId() + ")");
        }
    }

    /**
     * Handles placing a new multi-bet (accumulator bet).
     */
    @Transactional
    public MultiBetResponseDTO placeMultiBet(MultiBetRequestDTO dto) {

        // 1️⃣ Load the user and check their balance
        User user = userService.getUserById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BigDecimal stake = BigDecimal.valueOf(dto.getStake());
        BigDecimal userBalance = user.getBalance();

        if (userBalance.compareTo(stake) < 0)
            throw new RuntimeException("Insufficient balance");

        // 2️⃣ Validate and build selections
        List<MultiBetSelection> legs = new ArrayList<>();
        BigDecimal combinedOdds = BigDecimal.ONE;

        for (MultiBetSelectionDTO sel : dto.getSelections()) {
            Game game = gameService.getMatchById(sel.getGameId());
            Market market = marketService.getMarketById(sel.getMarketId());
            Odd odd = oddService.getOddById(sel.getOddId());

            validateSelectionConsistency(game, market, odd);

            // Multiply combined odds
            combinedOdds = combinedOdds.multiply(BigDecimal.valueOf(odd.getValue()));

            legs.add(MultiBetSelection.builder()
                    .game(game)
                    .market(market)
                    .odd(odd)
                    .status(BetStatus.PENDING)
                    .build());
        }

        // 3️⃣ Calculate potential win
        BigDecimal potentialWin = stake.multiply(combinedOdds);

        // 4️⃣ Deduct stake from user balance
        userService.updateUserBalance(user.getId(), stake.negate());

        // 5️⃣ Create and persist MultiBet parent
        MultiBet parent = MultiBetMapper.toEntity(dto, user, legs);
        parent.setPotentialWin(potentialWin);
        parent.setPlacedAt(LocalDateTime.now());
        parent.setStatus(BetStatus.PENDING);

        MultiBet savedParent = multiBetRepo.save(parent);

        // 6️⃣ Attach legs to parent and save them
        legs.forEach(l -> l.setMultiBet(savedParent));
        selectionRepo.saveAll(legs);

        savedParent.setSelections(legs);

        // 7️⃣ Return DTO for frontend
        return MultiBetMapper.toResponseDTO(savedParent);
    }

    /**
     * Fetch all multi-bets with selections populated.
     */
    public List<MultiBetResponseDTO> getAllMultiBets() {
        return multiBetRepo.findAll().stream()
                .peek(mb -> mb.setSelections(selectionRepo.findByMultiBetId(mb.getId())))
                .map(MultiBetMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific multi-bet by ID.
     */
    public MultiBetResponseDTO getMultiBetById(Long id) {
        MultiBet mb = multiBetRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Multibet not found"));
        mb.setSelections(selectionRepo.findByMultiBetId(id));
        return MultiBetMapper.toResponseDTO(mb);
    }

    /**
     * Get all multi-bets placed by a specific user.
     */
    public List<MultiBetResponseDTO> getUserMultiBets(Long userId) {
        return multiBetRepo.findByUserId(userId).stream()
                .peek(mb -> mb.setSelections(selectionRepo.findByMultiBetId(mb.getId())))
                .map(MultiBetMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Allow editing a multi-bet only if it's still PENDING.
     */
    @Transactional
    public MultiBetResponseDTO updateMultiBet(Long id, MultiBetRequestDTO dto) {

        MultiBet mb = multiBetRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Multibet not found"));

        if (mb.getStatus() != BetStatus.PENDING)
            throw new IllegalStateException("Only pending multi-bets can be updated");

        BigDecimal oldStake = mb.getStake();
        BigDecimal newStake = BigDecimal.valueOf(dto.getStake());

        // 1️⃣ Handle stake update with balance adjustment
        if (newStake.compareTo(oldStake) != 0) {
            // Refund the old stake
            userService.updateUserBalance(mb.getUser().getId(), oldStake);

            // Check if user can afford the new stake
            BigDecimal balanceAfterRefund = userService.getUserById(mb.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                    .getBalance();

            if (balanceAfterRefund.compareTo(newStake) < 0)
                throw new RuntimeException("Insufficient balance for new stake");

            // Deduct the new stake
            userService.updateUserBalance(mb.getUser().getId(), newStake.negate());
        }

        mb.setStake(newStake);

        // 2️⃣ Remove old selections and build new ones
        selectionRepo.deleteAll(selectionRepo.findByMultiBetId(id));

        List<MultiBetSelection> newLegs = new ArrayList<>();
        BigDecimal combinedOdds = BigDecimal.ONE;

        for (MultiBetSelectionDTO sel : dto.getSelections()) {
            Game game = gameService.getMatchById(sel.getGameId());
            Market market = marketService.getMarketById(sel.getMarketId());
            Odd odd = oddService.getOddById(sel.getOddId());

            validateSelectionConsistency(game, market, odd);

            MultiBetSelection leg = MultiBetSelection.builder()
                    .multiBet(mb)
                    .game(game)
                    .market(market)
                    .odd(odd)
                    .status(BetStatus.PENDING)
                    .build();

            combinedOdds = combinedOdds.multiply(BigDecimal.valueOf(leg.getOdd().getValue()));
            newLegs.add(leg);
        }

        selectionRepo.saveAll(newLegs);

        // 3️⃣ Recalculate potential win and save
        mb.setPotentialWin(newStake.multiply(combinedOdds));
        mb.setSelections(newLegs);

        MultiBet saved = multiBetRepo.save(mb);
        return MultiBetMapper.toResponseDTO(saved);
    }
}
