package com.sparta.milestone.service;

import com.sparta.milestone.domain.Comments;
import com.sparta.milestone.domain.CommentsRepository;
import com.sparta.milestone.domain.Post;
import com.sparta.milestone.domain.PostRepository;
import com.sparta.milestone.models.CommentRequestDto;
import com.sparta.milestone.models.CommentsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentsRepository commentsRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long updateComments(Long id, CommentRequestDto requestDto) {
        Comments comments = commentsRepository.findById(id).orElseThrow( () -> new NullPointerException("오류"));
        comments.update(requestDto);
        return id;
    }

    public CommentsResponseDto addComments(Long id, CommentRequestDto requestDto, String username){
        requestDto.setNickname(username);
        requestDto.setUsername(username);
        Post post = postRepository.findById(id).orElseThrow( () -> new NullPointerException("오류"));
        commentsRepository.save(new Comments(post, requestDto));
        return new CommentsResponseDto(requestDto, post);
    }

    public List<Comments> getDetail(Long id, String username){
        Post post = postRepository.findById(id).orElseThrow( () -> new NullPointerException("오류"));
        List<Comments> comments = commentsRepository.findByPostOrderByModifiedAtDesc(post);
        for ( Comments a : comments) {
            if ( a.getUsername().equals(username) ){
                a.setActivate(1);
            } else
            {
                a.setActivate(0);
            }
        }
        return comments;
    }
}
