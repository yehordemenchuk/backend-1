package org.gamestudio.auth.dto;

import java.io.Serializable;

public record UserResponse(Long id,
                           String name,
                           String hashPassword,
                           String role) implements Serializable {
}
