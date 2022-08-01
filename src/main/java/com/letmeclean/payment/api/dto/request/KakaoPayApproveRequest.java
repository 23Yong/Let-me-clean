package com.letmeclean.payment.api.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KakaoPayApproveRequest {

    // 가맹점 코드
    @NotEmpty
    private String cid = "TC0ONETIME";

    private String cidSecret;

    // 결제 고유 번호
    @NotEmpty
    private String tid;

    // 가맹점 주문번호, 결제 준비 API 요청과 일치
    @NotEmpty
    private String partnerOrderId;

    // 가맹점 회원 id - user 대체 ID
    @NotEmpty
    private String partnerUserId;

    // 결제승인 요청을 인증하는 토큰
    @NotEmpty
    private String pgToken;

    // 결제 승인 요청에 대해 저장하고 싶은 값
    private String payload;

    // 상품 총액
    private Integer totalAmount;

    public KakaoPayApproveRequest(String tid, String partnerOrderId, String partnerUserId, String pgToken) {
        this.tid = tid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.pgToken = pgToken;
    }
}
