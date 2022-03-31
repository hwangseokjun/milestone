package com.sparta.milestone.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogMemberRepository extends JpaRepository<BlogMember, Long> {
    Optional<BlogMember> findByUsername(String username);
    Optional<BlogMember> findByKakaoId(Long kakaoId);
}