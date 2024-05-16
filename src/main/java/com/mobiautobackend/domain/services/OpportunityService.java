package com.mobiautobackend.domain.services;

import com.mobiautobackend.api.rest.models.request.OpportunityAssignRequestModel;
import com.mobiautobackend.domain.entities.Customer;
import com.mobiautobackend.domain.entities.Dealership;
import com.mobiautobackend.domain.entities.Member;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final CustomerRepository customerRepository;
    private final DealershipService dealershipService;

    @Autowired
    public OpportunityService(OpportunityRepository opportunityRepository, CustomerRepository customerRepository,
                              DealershipService dealershipService) {
        this.opportunityRepository = opportunityRepository;
        this.customerRepository = customerRepository;
        this.dealershipService = dealershipService;
    }

    @Transactional
    public Opportunity create(Opportunity opportunity) {
        opportunity.setCustomer(this.createCustomer(opportunity.getCustomer()));
        String assistantId = this.getAssistantIdToAssign(opportunity);
        if (assistantId != null) {
            opportunity.setAssignDate(ZonedDateTime.now());
            opportunity.setStatus(OpportunityStatus.IN_PROGRESS);
            opportunity.setMemberId(assistantId);
        }
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

    public String getAssistantIdToAssign(Opportunity opportunity) {
        Dealership dealership = dealershipService.findById(opportunity.getDealershipId()).get();
        List<String> assistantMembers = dealership.getMembers().stream().filter(Member::isAssistant).map(Member::getId).toList();
        if (assistantMembers.isEmpty()) {
            return null;
        }

        Map<String, Long> opportunitiesInProgressByMembersId = opportunityRepository.findByDealershipIdAndStatusAndMemberIdIn(
                        opportunity.getDealershipId(), OpportunityStatus.IN_PROGRESS, assistantMembers)
                .stream()
                .collect(Collectors.groupingBy(Opportunity::getMemberId, Collectors.counting()));

        String memberIdWithLessOpportuniesAssign = opportunitiesInProgressByMembersId.entrySet().stream()
                .min(Comparator.comparingLong((Map.Entry<String, Long> entry) -> entry.getValue())
                        .thenComparingLong(entry -> this.getLastAssignOpportunityDate(entry.getKey()).toEpochSecond()))
                .map(Map.Entry::getKey)
                .orElse(null);

        return memberIdWithLessOpportuniesAssign;
    }

    private ZonedDateTime getLastAssignOpportunityDate(String memberId) {
        return opportunityRepository.findTopByMemberIdOrderByAssignDateDesc(memberId)
                .map(Opportunity::getAssignDate)
                .orElse(null);
    }
}
