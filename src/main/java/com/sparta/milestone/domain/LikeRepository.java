package com.sparta.milestone.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Love, Long> {
    Love findByMemberAndAndPostID(BlogMember member, Long PostID);
}
