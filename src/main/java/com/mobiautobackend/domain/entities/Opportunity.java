package com.mobiautobackend.domain.entities;

import com.mobiautobackend.domain.enumeration.OpportunityStatus;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "OPPORTUNITY")
public class Opportunity {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    private String id;

    @OneToOne
    private Customer customer;

    @Column(name = "DEALERSHIP_ID", nullable = false)
    private String dealershipId;

    @Column(name = "MEMBER_ID")
    private String memberId;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private OpportunityStatus status;

    @Column(name = "REASON")
    private String reason;

    @Column(name = "VEHICLE_ID", nullable = false)
    private String vehicleId;

    @Column(name = "ASSIGN_DATE")
    private ZonedDateTime assignDate;

    @Column(name = "CONCLUSION_DATE")
    private ZonedDateTime conclusionDate;

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDealershipId() {
        return dealershipId;
    }

    public void setDealershipId(String dealershipId) {
        this.dealershipId = dealershipId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public OpportunityStatus getStatus() {
        return status;
    }

    public void setStatus(OpportunityStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public ZonedDateTime getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(ZonedDateTime assignDate) {
        this.assignDate = assignDate;
    }

    public ZonedDateTime getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(ZonedDateTime conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
