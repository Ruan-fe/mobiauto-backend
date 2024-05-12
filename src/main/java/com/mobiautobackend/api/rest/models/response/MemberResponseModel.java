package com.mobiautobackend.api.rest.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mobiautobackend.domain.entities.Member;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.ZonedDateTime;

@JsonPropertyOrder({
        "id",
        "name",
        "email",
        "role",
        "creationDate"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "member", collectionRelation = "members")
public class MemberResponseModel extends RepresentationModel<MemberResponseModel> {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private String role;

    @JsonProperty("creationDate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime creationDate;

    public MemberResponseModel() {
    }

    public MemberResponseModel(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.role = member.getRole();
        this.creationDate = member.getCreationDate();
    }
}
