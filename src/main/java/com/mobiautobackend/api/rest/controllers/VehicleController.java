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

import static com.mobiautobackend.api.rest.controllers.DealershipController.DEALERSHIP_SELF_PATH;

@RestController
public class VehicleController {

    public static final String VEHICLE_RESOURCE_PATH = DEALERSHIP_SELF_PATH + "/vehicles";
    public static final String VEHICLE_SELF_PATH = VEHICLE_RESOURCE_PATH + "/{vehicleId}";
    public static final String VEHICLE_PATH = "/api/vehicles";

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
    public ResponseEntity<?> create(@PathVariable("dealershipId") final String dealershipId,
                                    @RequestBody @Valid VehicleRequestModel vehicleRequestModel) {
        //TODO validar que o usuario autenticado pertence e tem cargo na loja
        dealershipService.findById(dealershipId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessagesEnum.DEALERSHIP_NOT_FOUND));

        Vehicle vehicle = vehicleAssembler.toEntity(vehicleRequestModel, dealershipId);
        vehicle = vehicleService.create(vehicle);

        return ResponseEntity.created(vehicleAssembler.buildVehicleSelfLink(vehicle.getId(), vehicle.getDealershipId()).toUri()).build();
    }

    @GetMapping(VEHICLE_SELF_PATH)
    public ResponseEntity<VehicleResponseModel> findById(@PathVariable("vehicleId") final String vehicleId,
                                                         @PathVariable("dealershipId") final String dealershipId) {
        Vehicle vehicle = vehicleService.findByIdAndDealershipId(vehicleId, dealershipId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessagesEnum.VEHICLE_NOT_FOUND));
        return ResponseEntity.ok().body(vehicleAssembler.toModel(vehicle));
    }

    //TODO get all vehicle
    //TODO get all vehicle from store
}
