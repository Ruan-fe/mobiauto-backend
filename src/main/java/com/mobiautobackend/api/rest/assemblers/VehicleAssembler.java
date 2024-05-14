package com.mobiautobackend.api.rest.assemblers;

import com.mobiautobackend.api.rest.controllers.DealershipController;
import com.mobiautobackend.api.rest.controllers.VehicleController;
import com.mobiautobackend.api.rest.models.request.VehicleRequestModel;
import com.mobiautobackend.api.rest.models.response.VehicleResponseModel;
import com.mobiautobackend.domain.entities.Vehicle;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VehicleAssembler extends RepresentationModelAssemblerSupport<Vehicle, VehicleResponseModel> {

    public VehicleAssembler() {
        super(VehicleController.class, VehicleResponseModel.class);
    }

    public Vehicle toEntity(VehicleRequestModel vehicleRequestModel, String dealershipId) {
        Vehicle vehicle = new Vehicle();
        vehicle.setDealershipId(dealershipId);
        vehicle.setLicensePlate(vehicleRequestModel.getLicensePlate());
        vehicle.setBrandType(vehicleRequestModel.getBrandType());
        vehicle.setModel(vehicleRequestModel.getModel());
        vehicle.setManufacturingYear(vehicleRequestModel.getManufacturingYear());
        vehicle.setModelYear(vehicleRequestModel.getModelYear());
        vehicle.setVersion(vehicleRequestModel.getVersion());
        vehicle.setTransmissionType(vehicleRequestModel.getTransmissionType());
        vehicle.setFuelType(vehicleRequestModel.getFuelType());
        vehicle.setDoors(vehicleRequestModel.getDoors());
        vehicle.setColor(vehicleRequestModel.getColor());
        return vehicle;
    }

    @Override
    public VehicleResponseModel toModel(Vehicle vehicle) {
        VehicleResponseModel vehicleResponseModel = new VehicleResponseModel(vehicle);
        vehicleResponseModel.add(this.buildVehicleSelfLink(vehicle.getId(), vehicle.getDealershipId()));
        vehicleResponseModel.add(this.buildDealershipLink(vehicle.getDealershipId()));
        return vehicleResponseModel;
    }

    public Link buildVehicleSelfLink(String vehicleId, String dealershipId) {
        return linkTo(methodOn(VehicleController.class).findById(vehicleId, dealershipId)).withSelfRel();
    }

    public Link buildDealershipLink(String dealershipId) {
        return linkTo(methodOn(DealershipController.class).findById(dealershipId)).withRel("dealership");
    }
}
