package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.api.rest.assemblers.VehicleAssembler;
import com.mobiautobackend.api.rest.models.request.VehicleRequestModel;
import com.mobiautobackend.api.rest.models.response.VehicleResponseModel;
import com.mobiautobackend.domain.entities.Vehicle;
import com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum;
import com.mobiautobackend.domain.exceptions.BadRequestException;
import com.mobiautobackend.domain.exceptions.NotFoundException;
import com.mobiautobackend.domain.services.DealershipService;
import com.mobiautobackend.domain.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VehicleController {

    private static final String VEHICLE_RESOURCE_PATH = "/api/vehicles";
    private static final String VEHICLE_SELF_PATH = VEHICLE_RESOURCE_PATH + "/{vehicleId}";

    private final VehicleService vehicleService;
    private final VehicleAssembler vehicleAssembler;
    private final DealershipService dealershipService;

    public VehicleController(VehicleService vehicleService,
                             VehicleAssembler vehicleAssembler,
                             DealershipService dealershipService) {
        this.vehicleService = vehicleService;
        this.vehicleAssembler = vehicleAssembler;
        this.dealershipService = dealershipService;
    }

    @PostMapping(VEHICLE_RESOURCE_PATH)
    public ResponseEntity<?> create(@RequestBody @Valid VehicleRequestModel vehicleRequestModel) {
        dealershipService.findById(vehicleRequestModel.getDealershipId())
                .orElseThrow(() -> new BadRequestException(ExceptionMessagesEnum.DEALERSHIP_NOT_FOUND));

        Vehicle vehicle = vehicleAssembler.toEntity(vehicleRequestModel);
        vehicle = vehicleService.create(vehicle);

        return ResponseEntity.created(vehicleAssembler.buildVehicleSelfLink(vehicle.getId()).toUri()).build();
    }

    @GetMapping(VEHICLE_SELF_PATH)
    public ResponseEntity<VehicleResponseModel> findById(@PathVariable("vehicleId") final String vehicleId) {
        Vehicle vehicle = vehicleService.findById(vehicleId).orElseThrow(() ->
                new NotFoundException(ExceptionMessagesEnum.VEHICLE_NOT_FOUND));
        return ResponseEntity.ok().body(vehicleAssembler.toModel(vehicle));
    }
}
