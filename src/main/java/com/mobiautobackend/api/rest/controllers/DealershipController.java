package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.api.rest.assemblers.DealershipAssembler;
import com.mobiautobackend.api.rest.models.request.DealershipRegisterMemberRequestModel;
import com.mobiautobackend.api.rest.models.request.DealershipRequestModel;
import com.mobiautobackend.api.rest.models.response.DealershipResponseModel;
import com.mobiautobackend.domain.entities.Dealership;
import com.mobiautobackend.domain.entities.Member;
import com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum;
import com.mobiautobackend.domain.exceptions.BadRequestException;
import com.mobiautobackend.domain.exceptions.ConflictException;
import com.mobiautobackend.domain.exceptions.ForbiddenException;
import com.mobiautobackend.domain.exceptions.NotFoundException;
import com.mobiautobackend.domain.services.DealershipService;
import com.mobiautobackend.domain.services.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum.NOT_AUTHORIZED;

@RestController
public class DealershipController {
    public static final String DEALERSHIP_RESOURCE_PATH = "/api/dealerships";
    public static final String DEALERSHIP_SELF_PATH = DEALERSHIP_RESOURCE_PATH + "/{dealershipId}";
    public static final String DEALERSHIP_REGISTER_MEMBER_PATH = DEALERSHIP_SELF_PATH + "/registerMember";

    private final DealershipService dealershipService;
    private final DealershipAssembler dealershipAssembler;
    private final MemberService memberService;

    @Autowired
    public DealershipController(DealershipService dealershipService,
                                DealershipAssembler dealershipAssembler,
                                MemberService memberService) {
        this.dealershipService = dealershipService;
        this.dealershipAssembler = dealershipAssembler;
        this.memberService = memberService;
    }

    @PostMapping(DEALERSHIP_RESOURCE_PATH)
    public ResponseEntity<?> create(@RequestBody @Valid DealershipRequestModel dealershipRequestModel) {
        if (!memberService.isAnAuthorizedMember(dealershipRequestModel.getMemberId())) {
            throw new ForbiddenException(NOT_AUTHORIZED);
        }
        memberService.findById(dealershipRequestModel.getMemberId())
                .orElseThrow(() -> new BadRequestException(ExceptionMessagesEnum.MEMBER_NOT_FOUND));

        dealershipService.findByCnpjOrMemberId(dealershipRequestModel.getCnpj(), dealershipRequestModel.getMemberId())
                .ifPresent(searchedDealership -> {
                    throw new ConflictException(ExceptionMessagesEnum.DEALERSHIP_ALREADY_EXISTS,
                            dealershipAssembler.buildDealershipSelfLink(searchedDealership.getId()).toUri());
                });

        Dealership dealership = dealershipAssembler.toEntity(dealershipRequestModel);
        dealership = dealershipService.create(dealership);

        return ResponseEntity.created(dealershipAssembler.buildDealershipSelfLink(dealership.getId()).toUri()).build();
    }

    @GetMapping(DEALERSHIP_SELF_PATH)
    public ResponseEntity<DealershipResponseModel> findById(@PathVariable("dealershipId") final String dealershipId) {
        Dealership dealership = dealershipService.findById(dealershipId).orElseThrow(() ->
                new NotFoundException(ExceptionMessagesEnum.DEALERSHIP_NOT_FOUND));
        return ResponseEntity.ok().body(dealershipAssembler.toModel(dealership));
    }

    @PostMapping(DEALERSHIP_REGISTER_MEMBER_PATH)
    public ResponseEntity<?> registerMember(@PathVariable("dealershipId") final String dealershipId,
                                            @RequestBody @Valid DealershipRegisterMemberRequestModel registerMemberRequestModel) {
        Dealership dealership = dealershipService.findById(dealershipId).orElseThrow(() ->
                new BadRequestException(ExceptionMessagesEnum.DEALERSHIP_NOT_FOUND));

        if (!dealershipService.isAnAuthorizedMember(dealership.getId())) {
            throw new ForbiddenException(NOT_AUTHORIZED);
        }
        Member memberToRegister = memberService.findById(registerMemberRequestModel.getMemberId())
                .orElseThrow(() -> new BadRequestException(ExceptionMessagesEnum.MEMBER_NOT_FOUND));

        dealership = dealershipService.registerMember(memberToRegister, dealership, registerMemberRequestModel.getRole());

        return ResponseEntity.ok().location(dealershipAssembler.buildDealershipSelfLink(dealership.getId()).toUri()).build();
    }
}
