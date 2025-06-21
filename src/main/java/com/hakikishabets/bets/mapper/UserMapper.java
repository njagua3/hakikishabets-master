package com.hakikishabets.bets.mapper;

import com.hakikishabets.bets.dto.UserRequestDTO;
import com.hakikishabets.bets.dto.UserResponseDTO;
import com.hakikishabets.bets.model.User;

public class UserMapper {

    // Convert DTO to Entity
    public static User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(dto.getPassword());
        user.setBalance(dto.getBalance());
        return user;
    }

    // Convert Entity to DTO
    public static UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setBalance(user.getBalance());
        return dto;
    }
}
