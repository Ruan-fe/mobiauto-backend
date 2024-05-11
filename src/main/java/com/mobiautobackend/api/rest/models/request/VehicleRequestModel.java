package com.mobiautobackend.api.rest.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobiautobackend.domain.enumeration.VehicleBrandType;
import com.mobiautobackend.domain.enumeration.VehicleColor;
import com.mobiautobackend.domain.enumeration.VehicleFuelType;
import com.mobiautobackend.domain.enumeration.VehicleTransmissionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VehicleRequestModel {

    @NotBlank
    @JsonProperty("dealershipId")
    private String dealershipId;

    @NotBlank
    @JsonProperty("licensePlate")
    private String licensePlate;

    @NotBlank
    @JsonProperty("brandType")
    private VehicleBrandType brandType;

    @NotBlank
    @JsonProperty("model")
    private String model;

    @NotNull
    @JsonProperty("manufacturingYear")
    private int manufacturingYear;

    @NotNull
    @JsonProperty("modelYear")
    private int modelYear;

    @NotNull
    @JsonProperty("description")
    private String description;

    @NotBlank
    @JsonProperty("transmissionType")
    private VehicleTransmissionType transmissionType;

    @NotBlank
    @JsonProperty("fuelType")
    private VehicleFuelType fuelType;

    @NotNull
    @JsonProperty("doors")
    private int doors;

    @NotBlank
    @JsonProperty("color")
    private VehicleColor color;

    public String getDealershipId() {
        return dealershipId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleBrandType getBrandType() {
        return brandType;
    }

    public String getModel() {
        return model;
    }

    public int getManufacturingYear() {
        return manufacturingYear;
    }

    public int getModelYear() {
        return modelYear;
    }

    public String getDescription() {
        return description;
    }

    public VehicleTransmissionType getTransmissionType() {
        return transmissionType;
    }

    public VehicleFuelType getFuelType() {
        return fuelType;
    }

    public int getDoors() {
        return doors;
    }

    public VehicleColor getColor() {
        return color;
    }
}
