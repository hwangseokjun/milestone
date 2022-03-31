package com.sparta.milestone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.milestone.config.UserDetailsImpl;
import com.sparta.milestone.domain.Comments;
import com.sparta.milestone.domain.PostRepository;
import com.sparta.milestone.models.PostResponseDto;
import com.sparta.milestone.models.SignupRequestDto;
import com.sparta.milestone.service.BlogMemberService;
import com.sparta.milestone.service.LikeService;
import com.sparta.milestone.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

// 아래의 공간에서는 주로 다른 페이지로 렌더링하는 기능을 사용합니다.

@Controller
@RequiredArgsConstructor
public class BlogController {

    private final PostRepository blogRepository; // 왜 이런 문제가 생기는가? AutoWired를 써서 빈으로 등록하니, RequireArgs가 안먹혔음
    private final BlogMemberService blogMemberService;
    private final CommentService commentService;
    private final LikeService likeService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if ( userDetails == null ){
            model.addAttribute("nickname", userDetails);
            return "index";
        }
        model.addAttribute("nickname", userDetails.getUsername());
        return "index";
    }

    @GetMapping("/blogs/details/{id}")
    public String readBlog(@PathVariable Long id, Model md, @AuthenticationPrincipal UserDetailsImpl userDetails){
        List<Comments> commentsList = new ArrayList<Comments>();
        PostResponseDto responseDto = new PostResponseDto(blogRepository.findById(id).orElseThrow(()->new NullPointerException("자료없음")));
        if ( userDetails == null ){
            commentsList = commentService.getDetail(id, null);
        } else{
            String username = userDetails.getUsername();
            if ( responseDto.getUsername().equals(username) ) {
                responseDto.setIsPostUser(true);
            }
            md.addAttribute("a","a");
            commentsList = commentService.getDetail(id, username);
            md.addAttribute("likeCheck", likeService.likeChecked(id, username));
        }
        md.addAttribute("commentsList", commentsList);
        md.addAttribute("posts", responseDto);
        return "detail";
    }

    @GetMapping("/blogs/write")
    public String postBlog(Model md, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return "write";
    }

    // 접근거부
    @GetMapping("/members/deny")
    public String deny() {
        return "accessDenied";
    }

    // 로그아웃 완료
    @GetMapping("/members/successLogout")
    public String successLogout() {
        return "logout";
    }

    // 회원 로그인 페이지
    @PreAuthorize("isAnonymous()")
    @GetMapping("/members/login")
    public String login() {
        return "login";
    }

    // 회원 가입 페이지
    @PreAuthorize("isAnonymous()")
    @GetMapping("/members/signup")
    public String signup() {
        return "signup";
    }

    // 회원 가입 요청 처리
    @PostMapping("/members/signup")
    public String registerUser(SignupRequestDto requestDto, RedirectAttributes rttr) {
        String str = blogMemberService.registerUser(requestDto);
        if ( str.equals("success") ) {
            return "redirect:/members/login";
        }
        rttr.addFlashAttribute("response", str);
        return "redirect:/members/signup";
    }

    // 카카오 콜백 주소
    @GetMapping("/members/kakao/callback")
    public String kakaoLogin(@RequestParam String code) throws JsonProcessingException { // 의문이 하나 풀림. RequestParam은 주소로 전달된 데이터를 의미한다.
        blogMemberService.kakaoLogin(code);
        return "redirect:/";
    }

}
