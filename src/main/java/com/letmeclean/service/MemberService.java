package com.letmeclean.service;

import com.letmeclean.controller.dto.response.member.MemberResponse;
import com.letmeclean.domain.member.Member;
import com.letmeclean.domain.member.MemberRepository;
import com.letmeclean.controller.dto.request.member.MemberRequest.SignUpRequestDto;
import com.letmeclean.exception.member.DuplicatedEmailException;
import com.letmeclean.exception.member.DuplicatedNicknameException;
import com.letmeclean.exception.member.InvalidPasswordException;
import com.letmeclean.exception.member.NotMatchPasswordException;
import com.letmeclean.service.converter.SignUpDtoConverter;
import com.letmeclean.service.encryption.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.letmeclean.controller.dto.response.member.MemberResponse.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final SignUpDtoConverter signUpDtoConverter;
    private final PasswordEncoder passwordEncoder;

    private boolean checkEmailDuplicated(String email) {
        return memberRepository.existsByEmail(email);
    }

    private boolean checkNicknameDuplicated(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    private boolean isValidPassword(String password) {
        final int MIN = 8;
        final int MAX = 20;
        final String REGEX =
                "^((?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\W]).{" + MIN + "," + MAX + "})$";

        Matcher matcher = Pattern.compile(REGEX).matcher(password);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }

    private boolean checkConfirmPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword) ? false : true;
    }

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        if (checkEmailDuplicated(signUpRequestDto.getEmail())) {
            throw new DuplicatedEmailException();
        }
        if (checkNicknameDuplicated(signUpRequestDto.getNickname())) {
            throw new DuplicatedNicknameException();
        }
        if (!isValidPassword(signUpRequestDto.getPassword())) {
            throw new InvalidPasswordException();
        }
        if (checkConfirmPassword(signUpRequestDto.getPassword(), signUpRequestDto.getConfirmPassword())) {
            throw new NotMatchPasswordException();
        }

        String hashedPassword = passwordEncoder.encrypt(signUpRequestDto.getPassword());
        System.out.println(hashedPassword);
        Member member = signUpDtoConverter.convertSignUpRequestToMember(signUpRequestDto, hashedPassword);

        Member savedMember = memberRepository.save(member);

        return signUpDtoConverter.convertMemberToSignUpResponse(savedMember);
    }
}
