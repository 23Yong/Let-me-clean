package com.letmeclean.model.order;

import com.letmeclean.model.AuditingFields;
import com.letmeclean.model.issuedticket.IssuedTicket;
import com.letmeclean.model.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Orders extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    // TODO : 일반 String 타입이 아닌, Address 타입을 따로 만들어 저장
    @Column(nullable = false, length = 255)
    private String address;

    @Column(length = 255)
    private String additionalInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_ticket_id")
    private IssuedTicket issuedTicket;

    private Orders(OrderState orderState, String address, String additionalInfo, Member member, IssuedTicket issuedTicket) {
        this.orderState = orderState;
        this.address = address;
        this.additionalInfo = additionalInfo;
        this.member = member;
        this.issuedTicket = issuedTicket;
    }

    public static Orders of(OrderState orderState, String address, String additionalInfo, Member member, IssuedTicket issuedTicket) {
        return new Orders(
                orderState,
                address,
                additionalInfo,
                member,
                issuedTicket
        );
    }
}
