package com.gamestudio.data.controller;

import com.gamestudio.data.dto.request.RatingRequest;
import com.gamestudio.data.dto.response.AverageRatingResponse;
import com.gamestudio.data.dto.response.RatingResponse;
import com.gamestudio.data.service.RatingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ratings/")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<RatingResponse> saveRating(@Valid @RequestBody RatingRequest ratingRequest) {
        RatingResponse ratingResponse = ratingService.saveRating(ratingRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ratingResponse.id())
                .toUri();

        return ResponseEntity.created(location).body(ratingResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<RatingResponse> findRatingById(@PathVariable long id) throws EntityNotFoundException {
        RatingResponse ratingResponse = ratingService.findRatingById(id);

        return ResponseEntity.ok(ratingResponse);
    }

    @GetMapping("/avg/{game}")
    public ResponseEntity<AverageRatingResponse> findAverageRatingsByGame(@PathVariable String game) {
        return ResponseEntity.ok(ratingService.getAverageRatingByGame(game));
    }

    @GetMapping
    public ResponseEntity<List<RatingResponse>> findAllRatings() {
        return ResponseEntity.ok(ratingService.findAllRatings());
    }

    @GetMapping("/by-player")
    public ResponseEntity<RatingResponse> findRatingsByGameAndPlayer(@RequestParam String game,
                                                                     @RequestParam String player) {
        return ResponseEntity.ok(ratingService.findRatingByGameAndPlayer(game, player));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteRatingById(@PathVariable long id) throws EntityNotFoundException {
        ratingService.deleteRatingById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllRatings() {
        ratingService.deleteAllRatings();

        return ResponseEntity.noContent().build();
    }
}
