package com.letmeclean.controller;

import com.letmeclean.common.constants.ResponseConstants;
import com.letmeclean.service.LoginService;
import com.letmeclean.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.letmeclean.controller.dto.TokenDto.*;
import static com.letmeclean.controller.dto.member.MemberRequest.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;

    @PostMapping("/members")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        memberService.signUp(signUpRequestDto);
        return ResponseConstants.CREATED;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(@RequestBody LoginRequestDto loginRequestDto) {
        TokenInfo token = loginService.login(loginRequestDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenInfo> reissue(@RequestBody TokenInfo tokenInfoDto) {
        TokenInfo reissueToken = loginService.reissue(tokenInfoDto);
        return ResponseEntity.ok(reissueToken);
    }
}
