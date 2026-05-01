package com.gamestudio.users.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
public class UserEntity {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
    private long id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String hashPassword;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Role role;
}
