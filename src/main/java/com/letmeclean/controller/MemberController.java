package com.letmeclean.controller;

import com.letmeclean.dto.Response;
import com.letmeclean.dto.issuedcoupon.IssuedCouponResponse;
import com.letmeclean.dto.issuedticket.response.IssuedTicketResponse;
import com.letmeclean.dto.member.request.MemberModifyRequest;
import com.letmeclean.dto.member.request.PasswordModifyRequest;
import com.letmeclean.dto.member.request.SignUpRequest;
import com.letmeclean.dto.member.response.MemberResponse;
import com.letmeclean.dto.payment.response.PaymentResponse;
import com.letmeclean.global.aop.CurrentEmail;
import com.letmeclean.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{email}/exists")
    public Response<Boolean> checkEmailDuplicated(@PathVariable String email) {
        return Response.success(memberService.checkEmailDuplicated(email));
    }

    @GetMapping("/{nickname}/exists")
    public Response<Boolean> checkNicknameDuplicated(@PathVariable String nickname) {
        return Response.success(memberService.checkNicknameDuplicated(nickname));
    }

    @GetMapping("/payments")
    public Response<Page<PaymentResponse>> getMemberOfPaymentList(@CurrentEmail String email, Pageable pageable) {
        Page<PaymentResponse> response = memberService.findPaymentList(email, pageable);
        return Response.success(response);
    }

    @GetMapping("/issued-tickets")
    public Response<Page<IssuedTicketResponse>> getMemberOfIssuedTicketList(@CurrentEmail String email, @PageableDefault(size = 10) Pageable pageable) {
        Page<IssuedTicketResponse> response = memberService.findIssuedTicketList(email, pageable);
        return Response.success(response);
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

    @GetMapping("/coupons")
    public Response<Page<IssuedCouponResponse>> getHoldingCoupons(@CurrentEmail String email, @PageableDefault(size = 10) Pageable pageable) {
        Page<IssuedCouponResponse> response = memberService.getHoldingCoupons(email, pageable);
        return Response.success(response);
    }
}
