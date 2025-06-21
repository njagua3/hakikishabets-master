package com.hakikishabets.bets.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity // Marks this class as a JPA entity that maps to a database table
@Table(name = "bets") // Specifies the table name in the database
public class Bet {

    @Id // Primary key of the Bet entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment strategy
    private Long id;

    @Setter
    @Column(precision = 10, scale = 2) // Optional: improves control over DB decimal storage
    private BigDecimal stake; // Amount of money placed on the bet

    @Setter
    @Column(precision = 10, scale = 2)
    private BigDecimal potentialWin; // Amount of money that can be won if the bet succeeds

    @Setter
    @Enumerated(EnumType.STRING) // Store enum value as a String in the DB (e.g., "PENDING", "WON")
    private BetStatus status; // Status of the bet (e.g., PENDING, WON, LOST)

    @Setter
    private LocalDateTime placedAt; // Date and time the bet was placed

    // Entity Relationships (Foreign Keys)

    @Setter
    @ManyToOne // Many bets can be associated with one user
    @JoinColumn(name = "user_id") // Foreign key column in 'bets' table for the user
    private User user;

    @Setter
    @ManyToOne // Many bets can be associated with one game
    @JoinColumn(name = "game_id") // Foreign key column in 'bets' table for the game
    private Game game;

    @Setter
    @ManyToOne // Many bets can be associated with one market (e.g., Over/Under)
    @JoinColumn(name = "market_id") // Foreign key column in 'bets' table for the market
    private Market market;

    @Setter
    @ManyToOne // Many bets can use the same odd
    @JoinColumn(name = "odd_id") // Foreign key column in 'bets' table for the odd
    private Odd odd;

    // Constructors

    public Bet() {
        // Default constructor required by JPA
    }

    // Constructor to quickly create a bet with all fields using BigDecimal
    public Bet(BigDecimal stake, BigDecimal potentialWin, BetStatus status, LocalDateTime placedAt,
               User user, Game game, Market market, Odd odd) {
        this.stake = stake;
        this.potentialWin = potentialWin;
        this.status = status;
        this.placedAt = placedAt;
        this.user = user;
        this.game = game;
        this.market = market;
        this.odd = odd;
    }
}
