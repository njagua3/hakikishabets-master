package com.hakikishabets.bets.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a multi-bet placed by a user.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultiBet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The stake amount the user has placed on the multi-bet.
     * Uses BigDecimal for precise monetary values.
     */
    @NotNull
    @Column(precision = 10, scale = 2)
    private BigDecimal stake;

    /**
     * The potential amount the user could win from the multi-bet.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal potentialWin;

    /**
     * The current status of the multi-bet (e.g., PENDING, WON, LOST).
     */
    @Enumerated(EnumType.STRING)
    private BetStatus status;

    /**
     * Timestamp indicating when the multi-bet was placed.
     */
    @NotNull
    private LocalDateTime placedAt;

    /**
     * The user who placed the multi-bet.
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The selections that make up this multi-bet.
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "multiBet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MultiBetSelection> selections;
}
