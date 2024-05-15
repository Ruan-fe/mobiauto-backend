package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.api.rest.assemblers.DealershipAssembler;
import com.mobiautobackend.api.rest.models.request.DealershipRequestModel;
import com.mobiautobackend.api.rest.models.response.DealershipResponseModel;
import com.mobiautobackend.domain.entities.Dealership;
import com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum;
import com.mobiautobackend.domain.exceptions.BadRequestException;
import com.mobiautobackend.domain.exceptions.ConflictException;
import com.mobiautobackend.domain.exceptions.NotFoundException;
import com.mobiautobackend.domain.services.DealershipService;
import com.mobiautobackend.domain.services.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DealershipController {

    public static final String DEALERSHIP_RESOURCE_PATH = "/api/dealerships";
    public static final String DEALERSHIP_SELF_PATH = DEALERSHIP_RESOURCE_PATH + "/{dealershipId}";

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
        if (!memberService.isAllowed(dealershipRequestModel.getMemberId())) {
            //TODO lançar exception
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
}
