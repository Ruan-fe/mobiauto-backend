package com.mobiautobackend.api.rest.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;

@JsonPropertyOrder({
        "token"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberAuthResponseModel extends RepresentationModel<MemberAuthResponseModel> {

    @JsonProperty("token")
    private String token;

    public MemberAuthResponseModel() {
    }

    public MemberAuthResponseModel(String token) {
        this.token = token;
    }
}
