package com.letmeclean.model.payment;

import com.letmeclean.dto.payment.response.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "select new com.letmeclean.dto.payment.response.PaymentResponse(m.email, p.totalPrice, p.quantity, t.name, p.paymentStatus, p.createdAt) " +
            "from Payment p " +
            "join p.member m " +
            "join p.ticket t " +
            "where m.email = :email",
        countQuery = "select count(p) from Payment p")
    Page<PaymentResponse> findPaymentsByMember_Email(@Param("email") String email, Pageable pageable);
}
