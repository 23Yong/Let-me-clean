package com.letmeclean.payment.domain;

import com.letmeclean.global.BaseTimeEntity;
import com.letmeclean.member.domain.Member;
import com.letmeclean.ticket.domain.Ticket;
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

    @Min(0)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public void linkMember(Member member) {
        this.member = member;
    }

    @Builder
    public Payment(PaymentStatus paymentStatus, Integer totalPrice, Integer quantity, Member member, Ticket ticket) {
        this.paymentStatus = paymentStatus;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.member = member;
        this.ticket = ticket;
    }
}
