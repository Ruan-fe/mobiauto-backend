package com.mobiautobackend.domain.services;

import com.mobiautobackend.domain.entities.Dealership;
import com.mobiautobackend.domain.entities.Member;
import com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum;
import com.mobiautobackend.domain.enumeration.MemberRole;
import com.mobiautobackend.domain.exceptions.BadRequestException;
import com.mobiautobackend.domain.repositories.DealershipRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum.MEMBER_ALREADY_HAS_DEALERSHIP;

@Service
public class DealershipService {

    private final DealershipRepository dealershipRepository;
    private final MemberService memberService;

    @Autowired
    public DealershipService(DealershipRepository dealershipRepository,
                             MemberService memberService) {
        this.dealershipRepository = dealershipRepository;
        this.memberService = memberService;
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

    @Transactional
    public Dealership create(Dealership dealership) {
        memberService.updateMemberRole(CollectionUtils.firstElement(dealership.getMembers()).getId(), MemberRole.OWNER);
        return dealershipRepository.save(dealership);
    }

    public boolean isAnAuthorizedMember(String dealershipId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
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
                    return authenticatedMember.isOwner() || authenticatedMember.isManager();
                }
            }
            return false;
        }
        return true;
    }

    @Transactional
    public Dealership registerMember(Member memberToRegister, Dealership dealership, MemberRole memberRole) {
        if (memberToRegister.isUser()) {
            dealership.getMembers().add(memberToRegister);
            dealership.setMembers(dealership.getMembers());
            memberService.updateMemberRole(memberToRegister.getId(), memberRole);
            return dealershipRepository.save(dealership);
        }
        throw new BadRequestException(MEMBER_ALREADY_HAS_DEALERSHIP);
    }
}
