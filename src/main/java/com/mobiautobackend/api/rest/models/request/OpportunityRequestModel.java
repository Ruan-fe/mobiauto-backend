package com.mobiautobackend.api.rest.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OpportunityRequestModel {

    @NotBlank
    @JsonProperty("dealershipId")
    private String dealershipId;

    @NotBlank
    @JsonProperty("vehicleId")
    private String vehicleId;

    @NotNull
    @JsonProperty("customer")
    private Customer customer;

    public String getDealershipId() {
        return dealershipId;
    }

    public void setDealershipId(String dealershipId) {
        this.dealershipId = dealershipId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    private static class Customer {

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


