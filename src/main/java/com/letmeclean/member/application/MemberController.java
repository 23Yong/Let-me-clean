package com.letmeclean.member.application;

import com.letmeclean.global.constants.ResponseConstants;
import com.letmeclean.global.utils.SecurityUtil;
import com.letmeclean.payment.domain.Payment;
import com.letmeclean.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.letmeclean.member.dto.MemberRequest.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        memberService.signUp(signUpRequestDto);
        return ResponseConstants.CREATED;
    }

    @GetMapping("/member-emails/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicated(@PathVariable String email) {
        return ResponseEntity.ok(memberService.checkEmailDuplicated(email));
    }

    @GetMapping("/member-nicknames/{nickname}/exists")
    public ResponseEntity<Boolean> checkNicknameDuplicated(@PathVariable String nickname) {
        return ResponseEntity.ok(memberService.checkNicknameDuplicated(nickname));
    }

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getMemberOfPaymentList(
            @PageableDefault(size = 5) Pageable pageable) {
        String email = SecurityUtil.getCurrentMemberEmail();
        List<Payment> payments = memberService.findPaymentList(email, pageable);
        return ResponseEntity.ok(payments);
    }
}
