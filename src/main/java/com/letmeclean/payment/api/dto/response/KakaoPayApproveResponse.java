package com.letmeclean.payment.api.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayApproveResponse {

    // 요청 고유 번호
    private String aid;

    // 결제 고유 번호
    private String tid;

    // 가맹점 코드
    private String cid;

    // 정기결제용 ID, 정기결제 CID로 단건결제 요청 시 발급
    private String sid;

    // 가맹점 주문번호
    private String partnerOrderId;

    // 가맹점 회원 id
    private String partnerUserId;

    // 결제 수단, CARD 또는 MONEY 중 하나
    private String paymentMethodType;

    // 결제 금액 정보
    private Amount amount;

    // 결제 상세 정보, 결제수단이 카드일 경우만 포함
    private CardInfo cardInfo;

    // 상품 이름
    private String itemName;

    // 상품 코드
    private String itemCode;

    // 상품 수량
    private Integer quantity;

    // 결제 준비 요청 시각
    private LocalDateTime createdAt;

    // 결제 승인 시각
    private LocalDateTime approvedAt;

    // 결제 승인 요청에 대해 저장한 값, 요청 시 전달된 내용
    private String payload;

    public Integer getTotalAmount() {
        return amount.getTotal();
    }
}
