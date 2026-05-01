package org.gamestudio.auth.service;

import lombok.RequiredArgsConstructor;
import org.gamestudio.auth.dto.UserResponse;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscoveryClientService {
    private final RestTemplate restTemplate;

    private final DiscoveryClient discoveryClient;

    public UserResponse callUserServiceGetByUsername(String username) {
        List<ServiceInstance> instances = discoveryClient.getInstances("users-service");

        if (!instances.isEmpty()) {
            String url = instances.get(0).getUri() + "/api/v1/users/by-email/" + username;

            return restTemplate.getForObject(url, UserResponse.class);
        }

        return null;
    }
}
