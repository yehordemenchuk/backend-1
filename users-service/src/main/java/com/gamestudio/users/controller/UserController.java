package com.gamestudio.users.controller;

import com.gamestudio.users.dto.UserRequest;
import com.gamestudio.users.dto.UserResponse;
import com.gamestudio.users.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.registerUser(userRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userResponse.id())
                .toUri();

        return ResponseEntity.created(location).body(userResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long id)
            throws EntityNotFoundException {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping("by-email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email)
        throws EntityNotFoundException {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<UserResponse>>> getAllUsers(Pageable pageable,
                                                                             PagedResourcesAssembler<UserResponse> pagedResourcesAssembler) {

        Page<UserResponse> users = userService.findAllUsers(pageable);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(users));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long id) {
        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }
}
