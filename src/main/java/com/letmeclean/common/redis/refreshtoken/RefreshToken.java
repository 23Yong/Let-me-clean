package com.letmeclean.common.redis.refreshtoken;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;

@Getter
@NoArgsConstructor
@RedisHash("RefreshToken")
public class RefreshToken {

    @Id
    @GeneratedValue
    @Column(name = "refresh_token_id")
    private String id;

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
