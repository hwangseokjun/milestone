package com.sparta.milestone.models;

import com.sparta.milestone.domain.BlogMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeRequestDto {
    private Long memberID;
    private Long postID;

    public LikeRequestDto(Long id, BlogMember member) {
        this.postID = id;
        this.memberID = member.getId();
    }
}
