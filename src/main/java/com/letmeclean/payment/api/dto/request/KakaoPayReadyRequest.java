package com.letmeclean.payment.api.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KakaoPayReadyRequest {

    // 가맹점 코드 (테스트 결제)
    @NotEmpty
    private String cid = "TC0ONETIME";

    // 가맹점 코드 인증키
    private String cidSecret;

    // 가맹점 주문번호 - 티켓 결제번호
    @NotEmpty
    private String partnerOrderId;

    // 가맹점 회원 id - 가맹점에서 회원을 구분할 수 있는 id -> member 대체 id
    @NotEmpty
    private String partnerUserId;

    // 상품명 - 티켓 이름
    @NotEmpty
    private String itemName;

    // 상품코드
    private String itemCode;

    // 상품수량 - 티켓 수량
    @NotNull
    private Integer quantity;

    // 상품 총액 - 티켓 금액 합계
    @NotNull
    private Integer totalAmount;

    // 상품 비과세 - 일단 0원으로 일괄처리
    @NotNull
    private Integer taxFreeAmount = 0;

    // 상품 부가세 금액
    private Integer vatAmount;

    // 컵 보증금
    private Integer greenDeposit;

    // 결제 성공 시 redirect URL
    @NotNull
    private String approvalUrl;

    // 결제 취소 시 redirect URL
    @NotNull
    private String cancelUrl;

    // 결제 실패 시 redirect URL
    @NotNull
    private String failUrl;

    public KakaoPayReadyRequest(String partnerOrderId, String partnerUserId,
                                String itemName, Integer quantity, Integer totalAmount,
                                String approvalUrl, String cancelUrl, String failUrl) {
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.approvalUrl = approvalUrl;
        this.cancelUrl = cancelUrl;
        this.failUrl = failUrl;
    }
}
