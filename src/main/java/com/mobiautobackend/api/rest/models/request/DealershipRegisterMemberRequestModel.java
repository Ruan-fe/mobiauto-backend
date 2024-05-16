package com.mobiautobackend.api.rest.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobiautobackend.domain.enumeration.MemberRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DealershipRegisterMemberRequestModel {
    @NotBlank
    @JsonProperty("memberId")
    private String memberId;

    @NotNull
    @JsonProperty("role")
    private MemberRole role;

    public String getMemberId() {
        return memberId;
    }

    public MemberRole getRole() {
        return role;
    }
}
