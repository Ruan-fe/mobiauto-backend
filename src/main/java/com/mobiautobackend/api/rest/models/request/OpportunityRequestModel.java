package com.mobiautobackend.api.rest.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OpportunityRequestModel {

    @NotNull
    @Valid
    @JsonProperty("customer")
    private Customer customer;

    @NotBlank
    @JsonProperty
    private String vehicleId;

    public Customer getCustomer() {
        return customer;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public static class Customer {

        @NotBlank
        @JsonProperty("name")
        private String name;

        @NotBlank
        @Email
        @JsonProperty("email")
        private String email;

        @NotBlank
        @JsonProperty("phone")
        private String phone;

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }
    }
}


