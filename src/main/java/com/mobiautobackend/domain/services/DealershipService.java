package com.mobiautobackend.domain.services;

import com.mobiautobackend.domain.entities.Dealership;
import com.mobiautobackend.domain.repositories.DealershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DealershipService {

    private final DealershipRepository dealershipRepository;

    @Autowired
    public DealershipService(DealershipRepository dealershipRepository) {
        this.dealershipRepository = dealershipRepository;
    }

    public Optional<Dealership> findById(String dealershipId) {
        return dealershipRepository.findById(dealershipId);
    }

    public Optional<Dealership> findByCnpjOrMemberId(String cnpj, String memberId) {
        return dealershipRepository.findByCnpjOrMembersId(cnpj, memberId);
    }

    public Dealership create(Dealership dealership) {
        return dealershipRepository.save(dealership);
    }
}
