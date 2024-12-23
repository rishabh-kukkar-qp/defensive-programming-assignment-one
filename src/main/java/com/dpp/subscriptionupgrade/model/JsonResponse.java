package com.dpp.subscriptionupgrade.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonResponse {
    private Object data;
    private String successCode;
    private String errorCode;
    private String errorType;
    private List<String> errorDetails;


}
