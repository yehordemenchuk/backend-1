package com.gamestudio.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserRequest(@NotEmpty String name,
                          @NotEmpty @Email String email,
                          @NotEmpty String hashPassword,
                          @NotEmpty String role) {
}
