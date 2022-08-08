package com.letmeclean.issuedcoupon.service;

import com.letmeclean.coupon.domain.Coupon;
import com.letmeclean.coupon.service.CouponService;
import com.letmeclean.issuedcoupon.domain.IssuedCoupon;
import com.letmeclean.issuedcoupon.domain.IssuedCouponRepository;
import com.letmeclean.member.domain.Member;
import com.letmeclean.member.service.MemberService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.letmeclean.issuedcoupon.dto.request.IssuedCouponRequest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CouponIssueServiceTest {

    @Mock
    IssuedCouponRepository issuedCouponRepository;
    @Mock
    MemberService memberService;
    @Mock
    CouponService couponService;

    @InjectMocks
    CouponIssueService couponIssueService;

    Member member;
    Coupon coupon;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("23Yong@test.com")
                .password("password")
                .name("홍길동")
                .nickname("ImGilDong")
                .tel("010-1234-1234")
                .build();
        coupon = Coupon.builder()
                .name("회원가입 쿠폰")
                .pointAmount(10000)
                .expiredAt(LocalDateTime.now().plusDays(14))
                .build();
    }

    @DisplayName("회원이 쿠폰을 발급받으면")
    @Nested
    class IssueCouponTest {

        @DisplayName("쿠폰 발급에 성공한다.")
        @Test
        void issueCouponSuccess() {
            // given
            String email = "23Yong@test.com";
            Long couponId = 3L;
            IssuedCouponRequestDto issuedCouponRequestDto = new IssuedCouponRequestDto(email, couponId);

            when(memberService.findMember(email)).thenReturn(member);
            when(couponService.findCoupon(couponId)).thenReturn(coupon);

            // when
            couponIssueService.issueCouponToMember(issuedCouponRequestDto);

            // then
            then(memberService).should().findMember(email);
            then(couponService).should().findCoupon(couponId);
            then(issuedCouponRepository).should().save(any(IssuedCoupon.class));
            Assertions.assertAll(
                    () -> assertThat(member.getIssuedCoupons()).hasSize(1),
                    () -> assertThat(member.getIssuedCoupons().get(0).getMember()).isEqualTo(member)
            );
        }
    }
}