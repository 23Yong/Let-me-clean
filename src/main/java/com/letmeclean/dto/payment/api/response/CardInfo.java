package com.letmeclean.dto.payment.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardInfo {

    private String purchaseCorp;

    private String purchaseCorpCode;

    private String issuerCorp;

    private String issuerCorpCode;

    private String kakaopayPurchaseCorp;

    private String kakaopayPurchaseCorpCode;

    private String kakaopayIssuerCorp;

    private String kakaopayIssuerCorpCode;

    private String bin;

    private String cardType;

    private String installMonth;

    private String approvedId;

    private String cardMid;

    private String interestFreeInstall;

    private String cardItemCode;
}
