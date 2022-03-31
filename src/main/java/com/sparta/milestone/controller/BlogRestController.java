package com.sparta.milestone.controller;

import com.sparta.milestone.config.UserDetailsImpl;
import com.sparta.milestone.domain.CommentsRepository;
import com.sparta.milestone.domain.Post;
import com.sparta.milestone.domain.PostRepository;
import com.sparta.milestone.models.PostRequestDto;
import com.sparta.milestone.models.CommentRequestDto;
import com.sparta.milestone.models.CommentsResponseDto;
import com.sparta.milestone.models.PostResponseDto;
import com.sparta.milestone.service.LikeService;
import com.sparta.milestone.service.PostService;
import com.sparta.milestone.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class BlogRestController {

    private final PostRepository postRepository;
    private final PostService blogService;
    private final CommentService commentService;
    private final CommentsRepository commentsRepository;
    private final LikeService likeService;

    @GetMapping("/blogs")
    public List<PostResponseDto> getBlog(){
        return  blogService.getPost();
    }

    @PostMapping("/blogs")
    public Post postBlog(@RequestBody PostRequestDto requestDto,
                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        requestDto.setUsername(userDetails.getUsername());
        return postRepository.save(new Post(requestDto));
    }

    @PutMapping("/blogs/{id}")
    public Long updateBlog(@PathVariable Long id, @RequestBody PostRequestDto requestDto){
        return blogService.update(id, requestDto);
    }

    @DeleteMapping("/blogs/{id}")
    public Long deleteBlog(@PathVariable Long id){
        postRepository.deleteById(id);
        return id;
    }

    @PutMapping("/blogs/{id}/comments")
    public Long updateComments(@PathVariable Long id, @RequestBody CommentRequestDto requestDto) {
        return commentService.updateComments(id, requestDto);
    }

    @DeleteMapping("/blogs/{id}/comments")
    public Long deleteComment(@PathVariable Long id){
        commentsRepository.deleteById(id);
        return id;
    }

    @PostMapping("/blogs/{id}/comments")
    public CommentsResponseDto postComments(
            @PathVariable Long id,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.addComments(id, requestDto, userDetails.getUsername());
    }

    // 게시물 번호(Long id), 회원 ID(user Details로 찾기) 전달하여 저장
    @PutMapping("/likes/{id}")
    public Long likePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.putLike(id, userDetails.getUsername());
    }

}
