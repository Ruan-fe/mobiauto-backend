package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.api.rest.assemblers.VehicleAssembler;
import com.mobiautobackend.api.rest.models.request.VehicleRequestModel;
import com.mobiautobackend.api.rest.models.response.VehicleResponseModel;
import com.mobiautobackend.domain.entities.Vehicle;
import com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum;
import com.mobiautobackend.domain.exceptions.BadRequestException;
import com.mobiautobackend.domain.exceptions.ForbiddenException;
import com.mobiautobackend.domain.exceptions.NotFoundException;
import com.mobiautobackend.domain.services.DealershipService;
import com.mobiautobackend.domain.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum.NOT_AUTHORIZED;

@RestController
public class VehicleController {

    public static final String VEHICLE_RESOURCE_PATH = "/api/vehicles";
    public static final String VEHICLE_SELF_PATH = VEHICLE_RESOURCE_PATH + "/{vehicleId}";

    private final VehicleService vehicleService;
    private final VehicleAssembler vehicleAssembler;
    private final DealershipService dealershipService;
    private final PagedResourcesAssembler<Vehicle> pagedResponseAssembler;

    public VehicleController(VehicleService vehicleService,
                             VehicleAssembler vehicleAssembler,
                             DealershipService dealershipService,
                             PagedResourcesAssembler<Vehicle> pagedResponseAssembler) {
        this.vehicleService = vehicleService;
        this.vehicleAssembler = vehicleAssembler;
        this.dealershipService = dealershipService;
        this.pagedResponseAssembler = pagedResponseAssembler;
    }

    @PostMapping(VEHICLE_RESOURCE_PATH)
    public ResponseEntity<?> create(@RequestBody @Valid VehicleRequestModel vehicleRequestModel) {
        if (!dealershipService.isAnAuthorizedMember(vehicleRequestModel.getDealershipId())) {
            throw new ForbiddenException(NOT_AUTHORIZED);
        }
        dealershipService.findById(vehicleRequestModel.getDealershipId())
                .orElseThrow(() -> new BadRequestException(ExceptionMessagesEnum.DEALERSHIP_NOT_FOUND));

        Vehicle vehicle = vehicleAssembler.toEntity(vehicleRequestModel);
        vehicle = vehicleService.create(vehicle);

        return ResponseEntity.created(vehicleAssembler.buildVehicleSelfLink(vehicle.getId()).toUri()).build();
    }

    @GetMapping(VEHICLE_SELF_PATH)
    public ResponseEntity<VehicleResponseModel> findById(@PathVariable("vehicleId") final String vehicleId) {
        Vehicle vehicle = vehicleService.findById(vehicleId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessagesEnum.VEHICLE_NOT_FOUND));
        return ResponseEntity.ok().body(vehicleAssembler.toModel(vehicle));
    }

    @GetMapping(VEHICLE_RESOURCE_PATH)
    public ResponseEntity<PagedModel<VehicleResponseModel>> findAllByFilters(@RequestParam(value = "dealershipId", required = false) final String dealershipId,
                                                                             Pageable pageable) {
        Page<Vehicle> vehicles = vehicleService.findAllByFilters(dealershipId, pageable);
        return ResponseEntity.ok().body(pagedResponseAssembler.toModel(vehicles, vehicleAssembler));
    }
}
