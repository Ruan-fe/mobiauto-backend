package com.mobiautobackend.domain.services;

import com.mobiautobackend.domain.entities.Customer;
import com.mobiautobackend.domain.entities.Opportunity;
import com.mobiautobackend.domain.enumeration.OpportunityStatus;
import com.mobiautobackend.domain.repositories.CustomerRepository;
import com.mobiautobackend.domain.repositories.OpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Optional<Opportunity> findByIdAndDealershipIdAndVehicleId(String opportunityId, String dealershipId, String vehicleId) {
        return opportunityRepository.findByIdAndDealershipIdAndVehicleId(opportunityId, dealershipId, vehicleId);
    }
}
