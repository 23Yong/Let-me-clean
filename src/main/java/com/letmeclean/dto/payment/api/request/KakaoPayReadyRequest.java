package com.letmeclean.dto.payment.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record KakaoPayReadyRequest(
        // 가맹점 코드 (테스트 결제)
        @NotEmpty String cid,
        // 가맹점 코드 인증키
        String cidSecret,
        // 가맹점 주문번호 - 티켓 결제번호
        @NotEmpty String partnerOrderId,
        // 가맹점 회원 id - 가맹점에서 회원을 구분할 수 있는 id -> member 대체 id
        @NotEmpty String partnerUserId,
        // 상품명 - 티켓 이름
        @NotEmpty String itemName,
        // 상품코드
        String itemCode,
        // 상품수량 - 티켓 수량
        @NotNull Integer quantity,
        // 상품 총액 - 티켓 금액 합계
        @NotNull Integer totalAmount,
        // 상품 비과세 - 일단 0원으로 일괄처리
        @NotNull Integer taxFreeAmount,
        // 상품 부가세 금액
        Integer vatAmount,
        // 컵 보증금
        Integer greenDeposit,
        // 결제 성공 시 redirect URL
        @NotNull String approvalUrl,
        // 결제 취소 시 redirect URL
        @NotNull String cancelUrl,
        // 결제 실패 시 redirect URL
        @NotNull String failUrl
) {

    public KakaoPayReadyRequest(String cid,
                                String cidSecret,
                                String partnerOrderId,
                                String partnerUserId,
                                String itemName,
                                String itemCode,
                                Integer quantity,
                                Integer totalAmount,
                                Integer taxFreeAmount,
                                Integer vatAmount,
                                Integer greenDeposit,
                                String approvalUrl,
                                String cancelUrl,
                                String failUrl) {
        this.cid = "TC0ONETIME";
        this.cidSecret = cidSecret;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.taxFreeAmount = 0;
        this.vatAmount = vatAmount;
        this.greenDeposit = greenDeposit;
        this.approvalUrl = approvalUrl;
        this.cancelUrl = cancelUrl;
        this.failUrl = failUrl;
    }

    public static KakaoPayReadyRequest of(String partnerOrderId,
                                          String partnerUserId,
                                          String itemName,
                                          Integer quantity,
                                          Integer totalAmount,
                                          String approvalUrl,
                                          String cancelUrl,
                                          String failUrl) {
        return new KakaoPayReadyRequest(null,
                null,
                partnerOrderId,
                partnerUserId,
                itemName,
                null,
                quantity,
                totalAmount,
                0,
                null,
                null,
                approvalUrl,
                cancelUrl,
                failUrl);
    }
}
