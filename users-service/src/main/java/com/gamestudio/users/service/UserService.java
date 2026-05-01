package com.gamestudio.users.service;

import com.gamestudio.users.dto.UserRequest;
import com.gamestudio.users.dto.UserResponse;
import com.gamestudio.users.entity.UserEntity;
import com.gamestudio.users.mapper.UserMapper;
import com.gamestudio.users.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true)
    })
    public UserResponse registerUser(UserRequest userRequest) throws EntityExistsException {
        if (userRepository.existsByEmail(userRequest.email()) ||
        userRepository.existsByEmail(userRequest.email())) {
            throw new EntityExistsException("User already exists by email or name");
        }

        UserEntity user = userRepository.save(userMapper.fromRequest(userRequest));

        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "user")
    public UserResponse findUserById(long id) throws EntityNotFoundException {
        UserEntity user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(getUserErrorMessage(id)));

        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "user-email")
    public UserResponse findUserByEmail(String email) throws EntityNotFoundException {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        return userMapper.toResponse(user);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "user"),
            @CacheEvict(value = "user-email", allEntries = true),
            @CacheEvict(value = "users", allEntries = true)
    })
    public void deleteUserById(long id) throws EntityNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException(getUserErrorMessage(id));
        }

        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users")
    public Page<UserResponse> findAllUsers(Pageable pageable) {
        return userRepository
                .findAll(pageable)
                .map(userMapper::toResponse);
    }

    private String getUserErrorMessage(long id) { return "UserEntity with id " + id + " does not exist"; }
}
