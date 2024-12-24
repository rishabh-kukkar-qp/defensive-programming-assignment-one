package com.dpp.subscriptionupgrade.model.response;


import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuccessFailureResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("transaction_id")
    private String transactionId;
    @SerializedName("error")
    private String error;
}
