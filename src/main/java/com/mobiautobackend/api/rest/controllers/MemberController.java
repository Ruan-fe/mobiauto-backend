package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.api.rest.assemblers.MemberAssembler;
import com.mobiautobackend.api.rest.models.request.MemberAuthRequestModel;
import com.mobiautobackend.api.rest.models.request.MemberRequestModel;
import com.mobiautobackend.api.rest.models.response.MemberAuthResponseModel;
import com.mobiautobackend.api.rest.models.response.MemberResponseModel;
import com.mobiautobackend.domain.entities.Member;
import com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum;
import com.mobiautobackend.domain.exceptions.ConflictException;
import com.mobiautobackend.domain.exceptions.NotFoundException;
import com.mobiautobackend.domain.services.MemberService;
import com.mobiautobackend.domain.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {

    public static final String MEMBER_RESOURCE_PATH = "/api/members";
    public static final String MEMBER_SELF_PATH = MEMBER_RESOURCE_PATH + "/{memberId}";
    public static final String MEMBER_AUTH_PATH = MEMBER_RESOURCE_PATH + "/authenticate";

    private final MemberService memberService;
    private final MemberAssembler memberAssembler;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;


    @Autowired
    public MemberController(MemberService memberService,
                            MemberAssembler memberAssembler, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.memberService = memberService;
        this.memberAssembler = memberAssembler;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping(MEMBER_RESOURCE_PATH)
    public ResponseEntity<?> create(@RequestBody @Valid MemberRequestModel memberRequestModel) {
        Member member = memberAssembler.toEntity(memberRequestModel);

        memberService.findByEmail(member.getEmail()).ifPresent(searchedMember -> {
            throw new ConflictException(ExceptionMessagesEnum.MEMBER_ALREADY_EXISTS,
                    memberAssembler.buildMemberSelfLink(searchedMember.getId()).toUri());
        });

        member = memberService.create(member);

        return ResponseEntity.created(memberAssembler.buildMemberSelfLink(member.getId()).toUri()).build();
    }

    @GetMapping(MEMBER_SELF_PATH)
    public ResponseEntity<MemberResponseModel> findById(@PathVariable("memberId") final String memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() ->
                new NotFoundException(ExceptionMessagesEnum.MEMBER_NOT_FOUND));
        return ResponseEntity.ok().body(memberAssembler.toModel(member));
    }

    @PostMapping(MEMBER_AUTH_PATH)
    public ResponseEntity<MemberAuthResponseModel> authenticate(@RequestBody @Valid MemberAuthRequestModel memberAuthRequestModel) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(memberAuthRequestModel.getEmail(), memberAuthRequestModel.getPassword()));
        String token = tokenService.generateToken((Member) authentication.getPrincipal());
        return ResponseEntity.ok().body(memberAssembler.toModel(token));
    }
}
