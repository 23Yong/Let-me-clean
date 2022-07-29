package com.letmeclean.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.letmeclean.global.BaseTimeEntity;
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

    @Builder
    public Member(String email, String password, String name, String nickname, String tel) {
        this.email = email;
        this.password = password;
        this.username = name;
        this.nickname = nickname;
        this.tel = tel;
    }
}