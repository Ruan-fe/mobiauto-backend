package com.mobiautobackend.api.rest.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobiautobackend.domain.enumeration.OpportunityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OpportunityAssignRequestModel {

    @NotBlank
    @JsonProperty("memberId")
    private String memberId;

    @NotBlank
    @JsonProperty("reason")
    private String reason;

    @NotNull
    @JsonProperty("status")
    private OpportunityStatus status;

    public @NotBlank String getMemberId() {
        return memberId;
    }

    public @NotBlank String getReason() {
        return reason;
    }

    public @NotNull OpportunityStatus getStatus() {
        return status;
    }
}


