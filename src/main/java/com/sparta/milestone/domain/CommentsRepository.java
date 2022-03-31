package com.sparta.milestone.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByPostOrderByModifiedAtDesc(Post post);
}