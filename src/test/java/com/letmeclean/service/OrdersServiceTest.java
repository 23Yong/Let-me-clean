package com.letmeclean.service;

import com.letmeclean.dto.order.OrdersDto;
import com.letmeclean.fixture.IssuedTicketEntityFixture;
import com.letmeclean.fixture.MemberEntityFixture;
import com.letmeclean.fixture.TicketEntityFixture;
import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.exception.LetMeCleanException;
import com.letmeclean.model.issuedticket.IssuedTicket;
import com.letmeclean.model.issuedticket.IssuedTicketRepository;
import com.letmeclean.model.issuedticket.IssuedTicketStatus;
import com.letmeclean.model.member.Member;
import com.letmeclean.model.member.MemberRepository;
import com.letmeclean.model.order.OrderState;
import com.letmeclean.model.order.Orders;
import com.letmeclean.model.order.OrdersRepository;
import com.letmeclean.model.ticket.Ticket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdersServiceTest {

    @Mock
    OrdersRepository ordersRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    IssuedTicketRepository issuedTicketRepository;

    @InjectMocks
    OrdersService ordersService;

    @DisplayName("멤버가 수거요청을 하면")
    @Nested
    class PickUpOrdersTest {

        @DisplayName("수거요청이 성공한다.")
        @Test
        void 수거요청_성공() {
            // given
            Member member = MemberEntityFixture.get();
            Ticket ticket = TicketEntityFixture.get();
            IssuedTicket issuedTicket = IssuedTicketEntityFixture.get(member, ticket);
            Orders order = Orders.of(OrderState.REQUESTED, "address", "additionalInfo", member, issuedTicket);
            OrdersDto ordersDto = createOrdersDto();

            given(memberRepository.findByEmail(ordersDto.email())).willReturn(Optional.of(member));
            given(issuedTicketRepository.findById(ordersDto.issuedTicketId())).willReturn(Optional.of(issuedTicket));
            given(ordersRepository.save(any(Orders.class))).willReturn(order);
                    
            // when
            ordersService.pickUpOrder(ordersDto);

            // then
            then(memberRepository).should().findByEmail(eq("23Yong@email.com"));
            then(issuedTicketRepository).should().findById(eq(2L));
            then(ordersRepository).should().save(any(Orders.class));
        }

        @DisplayName("이메일을 찾을 수 없어 실패한다.")
        @Test
        void 이메일을_찾을수없어_실패한다() {
            // given
            Member member = MemberEntityFixture.get();
            Ticket ticket = TicketEntityFixture.get();
            OrdersDto ordersDto = createOrdersDto();

            given(memberRepository.findByEmail(ordersDto.email())).willReturn(Optional.empty());

            // when & then
            LetMeCleanException e = Assertions.assertThrows(LetMeCleanException.class, () -> ordersService.pickUpOrder(ordersDto));

            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
            then(memberRepository).should().findByEmail(ordersDto.email());
            then(issuedTicketRepository).shouldHaveNoInteractions();
            then(ordersRepository).shouldHaveNoInteractions();
        }

        @DisplayName("발급된 티켓을 찾을 수 없어 실패한다.")
        @Test
        void 발급된_티켓을_찾을수없어_실패한다() {
            // given
            Member member = MemberEntityFixture.get();
            OrdersDto ordersDto = createOrdersDto();

            given(memberRepository.findByEmail(ordersDto.email())).willReturn(Optional.of(member));
            given(issuedTicketRepository.findById(ordersDto.issuedTicketId())).willReturn(Optional.empty());

            // when & then
            LetMeCleanException e = Assertions.assertThrows(LetMeCleanException.class, () -> ordersService.pickUpOrder(ordersDto));

            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ISSUED_TICKET_NOT_FOUND);
            then(memberRepository).should().findByEmail(ordersDto.email());
            then(issuedTicketRepository).should().findById(ordersDto.issuedTicketId());
            then(ordersRepository).shouldHaveNoInteractions();
        }

        @DisplayName("이미 사용한 티켓이라 실패한다.")
        @Test
        void 이미_사용한_티켓이라_실패한다() {
            // given
            Member member = MemberEntityFixture.get();
            Ticket ticket = TicketEntityFixture.get();
            IssuedTicket issuedTicket = IssuedTicket.of(IssuedTicketStatus.TICKET_USED, member, ticket);
            Orders order = Orders.of(OrderState.REQUESTED, "address", "additionalInfo", member, issuedTicket);
            OrdersDto ordersDto = createOrdersDto();

            given(memberRepository.findByEmail(ordersDto.email())).willReturn(Optional.of(member));
            given(issuedTicketRepository.findById(ordersDto.issuedTicketId())).willReturn(Optional.of(issuedTicket));

            // when & then
            LetMeCleanException e = Assertions.assertThrows(LetMeCleanException.class, () -> ordersService.pickUpOrder(ordersDto));

            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ALREADY_USED_TICKET);
            then(memberRepository).should().findByEmail(ordersDto.email());
            then(issuedTicketRepository).should().findById(ordersDto.issuedTicketId());
            then(ordersRepository).shouldHaveNoInteractions();
        }
    }

    private OrdersDto createOrdersDto() {
        return OrdersDto.of(
                1L,
                OrderState.REQUESTED,
                "address",
                "additionalInfo",
                "23Yong@email.com",
                2L
        );
    }
}
