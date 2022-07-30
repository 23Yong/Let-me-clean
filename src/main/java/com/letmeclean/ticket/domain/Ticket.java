package com.letmeclean.ticket.domain;

import com.letmeclean.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Ticket extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "ticket_id")
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @Min(0)
    private Integer price;

    private String description;

    @Builder
    public Ticket(String name, Integer price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
