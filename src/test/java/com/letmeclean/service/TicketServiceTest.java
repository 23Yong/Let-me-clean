package com.letmeclean.service;

import com.letmeclean.dto.ticket.TicketDto;
import com.letmeclean.dto.ticket.TicketSoldDto;
import com.letmeclean.fixture.MemberEntityFixture;
import com.letmeclean.fixture.TicketEntityFixture;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    TicketRepository ticketRepository;
    @Mock
    IssuedTicketRepository issuedTicketRepository;
    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    TicketService ticketService;

    @DisplayName("운영자가 티켓을 등록하면 ")
    @Nested
    class TicketRegisterTest {

        @DisplayName("티켓 등록에 성공한다.")
        @Test
        void 티켓등록_성공() {
            // given
            TicketDto ticketDto = TicketDto.of(1L, "BASIC_TICKET", 10000, "수거함을 수거해가는 기본 티켓");
            Ticket ticketEntity = ticketDto.toEntity();

            given(ticketRepository.existsByName(anyString())).willReturn(false);
            given(ticketRepository.save(any(Ticket.class))).willReturn(ticketEntity);

            // when
            ticketService.register(ticketDto);

            // then
            then(ticketRepository).should().existsByName("BASIC_TICKET");
            then(ticketRepository).should().save(any(Ticket.class));
        }

        @DisplayName("중복된 티켓이름으로 인해 티켓 등록에 실패한다.")
        @Test
        void 중복된_티켓이름으로_티켓등록_실패() {
            // given
            TicketDto ticketDto = TicketDto.of(1L, "BASIC_TICKET", 10000, "수거함을 수거해가는 기본 티켓");
            given(ticketRepository.existsByName(anyString())).willReturn(true);

            // when & then
            LetMeCleanException e = Assertions.assertThrows(LetMeCleanException.class, () -> ticketService.register(ticketDto));

            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_TICKET_CONFLICT);
            then(ticketRepository).should().existsByName(anyString());
            then(ticketRepository).should(times(0)).save(any(Ticket.class));
        }
    }

    @DisplayName("구매한 모든 티켓이 판매되면")
    @Nested
    class TicketAllSoldTest {

        @DisplayName("구매한 모든 티켓이 발급된다.")
        @Test
        void 티켓판매_성공() {
            // given
            TicketSoldDto ticketSoldDto = createTicketSoldDto();
            Member member = MemberEntityFixture.get();
            Ticket ticket = TicketEntityFixture.get();
            List<IssuedTicket> issuedTickets = List.of(IssuedTicket.of(IssuedTicketStatus.TICKET_NOT_USED, member, ticket),
                    IssuedTicket.of(IssuedTicketStatus.TICKET_NOT_USED, member, ticket),
                    IssuedTicket.of(IssuedTicketStatus.TICKET_NOT_USED, member, ticket),
                    IssuedTicket.of(IssuedTicketStatus.TICKET_NOT_USED, member, ticket));
            Payment payment = Payment.of(PaymentStatus.TICKET_PAY_COMPLETED, ticketSoldDto.totalPrice(), ticketSoldDto.quantity(), member, ticket);

            given(memberRepository.findByEmail(member.getEmail())).willReturn(Optional.of(member));
            given(ticketRepository.findById(anyLong())).willReturn(Optional.of(ticket));
            given(issuedTicketRepository.saveAll(any())).willReturn(issuedTickets);
            given(paymentRepository.save(any(Payment.class))).willReturn(payment);

            // when
            ticketService.soldAllTickets(ticketSoldDto);

            // then
            then(memberRepository).should().findByEmail(member.getEmail());
            then(ticketRepository).should().findById(1L);
            then(issuedTicketRepository).should().saveAll(any());
        }

        @DisplayName("회원정보가 없어 실패한다.")
        @Test
        void 회원정보를_찾을수_없어_실패() {
            // given
            TicketSoldDto ticketSoldDto = createTicketSoldDto();

            given(memberRepository.findByEmail(anyString())).willReturn(Optional.empty());

            // when & then
            LetMeCleanException e = Assertions.assertThrows(LetMeCleanException.class, () -> ticketService.soldAllTickets(ticketSoldDto));

            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);

            then(memberRepository).should().findByEmail(anyString());
            then(ticketRepository).shouldHaveNoInteractions();
            then(issuedTicketRepository).shouldHaveNoInteractions();
        }

        @DisplayName("티켓 정보가 없어 실패한다.")
        @Test
        void 티켓정보를_찾을수_없어_실패() {
            // given
            TicketSoldDto ticketSoldDto = createTicketSoldDto();
            Member member = MemberEntityFixture.get();

            given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
            given(ticketRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            LetMeCleanException e = Assertions.assertThrows(LetMeCleanException.class, () -> ticketService.soldAllTickets(ticketSoldDto));

            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.TICKET_NOT_FOUND);

            then(memberRepository).should().findByEmail(anyString());
            then(ticketRepository).should().findById(anyLong());
            then(issuedTicketRepository).shouldHaveNoInteractions();
        }
    }

    private TicketSoldDto createTicketSoldDto() {
        return TicketSoldDto.of(
                "23Yong@email.com",
                4,
                40000,
                1L
        );
    }
}