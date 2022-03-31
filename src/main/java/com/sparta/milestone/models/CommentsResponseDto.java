package com.sparta.milestone.models;

import com.sparta.milestone.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentsResponseDto {

    private Long id;
    private String nickname;
    private String comments;
    private LocalDateTime modifiedAt;

    public CommentsResponseDto(CommentRequestDto requestDto, Post post){
        this.id = post.getId();
        this.nickname = requestDto.getNickname();
        this.comments = requestDto.getComments();
        this.modifiedAt = post.getModifiedAt();
    }
}
