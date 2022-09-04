package com.letmeclean.model.ticket;

import com.letmeclean.model.AuditingFields;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Ticket extends AuditingFields {

    @Id
    @GeneratedValue
    @Column(name = "ticket_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Min(0)
    @Column(nullable = false)
    private Integer price;

    private String description;

    @Builder
    public Ticket(String name, Integer price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
