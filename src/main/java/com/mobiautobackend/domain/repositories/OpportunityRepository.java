package com.mobiautobackend.domain.repositories;

import com.mobiautobackend.domain.entities.Opportunity;
import com.mobiautobackend.domain.enumeration.OpportunityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, String> {
    Page<Opportunity> findByDealershipIdAndVehicleIdAndStatusIn(String dealershipId, String vehicleId, List<OpportunityStatus> statuses, Pageable pageable);

    Optional<Opportunity> findByIdAndDealershipIdAndVehicleId(String id, String dealershipId, String vehicleId);

    Optional<Opportunity> findByCustomerEmailAndVehicleIdAndStatusIn(String email, String vehicleId, List<OpportunityStatus> statuses);

    Page<Opportunity> findByDealershipIdAndStatusIn(String dealershipId, List<OpportunityStatus> statuses, Pageable pageable);

    Page<Opportunity> findByDealershipId(String dealershipId, Pageable pageable);

    Page<Opportunity> findByDealershipIdAndVehicleId(String dealershipId, String vehicleId, Pageable pageable);
}