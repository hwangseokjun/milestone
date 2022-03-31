package com.sparta.milestone.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BlogMemberRequestDto {
    private String username;
    private String password;
}
