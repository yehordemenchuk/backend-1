package com.gamestudio.data.dto.response;

import java.util.Date;

public record ScoreResponse(Long id,
                            String game,
                            String player,
                            Integer points,
                            Date playedOn) {
}
