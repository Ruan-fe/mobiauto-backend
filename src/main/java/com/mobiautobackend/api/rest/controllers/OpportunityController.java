package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.api.rest.assemblers.OpportunityAssembler;
import com.mobiautobackend.api.rest.models.request.OpportunityAssignRequestModel;
import com.mobiautobackend.api.rest.models.request.OpportunityRequestModel;
import com.mobiautobackend.api.rest.models.response.OpportunityResponseModel;
import com.mobiautobackend.domain.entities.Opportunity;
import com.mobiautobackend.domain.entities.Vehicle;
import com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum;
import com.mobiautobackend.domain.enumeration.OpportunityStatus;
import com.mobiautobackend.domain.exceptions.BadRequestException;
import com.mobiautobackend.domain.exceptions.ConflictException;
import com.mobiautobackend.domain.exceptions.ForbiddenException;
import com.mobiautobackend.domain.exceptions.NotFoundException;
import com.mobiautobackend.domain.services.DealershipService;
import com.mobiautobackend.domain.services.OpportunityService;
import com.mobiautobackend.domain.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum.NOT_AUTHORIZED;

@RestController
public class OpportunityController {

    public static final String OPPORTUNITY_RESOURCE_PATH = "/api/opportunities";
    public static final String OPPORTUNITY_SELF_PATH = OPPORTUNITY_RESOURCE_PATH + "/{opportunityId}";
    public static final String OPPORTUNITY_ASSIGN_PATH = OPPORTUNITY_SELF_PATH + "/assign";


    private final OpportunityService opportunityService;
    private final OpportunityAssembler opportunityAssembler;
    private final VehicleService vehicleService;
    private final PagedResourcesAssembler<Opportunity> pagedResponseAssembler;
    private final DealershipService dealershipService;

    @Autowired
    public OpportunityController(OpportunityService opportunityService,
                                 OpportunityAssembler opportunityAssembler,
                                 VehicleService vehicleService,
                                 PagedResourcesAssembler<Opportunity> pagedResponseAssembler,
                                 DealershipService dealershipService) {
        this.opportunityService = opportunityService;
        this.opportunityAssembler = opportunityAssembler;
        this.vehicleService = vehicleService;
        this.pagedResponseAssembler = pagedResponseAssembler;
        this.dealershipService = dealershipService;
    }

    @PostMapping(OPPORTUNITY_RESOURCE_PATH)
    public ResponseEntity<?> create(@RequestBody @Valid OpportunityRequestModel opportunityRequestModel) {
        Vehicle vehicle = vehicleService.findById(opportunityRequestModel.getVehicleId())
                .orElseThrow(() -> new BadRequestException(ExceptionMessagesEnum.VEHICLE_NOT_FOUND));

        Opportunity opportunity = opportunityAssembler.toEntity(opportunityRequestModel, vehicle.getDealershipId(),
                opportunityRequestModel.getVehicleId());

        opportunityService.findOpportunity(opportunityRequestModel.getCustomer().getEmail(), opportunity.getVehicleId(),
                List.of(OpportunityStatus.NEW, OpportunityStatus.IN_PROGRESS)).ifPresent(searchedOpportunity -> {
            throw new ConflictException(ExceptionMessagesEnum.OPPORTUNITY_ALREADY_EXISTS,
                    opportunityAssembler.buildOpportunitySelfLink(searchedOpportunity).toUri());
        });

        opportunity = opportunityService.create(opportunity);

        return ResponseEntity.created(opportunityAssembler.buildOpportunitySelfLink(opportunity).toUri()).build();
    }

    @GetMapping(OPPORTUNITY_SELF_PATH)
    public ResponseEntity<OpportunityResponseModel> findById(@PathVariable("opportunityId") final String opportunityId) {
        Opportunity opportunity = opportunityService.findById(opportunityId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessagesEnum.OPPORTUNITY_NOT_FOUND));

        if (!dealershipService.isAnAuthorizedMember(opportunity.getDealershipId())) {
            throw new ForbiddenException(NOT_AUTHORIZED);
        }

        return ResponseEntity.ok().body(opportunityAssembler.toModel(opportunity));
    }

    @GetMapping(OPPORTUNITY_RESOURCE_PATH)
    public ResponseEntity<PagedModel<OpportunityResponseModel>> findAllByFilters(@RequestParam("dealershipId") final String dealershipId,
                                                                                 @RequestParam(value = "statuses", required = false) final List<OpportunityStatus> statuses,
                                                                                 Pageable pageable) {
        if (!dealershipService.isAnAuthorizedMember(dealershipId)) {
            throw new ForbiddenException(NOT_AUTHORIZED);
        }

        Page<Opportunity> opportunities = opportunityService.findAllByFilters(dealershipId, statuses, pageable);
        return ResponseEntity.ok().body(pagedResponseAssembler.toModel(opportunities, opportunityAssembler));
    }

    @PostMapping(OPPORTUNITY_ASSIGN_PATH)
    public ResponseEntity<?> assign(@PathVariable("opportunityId") final String opportunityId,
                                    @RequestBody @Valid OpportunityAssignRequestModel assignRequestModel) {
        Opportunity opportunity = opportunityService.findById(opportunityId).orElseThrow(() ->
                new BadRequestException(ExceptionMessagesEnum.OPPORTUNITY_NOT_FOUND));

        if (!dealershipService.isAnAuthorizedMember(opportunity.getDealershipId())) {
            throw new ForbiddenException(NOT_AUTHORIZED);
        }

        opportunity = opportunityService.assign(opportunity, assignRequestModel);

        return ResponseEntity.ok().location(opportunityAssembler.buildOpportunitySelfLink(opportunity).toUri()).build();
    }
}
