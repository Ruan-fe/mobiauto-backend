package com.mobiautobackend.api.rest.assemblers;

import com.mobiautobackend.api.rest.controllers.DealershipController;
import com.mobiautobackend.api.rest.controllers.OpportunityController;
import com.mobiautobackend.api.rest.controllers.VehicleController;
import com.mobiautobackend.api.rest.models.request.OpportunityAssignRequestModel;
import com.mobiautobackend.api.rest.models.request.OpportunityRequestModel;
import com.mobiautobackend.api.rest.models.response.OpportunityResponseModel;
import com.mobiautobackend.domain.entities.Customer;
import com.mobiautobackend.domain.entities.Opportunity;
import com.mobiautobackend.domain.enumeration.OpportunityStatus;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OpportunityAssembler extends RepresentationModelAssemblerSupport<Opportunity, OpportunityResponseModel> {

    public OpportunityAssembler() {
        super(OpportunityController.class, OpportunityResponseModel.class);
    }

    @Override
    public OpportunityResponseModel toModel(Opportunity opportunity) {
        OpportunityResponseModel opportunityResponseModel = new OpportunityResponseModel(opportunity, opportunity.getCustomer());
        opportunityResponseModel.add(this.buildOpportunitySelfLink(opportunity));
        opportunityResponseModel.add(this.buildDealershipLink(opportunity.getDealershipId()));
        return opportunityResponseModel;
    }

    public Opportunity toEntity(OpportunityRequestModel opportunityRequestModel, String dealershipId, String vehicleId) {
        Opportunity opportunity = new Opportunity();
        opportunity.setDealershipId(dealershipId);
        opportunity.setStatus(OpportunityStatus.NEW);
        opportunity.setVehicleId(vehicleId);

        Customer customer = new Customer();
        customer.setName(opportunityRequestModel.getCustomer().getName());
        customer.setEmail(opportunityRequestModel.getCustomer().getEmail());
        customer.setPhone(opportunityRequestModel.getCustomer().getPhone());

        opportunity.setCustomer(customer);

        return opportunity;
    }

    public Link buildOpportunitySelfLink(Opportunity opportunity) {
        return linkTo(methodOn(OpportunityController.class).findById(opportunity.getId())).withSelfRel();
    }

    public Link buildVehicleLink(String vehicleId) {
        return linkTo(methodOn(VehicleController.class).findById(vehicleId)).withRel("vehicle");
    }

    public Link buildDealershipLink(String dealershipId) {
        return linkTo(methodOn(DealershipController.class).findById(dealershipId)).withRel("dealership");
    }

    public Opportunity toEntity(OpportunityAssignRequestModel assignRequestModel, Opportunity opportunityOld) {
        Opportunity opportunity = new Opportunity();
        opportunity.setId(opportunityOld.getId());
        opportunity.setCustomer(opportunityOld.getCustomer());
        opportunity.setVehicleId(opportunityOld.getVehicleId());
        opportunity.setDealershipId(opportunityOld.getDealershipId());
        opportunity.setAssignDate(opportunityOld.getAssignDate());
        opportunity.setCreationDate(opportunityOld.getCreationDate());
        opportunity.setStatus(assignRequestModel.getStatus());
        opportunity.setMemberId(assignRequestModel.getMemberId());
        opportunity.setReason(assignRequestModel.getReason());
        return opportunity;
    }
}
