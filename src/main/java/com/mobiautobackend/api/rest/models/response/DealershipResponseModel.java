package com.mobiautobackend.api.rest.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mobiautobackend.domain.entities.Dealership;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@JsonPropertyOrder({
        "id",
        "tradeName",
        "cnpj",
        "members",
        "creationDate"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "dealership", collectionRelation = "dealerships")
public class DealershipResponseModel extends RepresentationModel<DealershipResponseModel> {

    @JsonProperty("id")
    private String id;

    @JsonProperty("tradeName")
    private String tradeName;

    @JsonProperty("cnpj")
    private String cnpj;

    @JsonProperty("members")
    private List<MemberResponseModel> members; //TODO TALVEZ REMOVER?

    @JsonProperty("creationDate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime creationDate;

    public DealershipResponseModel() {
    }

    public DealershipResponseModel(Dealership dealership) {
        this.id = dealership.getId();
        this.tradeName = dealership.getTradeName();
        this.cnpj = dealership.getCnpj();
        this.members = dealership.getMembers().stream().map(MemberResponseModel::new).collect(Collectors.toList());
        this.creationDate = dealership.getCreationDate();
    }
}
