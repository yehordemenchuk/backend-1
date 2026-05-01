package com.gamestudio.data.dto.response;

import java.util.Date;

public record RatingResponse(Long id,
                             String game,
                             String player,
                             Integer rating,
                             Date ratedOn) {
}
