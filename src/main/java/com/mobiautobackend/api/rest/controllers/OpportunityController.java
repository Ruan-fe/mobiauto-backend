package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.api.rest.assemblers.OpportunityAssembler;
import com.mobiautobackend.api.rest.models.request.OpportunityRequestModel;
import com.mobiautobackend.api.rest.models.response.OpportunityResponseModel;
import com.mobiautobackend.domain.entities.Opportunity;
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

import static com.mobiautobackend.api.rest.controllers.DealershipController.DEALERSHIP_SELF_PATH;
import static com.mobiautobackend.api.rest.controllers.VehicleController.VEHICLE_SELF_PATH;
import static com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum.NOT_AUTHORIZED;

@RestController
public class OpportunityController {

    public static final String OPPORTUNITY_RESOURCE_PATH = VEHICLE_SELF_PATH + "/opportunities";
    public static final String OPPORTUNITY_SELF_PATH = OPPORTUNITY_RESOURCE_PATH + "/{opportunityId}";
    public static final String OPPORTUNITY_PATH = DEALERSHIP_SELF_PATH + "/opportunities";
    public static final String OPPORTUNITY_ASSIGN_PATH = OPPORTUNITY_PATH + "/assign" ;


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
        if (!dealershipService.isAnAuthorizedMember(dealershipId)) {
            throw new ForbiddenException(NOT_AUTHORIZED);
        }
        Opportunity opportunity = opportunityService.findByIdAndDealershipIdAndVehicleId(opportunityId, dealershipId, vehicleId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessagesEnum.OPPORTUNITY_NOT_FOUND));
        return ResponseEntity.ok().body(opportunityAssembler.toModel(opportunity));
    }

    @GetMapping(OPPORTUNITY_RESOURCE_PATH)
    public ResponseEntity<PagedModel<OpportunityResponseModel>> findAllByFilters(@PathVariable("dealershipId") final String dealershipId,
                                                                                 @PathVariable(value = "vehicleId") final String vehicleId,
                                                                                 @RequestParam(value = "statuses", required = false) final List<OpportunityStatus> statuses,
                                                                                 Pageable pageable) {
        if (!dealershipService.isAnAuthorizedMember(dealershipId)) {
            throw new ForbiddenException(NOT_AUTHORIZED);
        }
        Page<Opportunity> opportunities = opportunityService.findAllByFilters(dealershipId, vehicleId, statuses, pageable);
        return ResponseEntity.ok().body(pagedResponseAssembler.toModel(opportunities, opportunityAssembler));
    }

    @GetMapping(OPPORTUNITY_PATH)
    public ResponseEntity<PagedModel<OpportunityResponseModel>> findAllByDealershipIdAndFilters(@PathVariable("dealershipId") final String dealershipId,
                                                                                                @RequestParam(value = "statuses", required = false) final List<OpportunityStatus> statuses,
                                                                                                Pageable pageable) {
        if (!dealershipService.isAnAuthorizedMember(dealershipId)) {
            throw new ForbiddenException(NOT_AUTHORIZED);
        }
        Page<Opportunity> opportunities = opportunityService.findAllByFilters(dealershipId, statuses, pageable);
        return ResponseEntity.ok().body(pagedResponseAssembler.toModel(opportunities, opportunityAssembler));
    }
}
