package com.sparta.milestone.service;

import com.sparta.milestone.domain.*;
import com.sparta.milestone.models.PostRequestDto;
import com.sparta.milestone.models.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository blogRepository;

    @Transactional
    public Long update(Long id, PostRequestDto requestDto){
        Post post = blogRepository.findById(id).orElseThrow( () -> new NullPointerException("게시물이 존재하지 않습니다.")); // 반드시 해당 엔티티를 받아와야 변경이 가능하다.
        post.update(requestDto);
        return id;
    }

    public List<PostResponseDto> getPost(){
        List<Post> post = blogRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> responseDto = new ArrayList<PostResponseDto>();
        for ( Post a : post) {
            PostResponseDto b = new PostResponseDto(a);
            responseDto.add(b);
        }
        return responseDto;
    }
}
