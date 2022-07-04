package com.letmeclean.controller;

import com.letmeclean.common.utils.ResponseConstants;
import com.letmeclean.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.letmeclean.controller.dto.member.MemberRequest.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        memberService.signUp(signUpRequestDto);
        return ResponseConstants.CREATED;
    }
}
