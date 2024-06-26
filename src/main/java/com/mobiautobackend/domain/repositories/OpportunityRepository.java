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
    Optional<Opportunity> findByCustomerEmailAndVehicleIdAndStatusIn(String email, String vehicleId, List<OpportunityStatus> statuses);

    Page<Opportunity> findByDealershipIdAndStatusIn(String dealershipId, List<OpportunityStatus> statuses, Pageable pageable);

    Page<Opportunity> findByDealershipId(String dealershipId, Pageable pageable);

    List<Opportunity> findByDealershipIdAndStatusAndMemberIdIn(String dealershipId, OpportunityStatus status, List<String> memberIds);

    Optional<Opportunity> findTopByMemberIdOrderByAssignDateDesc(String memberId);
}