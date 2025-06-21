package com.hakikishabets.bets.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 👇 Test password match at startup
    @PostConstruct
    public void generateHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "johnpass";
        String hashed = encoder.encode(rawPassword);
        System.out.println("✅ admin123 hash to insert in DB: " + hashed);
    }
}
