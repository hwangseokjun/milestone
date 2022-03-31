package com.sparta.milestone.domain;

import com.sparta.milestone.models.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter // Getter 설정 안해놓으면 게시물을 담을 수 없다.
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comments extends Timestamped { // 추가로, 회원의 정보를 검증할 수 있는 자료를 보내줘서 매칭해야 한다.

    @Id // ID 값, Primary Key로 사용하겠다는 뜻입니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 명령. 어느 그룹에 속하든 무관하게 올라감
    private Long id;

    @ManyToOne // (fetch = FetchType.LAZY)로 설정하고 JoinColumn을 제거하는 방법도 있다.
    @JoinColumn(name = "posts_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false) // 코멘트 내용
    private String comments;

    private Integer activate;

    public Comments(Post post, CommentRequestDto requestDto) {
        this.post = post;
        this.nickname = requestDto.getNickname();
        this.username = requestDto.getUsername();
        this.comments = requestDto.getComments();
    }

    public void update(CommentRequestDto requestDto) {
        this.comments = requestDto.getComments();
    }
}
