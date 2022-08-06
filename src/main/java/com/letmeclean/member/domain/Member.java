package com.letmeclean.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.letmeclean.global.BaseTimeEntity;
import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.issuedticket.domain.IssuedTicket;
import com.letmeclean.payment.domain.Payment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String email;

    @Size(min = 8, max = 100)
    private String password;

    @Size(max = 45)
    private String username;

    @Size(max = 45)
    @Column(unique = true)
    private String nickname;

    @Size(max = 45)
    private String tel;

    @Min(0)
    private long point;

    @Size(max = 45)
    private String addressCode;

    @Size(max = 45)
    private String addressDetail;

    @Column(columnDefinition = "varchar(45) default 'DEFAULT'")
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Payment> payments = new ArrayList<>();

    @JsonIgnore
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
