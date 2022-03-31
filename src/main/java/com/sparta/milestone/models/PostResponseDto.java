package com.sparta.milestone.models;

import com.sparta.milestone.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;

// 전달 자료는 Json인데, 한번 더 Json화시켜 전달하면 오류가 생기므로 ResponseDto로 String자료만 Json으로 전달하도록 한다.
@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String username;
    private String subject;
    private String contents;
    private LocalDateTime modifiedAt;
    private Integer likeCnt;
    private Boolean isPostUser = false;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.username = post.getUsername();
        this.subject = post.getSubject();
        this.contents = post.getContents();
        this.modifiedAt = post.getModifiedAt();
        this.likeCnt = post.getLikeCnt();
    }
}