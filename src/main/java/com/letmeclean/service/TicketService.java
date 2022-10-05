package com.letmeclean.service;

import com.letmeclean.dto.ticket.TicketDto;
import com.letmeclean.dto.ticket.TicketSoldDto;
import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.exception.LetMeCleanException;
import com.letmeclean.model.issuedticket.IssuedTicket;
import com.letmeclean.model.issuedticket.IssuedTicketRepository;
import com.letmeclean.model.issuedticket.IssuedTicketStatus;
import com.letmeclean.model.member.Member;
import com.letmeclean.model.member.MemberRepository;
import com.letmeclean.model.payment.Payment;
import com.letmeclean.model.payment.PaymentRepository;
import com.letmeclean.model.payment.PaymentStatus;
import com.letmeclean.model.ticket.Ticket;
import com.letmeclean.model.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TicketService {

    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final IssuedTicketRepository issuedTicketRepository;
    private final PaymentRepository paymentRepository;

    public Ticket findTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new LetMeCleanException(ErrorCode.TICKET_NOT_FOUND, String.format("%s 을(를) 찾을 수 없습니다.", ticketId)));
    }

    @Transactional
    public void register(TicketDto ticketDto) {
        if (ticketRepository.existsByName(ticketDto.name())) {
            throw new LetMeCleanException(ErrorCode.DUPLICATE_TICKET_CONFLICT, String.format("%s 은(는) 이미 존재하는 이름입니다.", ticketDto.name()));
        }

        ticketRepository.save(ticketDto.toEntity());
    }

    @Transactional
    public void soldAllTickets(TicketSoldDto ticketSoldDto) {
        Member member = memberRepository.findByEmail(ticketSoldDto.email())
                .orElseThrow(() -> new LetMeCleanException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s 을(를) 찾을 수 없습니다.", ticketSoldDto.email())));
        Ticket ticket = findTicket(ticketSoldDto.ticketId());

        issueAllTickets(member, ticket, ticketSoldDto.quantity());

        paymentRepository.save(Payment.of(PaymentStatus.TICKET_PAY_COMPLETED, ticketSoldDto.totalPrice(), ticketSoldDto.quantity(), member, ticket));
    }

    private void issueAllTickets(Member member, Ticket ticket, Integer quantity) {
        List<IssuedTicket> issuedTickets = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            issuedTickets.add(IssuedTicket.of(IssuedTicketStatus.TICKET_NOT_USED, member, ticket));
        }

        issuedTicketRepository.saveAll(issuedTickets);
    }
}
