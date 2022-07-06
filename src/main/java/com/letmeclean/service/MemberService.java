package com.letmeclean.service;

import com.letmeclean.domain.member.Member;
import com.letmeclean.domain.member.MemberRepository;
import com.letmeclean.controller.dto.member.MemberRequest.SignUpRequestDto;
import com.letmeclean.exception.member.DuplicatedEmailException;
import com.letmeclean.exception.member.DuplicatedNicknameException;
import com.letmeclean.exception.member.InvalidPasswordException;
import com.letmeclean.exception.member.NotMatchPasswordException;
import com.letmeclean.common.utils.MatcherUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private boolean checkEmailDuplicated(String email) {
        return memberRepository.existsByEmail(email);
    }

    private boolean checkNicknameDuplicated(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    private boolean isValidPassword(String password) {
        return MatcherUtils.isMatch(password);
    }

    private boolean checkConfirmPassword(String password, String confirmPassword) {
        return !password.equals(confirmPassword);
    }

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (checkEmailDuplicated(signUpRequestDto.getEmail())) {
            throw DuplicatedEmailException.getInstance();
        }
        if (checkNicknameDuplicated(signUpRequestDto.getNickname())) {
            throw DuplicatedNicknameException.getInstance();
        }
        if (!isValidPassword(signUpRequestDto.getPassword())) {
            throw InvalidPasswordException.getInstance();
        }
        if (checkConfirmPassword(signUpRequestDto.getPassword(), signUpRequestDto.getConfirmPassword())) {
            throw NotMatchPasswordException.getInstance();
        }

        signUpRequestDto.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));

        Member member = signUpRequestDto.toEntity();
        memberRepository.save(member);
    }
}
