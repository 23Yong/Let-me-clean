package com.letmeclean.domain.member;

import com.letmeclean.domain.BaseTimeEntity;
import com.letmeclean.security.roles.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
@DynamicInsert
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String email;

    @Size(min = 8, max = 64)
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

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String email, String password, String name, String nickname, String tel, Role role) {
        this.email = email;
        this.password = password;
        this.username = name;
        this.nickname = nickname;
        this.tel = tel;
        this.role = role;
    }
}
