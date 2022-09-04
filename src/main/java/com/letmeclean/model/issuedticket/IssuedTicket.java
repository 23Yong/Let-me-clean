package com.letmeclean.model.issuedticket;

import com.letmeclean.model.AuditingFields;
import com.letmeclean.model.member.Member;
import com.letmeclean.model.ticket.Ticket;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class IssuedTicket extends AuditingFields {

    @Id
    @GeneratedValue
    @Column(name = "issued_ticket_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private IssuedTicketStatus issuedTicketStatus;

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
    public IssuedTicket(IssuedTicketStatus issuedTicketStatus, Member member, Ticket ticket) {
        this.issuedTicketStatus = issuedTicketStatus;
        this.member = member;
        this.ticket = ticket;
    }
}