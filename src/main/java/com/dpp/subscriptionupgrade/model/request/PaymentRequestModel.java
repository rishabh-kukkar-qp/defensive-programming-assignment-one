package com.dpp.subscriptionupgrade.model.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentRequestModel {
    private String name;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private Double amount;
}
