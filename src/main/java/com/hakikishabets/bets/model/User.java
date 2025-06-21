package com.hakikishabets.bets.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users") // Avoids conflict with reserved keyword "user"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(unique = true, nullable = false)
    private String phoneNumber; // ✅ Replaces username/email as login field

    @Setter
    @Column(nullable = false)
    private String password;    // ✅ For login/authentication

    @Setter
    @Column(precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bet> bets = new ArrayList<>();

    public User() {}

    public User(String phoneNumber, String password, BigDecimal balance) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.balance = balance;
    }
}
