package com.letmeclean.global.redis.refreshtoken;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("RefreshToken")
public class RefreshToken {

    @Id
    @GeneratedValue
    @Column(name = "refresh_token_id")
    private Long id;

    @Indexed
    private String email;
    private String value;

    @Builder
    public RefreshToken(String email, String value) {
        this.email = email;
        this.value = value;
    }

    public void updateValue(String value) {
        this.value = value;
    }
}
