package com.letmeclean.controller;

import com.letmeclean.dto.Response;
import com.letmeclean.dto.member.request.MemberModifyRequest;
import com.letmeclean.dto.member.request.PasswordModifyRequest;
import com.letmeclean.dto.member.request.SignUpRequest;
import com.letmeclean.dto.member.response.MemberResponse;
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

@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public Response<Void> signUp(@RequestBody SignUpRequest request) {
        memberService.signUp(request.toDto());
        return Response.success();
    }

    @GetMapping("/member-emails/{email}/exists")
    public Response<Boolean> checkEmailDuplicated(@PathVariable String email) {
        return Response.success(memberService.checkEmailDuplicated(email));
    }

    @GetMapping("/member-nicknames/{nickname}/exists")
    public Response<Boolean> checkNicknameDuplicated(@PathVariable String nickname) {
        return Response.success(memberService.checkNicknameDuplicated(nickname));
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

    @PutMapping("/info")
    public Response<MemberResponse> modifyMemberInfo(@CurrentEmail String email, @RequestBody MemberModifyRequest request) {
        MemberResponse response = MemberResponse.fromMemberDto(memberService.modifyMemberInfo(request.toDto(email)));
        return Response.success(response);
    }

    @PutMapping("/password")
    public Response<MemberResponse> modifyPassword(@CurrentEmail String email, @RequestBody PasswordModifyRequest request) {
        MemberResponse response = MemberResponse.fromMemberDto(memberService.modifyPassword(email, request.prevPassword(), request.newPassword()));
        return Response.success(response);
    }
}
