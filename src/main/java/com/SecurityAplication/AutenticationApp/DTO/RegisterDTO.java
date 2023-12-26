package com.SecurityAplication.AutenticationApp.DTO;

import com.SecurityAplication.AutenticationApp.domain.user.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
