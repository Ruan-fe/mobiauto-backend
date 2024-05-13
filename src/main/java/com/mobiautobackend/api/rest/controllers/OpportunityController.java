package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.api.rest.assemblers.OpportunityAssembler;
import com.mobiautobackend.api.rest.models.request.OpportunityRequestModel;
import com.mobiautobackend.api.rest.models.response.OpportunityResponseModel;
import com.mobiautobackend.domain.entities.Opportunity;
import com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum;
import com.mobiautobackend.domain.enumeration.OpportunityStatus;
import com.mobiautobackend.domain.exceptions.BadRequestException;
import com.mobiautobackend.domain.exceptions.ConflictException;
import com.mobiautobackend.domain.exceptions.NotFoundException;
import com.mobiautobackend.domain.services.OpportunityService;
import com.mobiautobackend.domain.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mobiautobackend.api.rest.controllers.VehicleController.VEHICLE_SELF_PATH;

@RestController
public class OpportunityController {

    private static final String OPPORTUNITY_RESOURCE_PATH = VEHICLE_SELF_PATH + "/opportunities";
    private static final String OPPORTUNITY_SELF_PATH = OPPORTUNITY_RESOURCE_PATH + "/{opportunityId}";

    private final OpportunityService opportunityService;
    private final OpportunityAssembler opportunityAssembler;
    private final VehicleService vehicleService;

    @Autowired
    public OpportunityController(OpportunityService opportunityService,
                                 OpportunityAssembler opportunityAssembler,
                                 VehicleService vehicleService) {
        this.opportunityService = opportunityService;
        this.opportunityAssembler = opportunityAssembler;
        this.vehicleService = vehicleService;
    }

    @PostMapping(OPPORTUNITY_RESOURCE_PATH)
    public ResponseEntity<?> create(@PathVariable("dealershipId") final String dealershipId,
                                    @PathVariable("vehicleId") final String vehicleId,
                                    @RequestBody @Valid OpportunityRequestModel opportunityRequestModel) {
        vehicleService.findByIdAndDealershipId(vehicleId, dealershipId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessagesEnum.VEHICLE_NOT_FOUND));

        Opportunity opportunity = opportunityAssembler.toEntity(opportunityRequestModel, dealershipId, vehicleId);

        opportunityService.findOpportunity(opportunityRequestModel.getCustomer().getEmail(), opportunity.getVehicleId(),
                List.of(OpportunityStatus.NEW, OpportunityStatus.IN_PROGRESS)).ifPresent(searchedOpportunity -> {
            throw new ConflictException(ExceptionMessagesEnum.OPPORTUNITY_ALREADY_EXISTS,
                    opportunityAssembler.buildOpportunitySelfLink(searchedOpportunity).toUri());
        });

        opportunity = opportunityService.create(opportunity);

        return ResponseEntity.created(opportunityAssembler.buildOpportunitySelfLink(opportunity).toUri()).build();
    }

    @GetMapping(OPPORTUNITY_SELF_PATH)
    public ResponseEntity<OpportunityResponseModel> findById(@PathVariable("dealershipId") final String dealershipId,
                                                             @PathVariable("vehicleId") final String vehicleId,
                                                             @PathVariable("opportunityId") final String opportunityId) {
        Opportunity opportunity = opportunityService.findByIdAndDealershipIdAndVehicleId(opportunityId, dealershipId, vehicleId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessagesEnum.OPPORTUNITY_NOT_FOUND));
        return ResponseEntity.ok().body(opportunityAssembler.toModel(opportunity));
    }
}
