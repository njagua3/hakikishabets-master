package com.hakikishabets.bets.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity // annotation means the game class is mapped to a db table called game
public class Game {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //db will auto increase the ID
    private Long id;

    @Setter
    private String gameName;
    @Setter
    private LocalDateTime startTime;
    @Setter
    private String status; // Can be: UPCOMING, ACTIVE, FINISHED, CANCELLED

    /*
    - The mappedBy = "game" attribute tells JPA that the Market entity has a field called game which owns the relationship.
    - cascade = CascadeType.ALL: If a game is saved or deleted, those actions cascade to its related markets.
     */
    @Setter
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL) // Renamed field to 'game'
    private List<Market> markets;

    // Constructors
    public Game() {}

    public Game(String gameName, LocalDateTime startTime, String status) {
        this.gameName = gameName;
        this.startTime = startTime;
        this.status = status;
    }

    public String getName() {
        return this.gameName;
    }
}
