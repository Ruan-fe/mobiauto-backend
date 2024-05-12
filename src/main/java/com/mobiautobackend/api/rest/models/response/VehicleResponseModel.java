package com.mobiautobackend.api.rest.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mobiautobackend.domain.entities.Vehicle;
import com.mobiautobackend.domain.enumeration.VehicleBrandType;
import com.mobiautobackend.domain.enumeration.VehicleColor;
import com.mobiautobackend.domain.enumeration.VehicleFuelType;
import com.mobiautobackend.domain.enumeration.VehicleTransmissionType;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@JsonPropertyOrder({
        "id",
        "dealershipId",
        "licensePlate",
        "brandType",
        "model",
        "manufacturingYear",
        "modelYear",
        "description",
        "transmissionType",
        "fuelType",
        "doors",
        "color"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "vehicle", collectionRelation = "vehicles")
public class VehicleResponseModel extends RepresentationModel<VehicleResponseModel> {

    @JsonProperty("id")
    private String id;

    @JsonProperty("dealershipId")
    private String dealershipId;

    @JsonProperty("licensePlate")
    private String licensePlate;

    @JsonProperty("brandType")
    private VehicleBrandType brandType;

    @JsonProperty("model")
    private String model;

    @JsonProperty("manufacturingYear")
    private int manufacturingYear;

    @JsonProperty("modelYear")
    private int modelYear;

    @JsonProperty("description")
    private String description;

    @JsonProperty("transmissionType")
    private VehicleTransmissionType transmissionType;

    @JsonProperty("fuelType")
    private VehicleFuelType fuelType;

    @JsonProperty("doors")
    private int doors;

    @JsonProperty("color")
    private VehicleColor color;

    public VehicleResponseModel() {
    }

    public VehicleResponseModel(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.dealershipId = vehicle.getDealershipId();
        this.licensePlate = vehicle.getLicensePlate();
        this.brandType = vehicle.getBrandType();
        this.model = vehicle.getModel();
        this.manufacturingYear = vehicle.getManufacturingYear();
        this.modelYear = vehicle.getModelYear();
        this.description = vehicle.getDescription();
        this.transmissionType = vehicle.getTransmissionType();
        this.fuelType = vehicle.getFuelType();
        this.doors = vehicle.getDoors();
        this.color = vehicle.getColor();
    }
}
