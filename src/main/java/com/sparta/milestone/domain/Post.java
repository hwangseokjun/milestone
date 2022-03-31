package com.sparta.milestone.domain;

import com.sparta.milestone.models.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

@Getter // Getter 설정 안해놓으면 게시물을 담을 수 없다.
@NoArgsConstructor
@Entity
public class Post extends Timestamped{

    @Id // ID 값, Primary Key로 사용하겠다는 뜻입니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 명령입니다.
    private Long id;

    @Column(nullable = false) // 컬럼 값이고 반드시 값이 존재해야 함을 나타냅니다.
    private String subject;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String contents;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) // mappedBy 설정을 삭제해주니 잘 작동합니다.
    private List<Comments> comments;

    //좋아요 개수
    @Formula("(SELECT count(1) FROM love l WHERE l.post_id = id)")
    private Integer likeCnt;

    public Post(PostRequestDto requestDto){
        this.subject = requestDto.getSubject();
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }

    public void update(PostRequestDto requestDto){
        this.subject = requestDto.getSubject();
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }
}
