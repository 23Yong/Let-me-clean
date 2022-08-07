package com.letmeclean.coupon.service;

import com.letmeclean.coupon.domain.Coupon;
import com.letmeclean.coupon.domain.CouponRepository;
import com.letmeclean.global.exception.AppException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static com.letmeclean.coupon.dto.request.CouponRequest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    CouponRepository couponRepository;

    @InjectMocks
    CouponService couponService;

    static ValidatorFactory validatorFactory;
    static Validator validator;

    @BeforeAll
    static void setValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @DisplayName("운영자가 쿠폰 등록을 시도하면")
    @Nested
    class CouponRegisterTest {

        @DisplayName("쿠폰 등록에 성공한다.")
        @Test
        void registerSuccess() {
            // given
            CouponRegisterRequestDto couponRegisterRequestDto = new CouponRegisterRequestDto(
                    "회원가입 쿠폰",
                    10000,
                    LocalDateTime.now().plusDays(14)
            );

            when(couponRepository.save(any(Coupon.class))).thenReturn(any(Coupon.class));

            // when
            couponService.registerCoupon(couponRegisterRequestDto);

            // then
            then(couponRepository).should().save(any(Coupon.class));
        }

        @DisplayName("쿠폰 만료기간이 현재 시간보다 빨라 등록에 실패한다.")
        @Test
        void registerFailBeforeCreatedAt() {
            // given
            CouponRegisterRequestDto couponRegisterRequestDto = new CouponRegisterRequestDto(
                    "회원가입 쿠폰",
                    10000,
                    LocalDateTime.now().minusDays(1)
            );

            // when, then
            Assertions.assertThatThrownBy(() -> couponService.registerCoupon(couponRegisterRequestDto))
                            .isInstanceOf(AppException.class);
        }

        @DisplayName("쿠폰 등록 형식을 지키지 않아 실패한다.")
        @Test
        void registerFailValidation() {
            // given
            CouponRegisterRequestDto couponRegisterRequestDto = new CouponRegisterRequestDto(
                    " ",
                    null,
                    null
            );

            Set<ConstraintViolation<CouponRegisterRequestDto>> constraintViolations = validator.validate(couponRegisterRequestDto);

            // when, then
            assertThat(constraintViolations.size()).isEqualTo(3);
        }
    }
}