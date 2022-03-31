package com.sparta.milestone.service;

import com.sparta.milestone.domain.*;
import com.sparta.milestone.models.LikeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final BlogMemberRepository memberRepository;

    public Long putLike(Long id, String username) {
        BlogMember member = memberRepository.findByUsername(username)
                .orElseThrow( () -> new NullPointerException(""));
        LikeRequestDto requestDto = new LikeRequestDto(id, member);
        try{
            Love like = likeRepository.findByMemberAndAndPostID(member, id);
            likeRepository.deleteById(like.getId());

        }catch (Exception e) {
            likeRepository.save(new Love(member, requestDto));
        }
        return id;
    }

    public String likeChecked(Long id, String username) {
        try {
            Love love = likeRepository.findByMemberAndAndPostID(memberRepository.findByUsername(username).get(), id);
            System.out.println(love.getId());
            return "isChecked";
        } catch (Exception e) {
            return "isNotChecked";
        }
    }

}
