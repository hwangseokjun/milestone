package com.sparta.milestone.service;

import com.sparta.milestone.config.UserDetailsImpl;
import com.sparta.milestone.domain.BlogMember;
import com.sparta.milestone.domain.BlogMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {

    private final BlogMemberRepository blogMemberRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BlogMember member = blogMemberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find " + username));

        return new UserDetailsImpl(member);
    }
}