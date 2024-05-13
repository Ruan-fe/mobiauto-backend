package com.mobiautobackend.api.rest.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mobiautobackend.domain.entities.Customer;
import com.mobiautobackend.domain.entities.Opportunity;
import com.mobiautobackend.domain.enumeration.OpportunityStatus;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.ZonedDateTime;

@JsonPropertyOrder({
        "id",
        "dealershipId",
        "vehicleId",
        "customer",
        "status",
        "memberId",
        "reason",
        "creationDate"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "opportunity", collectionRelation = "opportunities")
public class OpportunityResponseModel extends RepresentationModel<OpportunityResponseModel> {

    @JsonProperty("id")
    private String id;

    @JsonProperty("dealershipId")
    private String dealershipId;

    @JsonProperty("vehicleId")
    private String vehicleId;

    @JsonProperty("customer")
    private CustomerResponseModel customer;

    @JsonProperty("status")
    private OpportunityStatus status;

    @JsonProperty("memberId")
    private String memberId;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("creationDate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime creationDate;

    public OpportunityResponseModel() {
    }

    public OpportunityResponseModel(Opportunity opportunity, Customer customer) {
        this.id = opportunity.getId();
        this.dealershipId = opportunity.getDealershipId();
        this.vehicleId = opportunity.getVehicleId();
        this.customer = new CustomerResponseModel(customer);
        this.status = opportunity.getStatus();
        this.memberId = opportunity.getMemberId();
        this.reason = opportunity.getReason();
        this.creationDate = opportunity.getCreationDate();
    }

    private static class CustomerResponseModel {

        @JsonProperty("name")
        private String name;

        @JsonProperty("email")
        private String email;

        @JsonProperty("phone")
        private String phone;

        public CustomerResponseModel() {
        }

        public CustomerResponseModel(Customer customer) {
            this.name = customer.getName();
            this.email = customer.getEmail();
            this.phone = customer.getPhone();
        }
    }
}
