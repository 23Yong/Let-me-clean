package com.letmeclean.model.payment;

import com.letmeclean.model.AuditingFields;
import com.letmeclean.model.isseudcoupon.IssuedCoupon;
import com.letmeclean.model.member.Member;
import com.letmeclean.model.ticket.Ticket;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Payment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PaymentStatus paymentStatus;

    @Min(0)
    @Column(nullable = false)
    private Integer totalPrice;

    @Min(0)
    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_coupon_id")
    private IssuedCoupon issuedCoupon;

    private Payment(PaymentStatus paymentStatus, Integer totalPrice, Integer quantity, Member member, Ticket ticket) {
        this.paymentStatus = paymentStatus;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.member = member;
        this.ticket = ticket;
    }

    public static Payment of(PaymentStatus paymentStatus, Integer totalPrice, Integer quantity, Member member, Ticket ticket) {
        return new Payment(paymentStatus, totalPrice, quantity, member, ticket);
    }
}
