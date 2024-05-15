package com.mobiautobackend.api.rest.assemblers;

import com.mobiautobackend.api.rest.controllers.MemberController;
import com.mobiautobackend.api.rest.models.request.MemberRequestModel;
import com.mobiautobackend.api.rest.models.response.MemberResponseModel;
import com.mobiautobackend.domain.entities.Member;
import com.mobiautobackend.domain.enumeration.MemberRole;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MemberAssembler extends RepresentationModelAssemblerSupport<Member, MemberResponseModel> {

    public MemberAssembler() {
        super(MemberController.class, MemberResponseModel.class);
    }

    @Override
    public MemberResponseModel toModel(Member member) {
        MemberResponseModel memberResponseModel = new MemberResponseModel(member);
        memberResponseModel.add(this.buildMemberSelfLink(member.getId()));
        return memberResponseModel;
    }

    public Member toEntity(MemberRequestModel memberRequestModel) {
        Member member = new Member();
        member.setName(memberRequestModel.getName());
        member.setEmail(memberRequestModel.getEmail());
        member.setPassword(new BCryptPasswordEncoder().encode(memberRequestModel.getPassword()));
        member.setRole(MemberRole.USER);
        return member;
    }

    public Link buildMemberSelfLink(String memberId) {
        return linkTo(methodOn(MemberController.class).findById(memberId)).withSelfRel();
    }
}
