package com.mobiautobackend.domain.services;

import com.mobiautobackend.api.rest.models.request.OpportunityAssignRequestModel;
import com.mobiautobackend.domain.entities.Customer;
import com.mobiautobackend.domain.entities.Opportunity;
import com.mobiautobackend.domain.enumeration.OpportunityStatus;
import com.mobiautobackend.domain.repositories.CustomerRepository;
import com.mobiautobackend.domain.repositories.OpportunityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public OpportunityService(OpportunityRepository opportunityRepository, CustomerRepository customerRepository) {
        this.opportunityRepository = opportunityRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Opportunity create(Opportunity opportunity) {
        opportunity.setCustomer(this.createCustomer(opportunity.getCustomer()));
        return opportunityRepository.save(opportunity);
    }

    private Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Opportunity> findOpportunity(String email, String vehicleId, List<OpportunityStatus> statuses) {
        return opportunityRepository.findByCustomerEmailAndVehicleIdAndStatusIn(email, vehicleId, statuses);
    }

    public Optional<Opportunity> findById(String opportunityId) {
        return opportunityRepository.findById(opportunityId);
    }

    public Page<Opportunity> findAllByFilters(String dealershipId, List<OpportunityStatus> statuses, Pageable pageable) {
        if (CollectionUtils.isEmpty(statuses)) {
            return opportunityRepository.findByDealershipId(dealershipId, pageable);
        }
        return opportunityRepository.findByDealershipIdAndStatusIn(dealershipId, statuses, pageable);
    }

    @Transactional
    public Opportunity assign(Opportunity opportunity, OpportunityAssignRequestModel assignRequestModel) {
        opportunity.setStatus(assignRequestModel.getStatus());
        opportunity.setMemberId(assignRequestModel.getMemberId());
        opportunity.setReason(assignRequestModel.getReason());

        if (Objects.equals(opportunity.getStatus(), OpportunityStatus.IN_PROGRESS)) {
            opportunity.setAssignDate(ZonedDateTime.now());
        } else if (Objects.equals(opportunity.getStatus(), OpportunityStatus.APPROVED) ||
                Objects.equals(opportunity.getStatus(), OpportunityStatus.REPROVED)) {
            opportunity.setConclusionDate(ZonedDateTime.now());
        }

        return opportunityRepository.save(opportunity);
    }
}
