package com.sparta.milestone.domain;

import com.sparta.milestone.models.LikeRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name="love")
public class Love {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // (fetch = FetchType.LAZY)로 설정하고 JoinColumn을 제거하는 방법도 있다.
    @JoinColumn(name = "member_id", nullable = false)
    private BlogMember member;

    @Column(name = "post_id", nullable = false)
    private Long postID;

    public Love(BlogMember member, LikeRequestDto requestDto) {
        this.member = member;
        this.postID = requestDto.getPostID();
    }

}
