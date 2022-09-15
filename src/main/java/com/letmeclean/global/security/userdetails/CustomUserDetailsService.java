package com.letmeclean.global.security.userdetails;

import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.exception.LetMeCleanException;
import com.letmeclean.global.security.roles.Role;
import com.letmeclean.model.member.Member;
import com.letmeclean.model.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new LetMeCleanException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s 를(을) 찾을 수 없습니다.", username)));

        return memberToUserDetails(member);
    }

    private UserDetails memberToUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(Role.ROLE_MEMBER.name());

        return new CustomUserDetails(
                member.getEmail(),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
