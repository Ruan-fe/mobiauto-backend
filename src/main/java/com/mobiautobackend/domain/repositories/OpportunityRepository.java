package com.mobiautobackend.domain.repositories;

import com.mobiautobackend.domain.entities.Opportunity;
import com.mobiautobackend.domain.enumeration.OpportunityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, String> {
    Optional<Opportunity> findByIdAndDealershipIdAndVehicleId(String id, String dealershipId, String vehicleId);

    Optional<Opportunity> findByCustomerEmailAndVehicleIdAndStatusIn(String email, String vehicleId, Collection<OpportunityStatus> statuses);
}