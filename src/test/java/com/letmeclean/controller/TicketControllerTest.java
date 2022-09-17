package com.letmeclean.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letmeclean.dto.ticket.TicketDto;
import com.letmeclean.dto.ticket.request.TicketSaveRequest;
import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.exception.LetMeCleanException;
import com.letmeclean.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class TicketControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    TicketService ticketService;

    public TicketControllerTest(
            @Autowired MockMvc mockMvc,
            @Autowired ObjectMapper objectMapper
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void 티켓등록() throws Exception {
        TicketSaveRequest request = TicketSaveRequest.of("BASIC_TICKET", 100000, "쓰레기 수거함을 수거해가는 기본 티켓");

        mockMvc.perform(
                post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void 중복된_이름으로_티켓등록_실패할경우() throws Exception {
        TicketSaveRequest request = TicketSaveRequest.of("BASIC_TICKET", 100000, "쓰레기 수거함을 수거해가는 기본 티켓");
        doThrow(new LetMeCleanException(ErrorCode.DUPLICATE_TICKET_CONFLICT))
                .when(ticketService).register(any(TicketDto.class));

        mockMvc.perform(
                post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @WithAnonymousUser
    @Test
    void 권한이_없어_티켓등록_실패() throws Exception {
        TicketSaveRequest request = TicketSaveRequest.of("BASIC_TICKET", 100000, "쓰레기 수거함을 수거해가는 기본 티켓");

        mockMvc.perform(
                        post("/api/tickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
