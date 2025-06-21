package com.hakikishabets.bets.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Odd {

    // Unique identifier for the odd
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Description of the selection (e.g., "Home", "Away", "Draw", "Over 2.5")
    @Setter
    private String selection;

    // Decimal odd value (e.g., 2.45)
    @Setter
    private Double value;

    // Flag to check if the odd is currently active
    @Setter
    private boolean active = true;

    // Many odds belong to one market (e.g., "1X2" market)
    @Setter
    @ManyToOne
    @JoinColumn(name = "market_id") // Foreign key to Market table
    private Market market;

    // Required by JPA
    public Odd() {}

    // Convenience constructor for easier object creation
    public Odd(String selection, Double value, Market market) {
        this.selection = selection;
        this.value = value;
        this.market = market;
    }

}
