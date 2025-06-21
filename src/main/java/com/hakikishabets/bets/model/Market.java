package com.hakikishabets.bets.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
public class Market {

    // Primary key with auto-increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name of the market type (e.g., 1X2, Over/Under)
    @Setter
    private String name;

    // Many markets belong to one game
    @Setter
    @ManyToOne
    @JoinColumn(name = "game_id", referencedColumnName = "id") // Foreign key to Game table
    private Game game;

    // One market has many odds (possible outcomes with odds)
    @Setter
    @OneToMany(mappedBy = "market", cascade = CascadeType.ALL)
    private List<Odd> odds;

    // Required by JPA
    public Market() {}

    // Custom constructor for easier instantiation
    public Market(String name, Game game) {
        this.name = name;
        this.game = game;
    }

}
