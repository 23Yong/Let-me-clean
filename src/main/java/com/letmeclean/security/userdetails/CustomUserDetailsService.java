package com.letmeclean.security.userdetails;

import com.letmeclean.domain.member.Member;
import com.letmeclean.domain.member.MemberRepository;
import com.letmeclean.security.roles.Role;
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
                .orElseThrow(() -> new UsernameNotFoundException(username + " 을 데이터베이스에서 찾을 수 없습니다."));

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
