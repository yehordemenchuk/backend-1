package org.gamestudio.auth.service;

import lombok.RequiredArgsConstructor;
import org.gamestudio.auth.dto.UserResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final DiscoveryClientService discoveryClientService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        UserResponse userEntity = discoveryClientService.callUserServiceGetByUsername(email);

        return User.builder()
                .username(userEntity.name())
                .password(userEntity.hashPassword())
                .authorities(userEntity.role())
                .build();
    }
}
