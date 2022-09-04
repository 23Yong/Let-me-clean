package com.letmeclean.model.payment;

import com.letmeclean.dto.payment.response.PaymentDetailResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "select new com.letmeclean.payment.dto.response.PaymentDetailResponse(m.email, p.totalPrice, p.quantity, t.name, p.paymentStatus, p.createdAt) " +
            "from Payment p " +
            "join p.member m " +
            "join p.ticket t " +
            "where m.email = :email",
        countQuery = "select count(p) from Payment p")
    List<PaymentDetailResponse> findPaymentsByMemberEmail(@Param("email") String email, Pageable pageable);
}
