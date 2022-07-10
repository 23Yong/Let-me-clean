package com.letmeclean.domain.refreshtoken;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken {

    @Id
    private Long key;

    private String value;

    @Builder
    public RefreshToken(Long key, String value) {
        this.key = key;
        this.value = value;
    }

    public void updateValue(String value) {
        this.value = value;
    }
}
