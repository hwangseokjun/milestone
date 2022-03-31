package com.sparta.milestone.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.milestone.config.UserDetailsImpl;
import com.sparta.milestone.domain.BlogMember;
import com.sparta.milestone.domain.BlogMemberRepository;
import com.sparta.milestone.models.KakaoUserInfoDto;
import com.sparta.milestone.models.MemberRoleEnum;
import com.sparta.milestone.models.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BlogMemberService {

    private final BlogMemberRepository blogMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";
    private String pattern = "^[a-zA-Z0-9]*$";

    public String registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String chkPw = requestDto.getChkPw();

        // 아이디 유효성 검사
        if ( !Pattern.matches(pattern, username) || username.length() < 3 ) {
            return "아이디를 확인해 주세요.";
        }
        // 비밀번호 유효성 검사
        if ( ( username.contains(password) || password.length() < 4 ) || !password.equals(chkPw) ) {
            return "비밀번호를 확인해 주세요.";
        }
        // 회원 ID 중복 확인
        Optional<BlogMember> found = blogMemberRepository.findByUsername(username);
        if (found.isPresent()) {
            return "아이디가 중복됩니다.";
        }
// 비밀번호 암효화
        password = passwordEncoder.encode(password);

// 사용자 ROLE 확인
        MemberRoleEnum role = MemberRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = MemberRoleEnum.ADMIN;
        }

        BlogMember member = new BlogMember(username, password, role);
        blogMemberRepository.save(member);

        return "success";
    }

    public void kakaoLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

//         DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();

        BlogMember kakaoUser = blogMemberRepository.findByKakaoId(kakaoId)
                .orElse(null);
        if (kakaoUser == null) {
// 회원가입
// username: kakao nickname
            String nickname = kakaoUserInfo.getNickname();

// password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

// role: 일반 사용자
            MemberRoleEnum role = MemberRoleEnum.USER;

            kakaoUser = new BlogMember(nickname, encodedPassword, role, kakaoId);
            blogMemberRepository.save(kakaoUser);
        }

        // 4. 강제 로그인 처리
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    // 아래의 코드에서 중복되는 변수들은 리팩토링 후 Bean으로 등록해 활용해야 합니다. Bean으로 등록하지 않으면 활용이 되지 않습니다.
    // 에러코드 발생 이유 : 리팩토링 도중 뭔가 삭제했음

    private HttpHeaders headers = new HttpHeaders();
    private RestTemplate rt = new RestTemplate();

    private String getAccessToken(String code) throws JsonProcessingException {
// HTTP Header 생성
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "ee64c8a844e3a81a6dbc72b0eaa9a394");
        body.add("redirect_uri", "http://15.164.230.132/members/kakao/callback");
        body.add("code", code);

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

// HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
// HTTP Header 생성
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        return new KakaoUserInfoDto(id, nickname, email);
    }
}
