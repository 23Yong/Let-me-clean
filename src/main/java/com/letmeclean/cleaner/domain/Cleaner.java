package com.letmeclean.cleaner.domain;

import com.letmeclean.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Cleaner extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "cleaner_id")
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String email;

    @Size(min = 8, max = 100)
    private String password;

    @Size(max = 45)
    private String username;

    @Size(max = 45)
    private String tel;

    @Builder
    public Cleaner(String email, String password, String username, String tel) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.tel = tel;
    }
}
