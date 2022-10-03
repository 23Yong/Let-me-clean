package com.letmeclean.model.member;

import com.letmeclean.model.AuditingFields;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(columnList = "email", unique = true)
})
@Entity
public class Member extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(nullable = false, length = 50)
    private String tel;

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeTel(String tel) {
        this.tel = tel;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    private Member(String email, String password, String name, String nickname, String tel) {
        this.email = email;
        this.password = password;
        this.username = name;
        this.nickname = nickname;
        this.tel = tel;
    }

    public static Member of(String email, String password, String username, String nickname, String tel) {
        return new Member(email, password, username, nickname, tel);
    }
}
