package com.letmeclean.controller;

import com.letmeclean.global.aop.CurrentEmail;
import com.letmeclean.global.constants.ResponseConstants;
import com.letmeclean.dto.issuedticket.response.IssuedTicketDetailResponse;
import com.letmeclean.service.MemberService;
import com.letmeclean.dto.payment.response.PaymentDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.letmeclean.dto.member.request.MemberRequest.*;

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

    @GetMapping("/members/payments")
    public ResponseEntity<List<PaymentDetailResponse>> getMemberOfPaymentList(@CurrentEmail String email, Pageable pageable) {
        List<PaymentDetailResponse> payments = memberService.findPaymentList(email, pageable);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/members/issued-tickets")
    public ResponseEntity<List<IssuedTicketDetailResponse>> getMemberOfIssuedTicketList(@CurrentEmail String email, Pageable pageable) {
        List<IssuedTicketDetailResponse> issuedTickets = memberService.findIssuedTicketList(email, pageable);
        return ResponseEntity.ok(issuedTickets);
    }

    @PutMapping("/members/info")
    public ResponseEntity<Void> editMemberInfo(@CurrentEmail String email, @RequestBody EditInfoRequestDto editInfoRequestDto) {
        memberService.editMemberInfo(email, editInfoRequestDto);
        return ResponseConstants.OK;
    }

    @PutMapping("/members/password")
    public ResponseEntity<Void> changePassword(@CurrentEmail String email, @RequestBody EditPasswordRequestDto editPasswordRequestDto) {
        memberService.editPassword(email, editPasswordRequestDto);

        return ResponseConstants.OK;
    }
}
