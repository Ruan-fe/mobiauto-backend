package com.mobiautobackend.api.rest.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public class DealershipRequestModel {

    @NotBlank
    @JsonProperty("tradeName")
    private String tradeName;

    @NotBlank
    @CNPJ
    @JsonProperty("cnpj")
    private String cnpj;

    public String getTradeName() {
        return tradeName;
    }

    public String getCnpj() {
        return cnpj;
    }
}
