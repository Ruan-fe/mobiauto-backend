package com.mobiautobackend.domain.entities;

import com.mobiautobackend.domain.enumeration.VehicleBrandType;
import com.mobiautobackend.domain.enumeration.VehicleColor;
import com.mobiautobackend.domain.enumeration.VehicleFuelType;
import com.mobiautobackend.domain.enumeration.VehicleTransmissionType;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "VEHICLE")
@DynamicUpdate
public class Vehicle {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    private String id;

    @Column(name = "DEALERSHIP_ID", nullable = false)
    private String dealershipId;

    @Column(name = "LICENSE_PLATE")
    private String licensePlate;

    @Column(name = "BRAND_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleBrandType brandType;

    @Column(name = "MODEL", nullable = false)
    private String model;

    @Column(name = "MANUFACTURING_YEAR", nullable = false)
    private int manufacturingYear;

    @Column(name = "MODEL_YEAR", nullable = false)
    private int modelYear;

    @Column(name = "VERSION", nullable = false)
    private String version;

    @Column(name = "TRANSMISSION_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleTransmissionType transmissionType;

    @Column(name = "FUEL_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleFuelType fuelType;

    @Column(name = "DOORS", nullable = false)
    private int doors;

    @Column(name = "COLOR", nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleColor color;

    @Column(name = "CREATION_DATE", nullable = false)
    private ZonedDateTime creationDate;

    @PrePersist
    private void prePersist() {
        id = UUID.randomUUID().toString();
        creationDate = ZonedDateTime.now();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDealershipId() {
        return dealershipId;
    }

    public void setDealershipId(String dealershipId) {
        this.dealershipId = dealershipId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleBrandType getBrandType() {
        return brandType;
    }

    public void setBrandType(VehicleBrandType brandType) {
        this.brandType = brandType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getManufacturingYear() {
        return manufacturingYear;
    }

    public void setManufacturingYear(int manufacturingYear) {
        this.manufacturingYear = manufacturingYear;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public VehicleTransmissionType getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(VehicleTransmissionType transmissionType) {
        this.transmissionType = transmissionType;
    }

    public VehicleFuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(VehicleFuelType fuelType) {
        this.fuelType = fuelType;
    }

    public int getDoors() {
        return doors;
    }

    public void setDoors(int doors) {
        this.doors = doors;
    }

    public VehicleColor getColor() {
        return color;
    }

    public void setColor(VehicleColor color) {
        this.color = color;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
