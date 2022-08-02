package com.letmeclean.payment.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Amount {

    private Integer total;

    private Integer taxFree;

    private Integer vat;

    private Integer point;

    private Integer discount;

    private Integer greenDeposit;
}
