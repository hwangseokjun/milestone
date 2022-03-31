package com.sparta.milestone.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentRequestDto {
    private String nickname;
    private String username;
    private String comments;
}
