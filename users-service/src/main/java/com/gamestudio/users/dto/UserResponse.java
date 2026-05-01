package com.gamestudio.users.dto;

import java.io.Serializable;

public record UserResponse(Long id,
                           String name,
                           String email,
                           String hashPassword,
                           String role) implements Serializable {
}
