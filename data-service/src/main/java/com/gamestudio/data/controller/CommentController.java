package com.gamestudio.data.controller;

import com.gamestudio.data.dto.request.CommentRequest;
import com.gamestudio.data.dto.response.CommentResponse;
import com.gamestudio.data.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments/")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = commentService.createComment(commentRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(commentResponse.id())
                .toUri();

        return ResponseEntity.created(location).body(commentResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<CommentResponse> findByIdComment(@PathVariable long id) throws EntityNotFoundException {
        return ResponseEntity.ok(commentService.findCommentById(id));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> findAllComments() {
        return ResponseEntity.ok(commentService.findAllComments());
    }

    @GetMapping("/by-game/{game}")
    public ResponseEntity<List<CommentResponse>> findAllCommentsByGame(@PathVariable String game) {
        return ResponseEntity.ok(commentService.findCommentsByGame(game));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable long id) throws EntityNotFoundException {
        commentService.deleteCommentById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllComments() {
        commentService.deleteAllComments();

        return ResponseEntity.noContent().build();
    }
}

