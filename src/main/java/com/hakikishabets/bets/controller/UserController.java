package com.hakikishabets.bets.controller;

import com.hakikishabets.bets.dto.UserRequestDTO;
import com.hakikishabets.bets.dto.UserResponseDTO;
import com.hakikishabets.bets.mapper.UserMapper;
import com.hakikishabets.bets.model.User;
import com.hakikishabets.bets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        User createdUser = userService.createUser(UserMapper.toEntity(userRequestDTO));
        return ResponseEntity.ok(UserMapper.toDTO(createdUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(u -> ResponseEntity.ok(UserMapper.toDTO(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<UserResponseDTO> getUserByPhoneNumber(@PathVariable String phoneNumber) {
        return userService.getUserByPhoneNumber(phoneNumber)
                .map(user -> ResponseEntity.ok(UserMapper.toDTO(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deleteuser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        return isDeleted
                ? ResponseEntity.ok("User deleted successfully")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @PutMapping("/{id}/balance")
    public ResponseEntity<UserResponseDTO> updateUserBalance(@PathVariable Long id, @RequestParam Double amount) {
        User updatedUser = userService.updateUserBalance(id, BigDecimal.valueOf(amount));
        return ResponseEntity.ok(UserMapper.toDTO(updatedUser));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> dtos = users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
