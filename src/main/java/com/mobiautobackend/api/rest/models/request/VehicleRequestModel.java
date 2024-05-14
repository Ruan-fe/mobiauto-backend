package com.mobiautobackend.api.rest.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobiautobackend.domain.enumeration.VehicleBrandType;
import com.mobiautobackend.domain.enumeration.VehicleColor;
import com.mobiautobackend.domain.enumeration.VehicleFuelType;
import com.mobiautobackend.domain.enumeration.VehicleTransmissionType;
import jakarta.validation.constraints.NotNull;

public class VehicleRequestModel {

    @NotNull
    @JsonProperty("licensePlate")
    private String licensePlate;

    @NotNull
    @JsonProperty("brandType")
    private VehicleBrandType brandType;

    @NotNull
    @JsonProperty("model")
    private String model;

    @NotNull
    @JsonProperty("manufacturingYear")
    private int manufacturingYear;

    @NotNull
    @JsonProperty("modelYear")
    private int modelYear;

    @NotNull
    @JsonProperty("version")
    private String version;

    @NotNull
    @JsonProperty("transmissionType")
    private VehicleTransmissionType transmissionType;

    @NotNull
    @JsonProperty("fuelType")
    private VehicleFuelType fuelType;

    @NotNull
    @JsonProperty("doors")
    private int doors;

    @NotNull
    @JsonProperty("color")
    private VehicleColor color;

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

    public String getVersion() {
        return version;
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
