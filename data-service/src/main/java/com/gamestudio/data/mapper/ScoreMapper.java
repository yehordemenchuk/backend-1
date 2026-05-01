package com.gamestudio.data.mapper;

import com.gamestudio.data.dto.request.ScoreRequest;
import com.gamestudio.data.dto.response.ScoreResponse;
import com.gamestudio.data.entity.Score;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ScoreMapper {
    Score fromRequest(ScoreRequest request);

    ScoreResponse toResponse(Score score);

    void updateScoreFromRequest(ScoreRequest request,
                                @MappingTarget Score score);
}
