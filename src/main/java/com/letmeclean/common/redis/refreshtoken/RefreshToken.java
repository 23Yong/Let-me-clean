package com.letmeclean.common.redis.refreshtoken;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash("RT")
public class RefreshToken {

    @Id
    private String key;

    private String value;

    private Long expirationTime;

    @Builder
    public RefreshToken(String key, String value, Long expirationTime) {
        this.key = key;
        this.value = value;
        this.expirationTime = expirationTime;
    }

    public void updateValue(String value) {
        this.value = value;
    }
}
