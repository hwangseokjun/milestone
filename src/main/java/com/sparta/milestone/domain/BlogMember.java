package com.sparta.milestone.domain;

import com.sparta.milestone.models.BlogMemberRequestDto;
import com.sparta.milestone.models.MemberRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name="members")
public class BlogMember extends Timestamped {

    @Id // ID 값, Primary Key로 사용하겠다는 뜻입니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 명령입니다.
    private Long id;

    @Column(nullable = false, unique = true) // 컬럼 값이고 반드시 값이 존재해야 함을 나타냅니다.
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRoleEnum role;

    @Column(unique = true)
    private Long kakaoId;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) // mappedBy 설정을 삭제해주니 잘 작동합니다.
    private List<Love> loves;

    public BlogMember(String username, String password, MemberRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.kakaoId = null;
    }

    public BlogMember(String username, String password, MemberRoleEnum role, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    public BlogMember(BlogMemberRequestDto requestDto){
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
    }

    public void update(BlogMemberRequestDto requestDto){
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
    }
}

