package com.letmeclean.model.issuedticket;

import com.letmeclean.dto.issuedticket.response.IssuedTicketResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IssuedTicketRepository extends JpaRepository<IssuedTicket, Long> {

    @Query(value = "select new com.letmeclean.dto.issuedticket.response.IssuedTicketResponse(m.email, t.name, t.description, it.createdAt) " +
            "from IssuedTicket it " +
            "join it.member m " +
            "join it.ticket t " +
            "where m.email = :email",
        countQuery = "select count(it) from IssuedTicket it")
    Page<IssuedTicketResponse> findIssuedTicketsByMember_Email(@Param("email") String email, Pageable pageable);
}
