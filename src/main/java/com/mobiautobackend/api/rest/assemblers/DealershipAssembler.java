package com.mobiautobackend.api.rest.assemblers;

import com.mobiautobackend.api.rest.controllers.DealershipController;
import com.mobiautobackend.api.rest.models.request.DealershipRequestModel;
import com.mobiautobackend.api.rest.models.response.DealershipResponseModel;
import com.mobiautobackend.domain.entities.Dealership;
import com.mobiautobackend.domain.entities.Member;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DealershipAssembler extends RepresentationModelAssemblerSupport<Dealership, DealershipResponseModel> {

    public DealershipAssembler() {
        super(DealershipController.class, DealershipResponseModel.class);
    }

    @Override
    public DealershipResponseModel toModel(Dealership dealership) {
        DealershipResponseModel dealershipResponseModel = new DealershipResponseModel(dealership);
        dealershipResponseModel.add(this.buildDealershipSelfLink(dealership.getId()));
        return dealershipResponseModel;
    }

    public Link buildDealershipSelfLink(String dealershipId) {
        return linkTo(methodOn(DealershipController.class).findById(dealershipId)).withSelfRel();
    }

    public Dealership toEntity(DealershipRequestModel dealershipRequestModel) {
        Dealership dealership = new Dealership();
        dealership.setTradeName(dealershipRequestModel.getTradeName());
        dealership.setCnpj(dealershipRequestModel.getCnpj());
        dealership.setMembers(Collections.singletonList(new Member(dealershipRequestModel.getMemberId())));
        return dealership;
    }
}
