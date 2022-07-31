package com.letmeclean.ticket.service;

import com.letmeclean.global.exception.AppException;
import com.letmeclean.ticket.domain.Ticket;
import com.letmeclean.ticket.domain.TicketRepository;
import com.letmeclean.ticket.dto.request.TicketRequest;
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
        void registerSuccess() {
            // given
            TicketRequest.TicketSaveRequestDto ticketSaveRequestDto =
                    new TicketRequest.TicketSaveRequestDto("일반 티켓", 10000, "기본 수거함을 수거해가는 티켓입니다.");
            Ticket ticket = ticketSaveRequestDto.toEntity();

            when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

            // when
            ticketService.register(ticketSaveRequestDto);

            // then
            then(ticketRepository).should().existsByName("일반 티켓");
            then(ticketRepository).should().save(any(Ticket.class));
        }

        @DisplayName("중복된 티켓이름으로 인해 티켓 등록에 실패한다.")
        @Test
        void registerFailedDuplicatedName() {
            // given
            TicketRequest.TicketSaveRequestDto ticketSaveRequestDto =
                    new TicketRequest.TicketSaveRequestDto("일반 티켓", 10000, "기본 수거함을 수거해가는 티켓입니다.");

            when(ticketRepository.existsByName("일반 티켓")).thenReturn(true);

            // when, then
            assertThatThrownBy(() -> ticketService.register(ticketSaveRequestDto))
                    .isInstanceOf(AppException.class);
        }
    }
}