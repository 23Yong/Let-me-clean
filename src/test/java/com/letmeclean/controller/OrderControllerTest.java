package com.letmeclean.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letmeclean.dto.order.request.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class OrderControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public OrderControllerTest(
            @Autowired MockMvc mockMvc,
            @Autowired ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @WithMockUser(roles = "MEMBER")
    @Test
    void 수거요청_성공() throws Exception {
        mockMvc.perform(
                post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new OrderRequest("23Yong@email.com", 1L)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void 권한이_없어_수거요청_실패() throws Exception {
        mockMvc.perform(
                post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new OrderRequest("23Yong@email.com", 1L)))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
