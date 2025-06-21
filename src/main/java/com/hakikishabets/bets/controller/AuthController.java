package com.hakikishabets.bets.controller;

import com.hakikishabets.bets.dto.LoginRequestDTO;
import com.hakikishabets.bets.dto.LoginResponseDTO;
import com.hakikishabets.bets.model.User;
import com.hakikishabets.bets.security.JwtUtil;
import com.hakikishabets.bets.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder; // ✅ Injected from AppConfig

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        User user = userService.getUserByPhoneNumber(loginRequest.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("Invalid phone number or password"));

        System.out.println("👉 Raw Password: " + loginRequest.getPassword());
        System.out.println("👉 Encoded Password in DB: " + user.getPassword());
        System.out.println("👉 Matches? " + passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new LoginResponseDTO(null, "Invalid credentials"));
        }

        String token = jwtUtil.generateToken(user.getPhoneNumber());
        return ResponseEntity.ok(new LoginResponseDTO(token, "Login successful"));
    }
}
