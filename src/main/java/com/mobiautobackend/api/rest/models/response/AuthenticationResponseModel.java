package com.mobiautobackend.api.rest.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "token"
})
public class AuthenticationResponseModel {
    @JsonProperty("token")
    private String token;

    public AuthenticationResponseModel() {
    }

    public AuthenticationResponseModel(String token) {
        this.token = token;
    }
}
