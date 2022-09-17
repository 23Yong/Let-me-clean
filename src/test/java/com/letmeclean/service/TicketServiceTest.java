package com.letmeclean.service;

import com.letmeclean.dto.ticket.TicketDto;
import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.exception.LetMeCleanException;
import com.letmeclean.model.ticket.Ticket;
import com.letmeclean.model.ticket.TicketRepository;
import com.letmeclean.dto.ticket.request.TicketRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    TicketRepository ticketRepository;

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
}