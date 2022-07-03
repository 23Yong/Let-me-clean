package com.letmeclean.controller;

import com.letmeclean.controller.dto.request.member.MemberRequest;
import com.letmeclean.controller.dto.response.member.MemberResponse;
import com.letmeclean.controller.dto.response.member.MemberResponse.SignUpResponseDto;
import com.letmeclean.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.letmeclean.controller.dto.request.member.MemberRequest.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto signUpResponseDto = memberService.signUp(signUpRequestDto);
        return ResponseEntity.ok(signUpResponseDto);
    }
}
