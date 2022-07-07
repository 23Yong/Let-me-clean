package com.letmeclean.controller;

import com.letmeclean.common.constants.ResponseConstants;
import com.letmeclean.service.AuthService;
import com.letmeclean.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.letmeclean.controller.dto.TokenDto.*;
import static com.letmeclean.controller.dto.member.MemberRequest.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/members")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        memberService.signUp(signUpRequestDto);
        return ResponseConstants.CREATED;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(@RequestBody LoginRequestDto loginRequestDto) {
        TokenInfo token = authService.login(loginRequestDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenInfo> reissue(@RequestBody TokenInfo tokenInfoDto) {
        TokenInfo reissueToken = authService.reissue(tokenInfoDto);
        return ResponseEntity.ok(reissueToken);
    }

    @GetMapping("/members/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        authService.logout(logoutRequestDto);
        return ResponseConstants.OK;
    }
}
