package com.letmeclean.service;

import com.letmeclean.dto.order.OrdersDto;
import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.exception.LetMeCleanException;
import com.letmeclean.model.issuedticket.IssuedTicket;
import com.letmeclean.model.issuedticket.IssuedTicketRepository;
import com.letmeclean.model.member.Member;
import com.letmeclean.model.member.MemberRepository;
import com.letmeclean.model.order.Orders;
import com.letmeclean.model.order.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;
    private final IssuedTicketRepository issuedTicketRepository;

    @Transactional
    public void pickUpOrder(OrdersDto ordersDto) {
        String email = ordersDto.email();
        Long issuedTicketId = ordersDto.issuedTicketId();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new LetMeCleanException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s 를(을) 찾을 수 없습니다.", email)));
        IssuedTicket issuedTicket = issuedTicketRepository.findById(issuedTicketId)
                .orElseThrow(() -> new LetMeCleanException(ErrorCode.ISSUED_TICKET_NOT_FOUND, String.format("%s 를(을) 찾을 수 없습니다.", issuedTicketId)));
        if (issuedTicket.isUsedTicket()) {
            throw new LetMeCleanException(ErrorCode.ALREADY_USED_TICKET, String.format("%s 는(은) 이미 사용한 티켓입니다.", issuedTicketId));
        }

        Orders order = Orders.of(
                ordersDto.orderState(),
                ordersDto.address(),
                ordersDto.additionalInfo(),
                member,
                issuedTicket
        );
        ordersRepository.save(order);
    }
}
