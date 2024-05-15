package com.mobiautobackend.domain.services;

import com.mobiautobackend.domain.entities.Dealership;
import com.mobiautobackend.domain.entities.Member;
import com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum;
import com.mobiautobackend.domain.enumeration.MemberRole;
import com.mobiautobackend.domain.exceptions.BadRequestException;
import com.mobiautobackend.domain.repositories.DealershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
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

    public Optional<Dealership> findByMemberId(String memberId) {
        return dealershipRepository.findByMembersId(memberId);
    }

    public Dealership create(Dealership dealership) {
        return dealershipRepository.save(dealership);
    }

    public boolean isAnAuthorizedMember(String dealershipId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(MemberRole.ADMIN.toString()))) {
                return true;
            }
            Member authenticatedMember = (Member) authentication.getPrincipal();
            Dealership authenticatedMemberDealership = this.findByMemberId(authenticatedMember.getId())
                    .orElseThrow(() -> new BadRequestException(ExceptionMessagesEnum.DEALERSHIP_NOT_FOUND));

            if (Objects.equals(dealershipId, authenticatedMemberDealership.getId())) {
                Member searchedMember = authenticatedMemberDealership.getMembers().stream()
                        .filter(m -> m.getId().equals(authenticatedMember.getId())).findFirst().get();
                if (Objects.equals(authenticatedMember.getId(), searchedMember.getId())) {
                    return authenticatedMember.isOwner() || authenticatedMember.isManager() || authenticatedMember.isAssistant();
                } else {
                    return false;
                }
            }
            return false;
        }
        return true;
    }
}
