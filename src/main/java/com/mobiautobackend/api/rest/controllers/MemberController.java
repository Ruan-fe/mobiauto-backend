package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.api.rest.assemblers.MemberAssembler;
import com.mobiautobackend.api.rest.models.request.MemberRequestModel;
import com.mobiautobackend.api.rest.models.response.MemberResponseModel;
import com.mobiautobackend.domain.entities.Member;
import com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum;
import com.mobiautobackend.domain.exceptions.ConflictException;
import com.mobiautobackend.domain.exceptions.ForbiddenException;
import com.mobiautobackend.domain.exceptions.NotFoundException;
import com.mobiautobackend.domain.services.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum.NOT_AUTHORIZED;

@RestController
public class MemberController {

    public static final String MEMBER_RESOURCE_PATH = "/api/members";
    public static final String MEMBER_SELF_PATH = MEMBER_RESOURCE_PATH + "/{memberId}";

    private final MemberService memberService;
    private final MemberAssembler memberAssembler;

    @Autowired
    public MemberController(MemberService memberService,
                            MemberAssembler memberAssembler) {
        this.memberService = memberService;
        this.memberAssembler = memberAssembler;
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
        if (!memberService.isAnAuthorizedMember(memberId)) {
            throw new ForbiddenException(NOT_AUTHORIZED);
        }
        Member member = memberService.findById(memberId).orElseThrow(() ->
                new NotFoundException(ExceptionMessagesEnum.MEMBER_NOT_FOUND));
        return ResponseEntity.ok().body(memberAssembler.toModel(member));
    }
}
