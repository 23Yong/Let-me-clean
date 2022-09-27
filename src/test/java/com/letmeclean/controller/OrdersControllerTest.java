package com.letmeclean.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letmeclean.dto.order.OrdersDto;
import com.letmeclean.dto.order.request.OrdersRequest;
import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.exception.LetMeCleanException;
import com.letmeclean.service.OrdersService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class OrdersControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    OrdersService ordersService;

    public OrdersControllerTest(
            @Autowired MockMvc mockMvc,
            @Autowired ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @WithMockUser(roles = "MEMBER")
    @Test
    void 수거요청_성공() throws Exception {
        OrdersRequest request = createOrdersRequest();
        willDoNothing().given(ordersService).pickUpOrder(any(OrdersDto.class));

        mockMvc.perform(
                post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void 권한이_없어_수거요청_실패() throws Exception {
        OrdersRequest request = createOrdersRequest();

        mockMvc.perform(
                post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = "MEMBER", username = "23Yong@email.com")
    @Test
    void 회원정보가_없어_실패() throws Exception {
        OrdersRequest request = createOrdersRequest();

        doThrow(new LetMeCleanException(ErrorCode.MEMBER_NOT_FOUND))
                .when(ordersService).pickUpOrder(request.toDto("23Yong@email.com"));

        mockMvc.perform(
                post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = "MEMBER", username = "23Yong@email.com")
    @Test
    void 발급된_티켓정보가_없어_실패() throws Exception {
        OrdersRequest request = createOrdersRequest();

        doThrow(new LetMeCleanException(ErrorCode.ISSUED_TICKET_NOT_FOUND))
                .when(ordersService).pickUpOrder(request.toDto("23Yong@email.com"));

        mockMvc.perform(
                        post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = "MEMBER", username = "23Yong@email.com")
    @Test
    void 이미_사용된_티켓이라_실패() throws Exception {
        OrdersRequest request = createOrdersRequest();

        doThrow(new LetMeCleanException(ErrorCode.ALREADY_USED_TICKET))
                .when(ordersService).pickUpOrder(request.toDto("23Yong@email.com"));

        mockMvc.perform(
                        post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private OrdersRequest createOrdersRequest() {
        return new OrdersRequest("address", "additionalInfo", 1L);
    }
}
