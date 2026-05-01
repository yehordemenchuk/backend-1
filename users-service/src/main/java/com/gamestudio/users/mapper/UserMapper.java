package com.gamestudio.users.mapper;

import com.gamestudio.users.dto.UserRequest;
import com.gamestudio.users.dto.UserResponse;
import com.gamestudio.users.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity fromRequest(UserRequest userRequest);

    UserResponse toResponse(UserEntity user);
}
