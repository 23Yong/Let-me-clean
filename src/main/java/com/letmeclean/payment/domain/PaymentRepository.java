package com.letmeclean.payment.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "select p from Payment p inner join fetch p.member m where m.email = :email",
        countQuery = "select count(p) from Payment p inner join p.member m where m.email = :email")
    List<Payment> findByEmail(@Param("email") String email, Pageable pageable);
}
