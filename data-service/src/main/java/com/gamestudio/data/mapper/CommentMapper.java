package com.gamestudio.data.mapper;

import com.gamestudio.data.dto.request.CommentRequest;
import com.gamestudio.data.dto.response.CommentResponse;
import com.gamestudio.data.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment fromRequest(CommentRequest commentRequest);

    CommentResponse toResponse(Comment comment);
}
