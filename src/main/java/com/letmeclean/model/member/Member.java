package com.letmeclean.model.member;

import com.letmeclean.model.AuditingFields;
import com.letmeclean.model.issuedticket.IssuedTicket;
import com.letmeclean.model.payment.Payment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends AuditingFields {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(nullable = false, length = 50)
    private String tel;

    @OneToMany(mappedBy = "member")
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<IssuedTicket> issuedTickets = new ArrayList<>();

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeTel(String tel) {
        this.tel = tel;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void addPayment(Payment payment) {
        payment.linkMember(this);
        payments.add(payment);
    }

    public void addIssuedTicket(IssuedTicket issuedTicket) {
        issuedTicket.linkMember(this);
        issuedTickets.add(issuedTicket);
    }

    @Builder
    public Member(String email, String password, String name, String nickname, String tel) {
        this.email = email;
        this.password = password;
        this.username = name;
        this.nickname = nickname;
        this.tel = tel;
    }
}
