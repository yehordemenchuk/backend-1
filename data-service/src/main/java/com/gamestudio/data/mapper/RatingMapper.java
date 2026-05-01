package com.gamestudio.data.mapper;

import com.gamestudio.data.dto.request.RatingRequest;
import com.gamestudio.data.dto.response.RatingResponse;
import com.gamestudio.data.entity.Rating;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    Rating fromRequest(RatingRequest ratingRequest);

    RatingResponse toResponse(Rating rating);
}
