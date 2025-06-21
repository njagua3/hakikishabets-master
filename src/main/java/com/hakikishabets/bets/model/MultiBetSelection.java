package com.hakikishabets.bets.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultiBetSelection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "multi_bet_id")
    private MultiBet multiBet;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "market_id")
    private Market market;

    @ManyToOne
    @JoinColumn(name = "odd_id")
    private Odd odd;

    @Enumerated(EnumType.STRING)
    private BetStatus status; // This allows you to track each selection (optional)
}
