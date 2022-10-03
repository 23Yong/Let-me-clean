package com.letmeclean.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letmeclean.dto.member.MemberDto;
import com.letmeclean.dto.member.request.MemberModifyRequest;
import com.letmeclean.dto.member.request.PasswordModifyRequest;
import com.letmeclean.dto.member.request.SignUpRequest;
import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.exception.LetMeCleanException;
import com.letmeclean.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    public MemberControllerTest(
            @Autowired MockMvc mockMvc,
            @Autowired ObjectMapper objectMapper
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void 회원가입_성공() throws Exception {
        SignUpRequest request = createSignUpRequest();

        mockMvc.perform(
                post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 중복된_이메일로_회원가입_실패() throws Exception {
        SignUpRequest request = createSignUpRequest();
        doThrow(new LetMeCleanException(ErrorCode.DUPLICATE_EMAIL_CONFLICT))
                .when(memberService).signUp(request.toDto());

        mockMvc.perform(
                post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void 중복된_닉네임으로_회원가입_실패() throws Exception {
        SignUpRequest request = createSignUpRequest();
        doThrow(new LetMeCleanException(ErrorCode.DUPLICATE_NICKNAME_CONFLICT))
                .when(memberService).signUp(request.toDto());

        mockMvc.perform(
                        post("/api/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @WithMockUser(roles = {"MEMBER"})
    @Test
    void 회원정보_수정_성공() throws Exception {
        MemberModifyRequest request = createMemberModifyRequest();
        given(memberService.modifyMemberInfo(any(MemberDto.class)))
                .willReturn(request.toDto("23Yong@email.com"));

        mockMvc.perform(
                put("/api/members/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"MEMBER"})
    @Test
    void 회원정보를_찾을수없어_수정_실패() throws Exception {
        MemberModifyRequest request = createMemberModifyRequest();
        doThrow(new LetMeCleanException(ErrorCode.MEMBER_NOT_FOUND)).when(memberService).modifyMemberInfo(any(MemberDto.class));

        mockMvc.perform(
                        put("/api/members/info")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = {"MEMBER"})
    @Test
    void 회원비밀번호_수정_성공() throws Exception {
        PasswordModifyRequest request = createPasswordModifyRequest();
        MemberDto memberDto = createMemberDto();
        String prevPassword = request.prevPassword();
        String newPassword = request.newPassword();
        given(memberService.modifyPassword(anyString(), eq(prevPassword), eq(newPassword)))
                .willReturn(memberDto);

        mockMvc.perform(
                        put("/api/members/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "MEMBER")
    @Test
    void 회원정보를_찾을수없어_비밀번호_수정_실패() throws Exception {
        PasswordModifyRequest request = createPasswordModifyRequest();
        doThrow(new LetMeCleanException(ErrorCode.MEMBER_NOT_FOUND))
                .when(memberService).modifyPassword(anyString(), anyString(), anyString());

        mockMvc.perform(
                        put("/api/members/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = "MEMBER", username = "23Yong@email.com")
    @Test
    void 보유쿠폰_조회_성공() throws Exception {
        mockMvc.perform(
                get("/api/members/coupons")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void 권한이_없어_보유쿠폰_조회_실패() throws Exception {
        mockMvc.perform(
                get("/api/members/coupons")
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = "MEMBER", username = "23Yong@email.com")
    @Test
    void 이메일을_찾을수없어_실패() throws Exception {
        doThrow(new LetMeCleanException(ErrorCode.MEMBER_NOT_FOUND))
                .when(memberService).getHoldingCoupons(eq("23Yong@email.com"), any(Pageable.class));

        mockMvc.perform(
                get("/api/members/coupons")
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private MemberDto createMemberDto() {
        return MemberDto.of(
                1L,
                "23Yong@email.com",
                "thisIsTestPw123!@",
                "23Yong",
                "Nickname",
                "010-1234-1234"
        );
    }

    private SignUpRequest createSignUpRequest() {
        String email = "23Yong@test.com";
        String password = "password";
        String username = "23Yong";
        String nickname = "Nickname";
        String tel = "010-1234-1234";
        return SignUpRequest.of(email, password, username, nickname, tel);
    }

    private MemberModifyRequest createMemberModifyRequest() {
        String username = "23Yong";
        String nickname = "Nickname";
        String tel = "010-1234-1234";
        return MemberModifyRequest.of(username, nickname, tel);
    }

    private PasswordModifyRequest createPasswordModifyRequest() {
        return new PasswordModifyRequest("23Yong@email.com", "prevPassword", "newPassword");
    }
}