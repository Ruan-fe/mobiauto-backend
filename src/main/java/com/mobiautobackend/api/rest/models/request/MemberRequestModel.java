package com.mobiautobackend.api.rest.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberRequestModel {

    @NotBlank
    @JsonProperty("name")
    private String name;

    @NotBlank
    @Email
    @JsonProperty("email")
    private String email;

    @NotBlank
    @JsonProperty("password")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
