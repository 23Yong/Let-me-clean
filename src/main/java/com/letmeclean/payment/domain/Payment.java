package com.letmeclean.payment.domain;

import com.letmeclean.global.BaseTimeEntity;
import com.letmeclean.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Min(0)
    private Integer totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void completePay() {
        this.paymentStatus = PaymentStatus.TICKET_PAY_COMPLETED;
    }

    @Builder
    public Payment(PaymentStatus paymentStatus, Integer totalPrice, Member member) {
        this.paymentStatus = paymentStatus;
        this.totalPrice = totalPrice;
        this.member = member;
    }
}
