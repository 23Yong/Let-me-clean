package com.letmeclean.dto.payment.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotEmpty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record KakaoPayApproveRequest(
        // 가맹점 코드
        @NotEmpty String cid,
        String cidSecret,
        // 결제 고유 번호
        @NotEmpty String tid,
        // 가맹점 주문번호, 결제 준비 API 요청과 일치
        @NotEmpty String partnerOrderId,
        // 가맹점 회원 id - user 대체 ID
        @NotEmpty String partnerUserId,
        // 결제승인 요청을 인증하는 토큰
        @NotEmpty String pgToken,
        // 결제 승인 요청에 대해 저장하고 싶은 값
        String payload,
        // 상품 총액
        Integer totalAmount
) {

    public KakaoPayApproveRequest(String cid, String cidSecret, String tid, String partnerOrderId, String partnerUserId, String pgToken, String payload, Integer totalAmount) {
        this.cid = cid;
        this.cidSecret = cidSecret;
        this.tid = tid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.pgToken = pgToken;
        this.payload = payload;
        this.totalAmount = totalAmount;
    }

    public static KakaoPayApproveRequest of(String tid, String partnerOrderId, String partnerUserId, String pgToken) {
        return new KakaoPayApproveRequest("TC0ONETIME", null, tid, partnerOrderId, partnerUserId, pgToken, null, null);
    }
}
