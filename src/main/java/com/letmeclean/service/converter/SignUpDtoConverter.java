package com.letmeclean.service.converter;

import com.letmeclean.controller.dto.request.member.MemberRequest.SignUpRequestDto;
import com.letmeclean.controller.dto.response.member.MemberResponse;
import com.letmeclean.domain.member.Member;
import com.letmeclean.domain.member.MemberStatus;
import org.springframework.stereotype.Component;

import static com.letmeclean.controller.dto.response.member.MemberResponse.*;

@Component
public class SignUpDtoConverter {

    public Member convertSignUpRequestToMember(SignUpRequestDto signUpRequestDto, String hashedPassword) {
        return Member.builder()
                .email(signUpRequestDto.getEmail())
                .password(hashedPassword)
                .name(signUpRequestDto.getName())
                .nickname(signUpRequestDto.getNickname())
                .tel(signUpRequestDto.getTel())
                .build();
    }

    public SignUpResponseDto convertMemberToSignUpResponse(Member member) {
        return SignUpResponseDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .tel(member.getTel())
                .status(MemberStatus.DEFAULT)
                .build();
    }
}
