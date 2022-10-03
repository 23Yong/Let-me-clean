package com.letmeclean.model.isseudcoupon;

import com.letmeclean.dto.issuedcoupon.IssuedCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {

    @Query(value = "select new com.letmeclean.dto.issuedcoupon.IssuedCouponResponse(ic.issuedCouponStatus, c.discountPrice, c.expiredAt) " +
            "from IssuedCoupon ic " +
            "join ic.member m " +
            "join ic.coupon c " +
            "where m.email = :email",
            countQuery = "select count(ic) from IssuedCoupon ic")
    Page<IssuedCouponResponse> findIssuedCouponsByMember_Email(@Param("email") String email, Pageable pageable);
}
