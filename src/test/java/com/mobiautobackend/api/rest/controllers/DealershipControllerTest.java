package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.ApplicationTests;
import com.mobiautobackend.domain.enumeration.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.mobiautobackend.api.rest.controllers.DealershipController.*;
import static com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum.DEALERSHIP_NOT_FOUND;
import static com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum.MEMBER_NOT_FOUND;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

public class DealershipControllerTest extends ApplicationTests<DealershipControllerTest> {

    @Test
    public void shouldReturnOkWhenGetDealershipById() throws Exception {

        final String uri = fromPath(DEALERSHIP_SELF_PATH).buildAndExpand("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1").toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"))
                .andExpect(jsonPath("$.tradeName").value("Ruanzinho Veiculos"))
                .andExpect(jsonPath("$.cnpj").value("45572678000187"))
                .andExpect(jsonPath("$.creationDate").value("2024-04-23T23:01:40.619-03:00"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnNotFoundWhenGetDealershipByIdThatDoesNotExists() throws Exception {

        final String uri = fromPath(DEALERSHIP_SELF_PATH).buildAndExpand("DEALERSHIP_ID_DOES_NOT_EXISTS").toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(DEALERSHIP_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(DEALERSHIP_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnCreatedWhenPostDealership() throws Exception {
        final String uri = fromPath(DEALERSHIP_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnCreatedWhenPostDealership");

        MvcResult result = mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, containsString(uri)))
                .andReturn();

        mockMvc.perform(get(result.getResponse().getHeader(LOCATION)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.tradeName").value("Arigato Veiculos"))
                .andExpect(jsonPath("$.cnpj").value("20994146000107"))
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostDealershipAndNotExistsMemberId() throws Exception {
        final String uri = fromPath(DEALERSHIP_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostDealershipAndNotExistsMemberId");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(MEMBER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(MEMBER_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostDealershipWithNullTradeName() throws Exception {
        final String uri = fromPath(DEALERSHIP_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostDealershipWithNullTradeName");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("tradeName must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostDealershipWithNullCnpj() throws Exception {
        final String uri = fromPath(DEALERSHIP_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostDealershipWithNullCnpj");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("cnpj must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostDealershipWithInvalidCnpj() throws Exception {
        final String uri = fromPath(DEALERSHIP_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnCreatedWhenPostDealershipWithInvalidCnpj");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("cnpj invalid Brazilian corporate taxpayer registry number (CNPJ)"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostDealershipWithNullMemberId() throws Exception {
        final String uri = fromPath(DEALERSHIP_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostDealershipWithNullMemberId");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("memberId must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnOkWhenRegisterMemberInDealership() throws Exception {
        final String uri = fromPath(DEALERSHIP_REGISTER_MEMBER_PATH).buildAndExpand("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1").toUriString();
        final String uriGet = fromPath(DEALERSHIP_SELF_PATH).buildAndExpand("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1").toUriString();

        mockMvc.perform(get(uriGet))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"))
                .andExpect(jsonPath("$.tradeName").value("Ruanzinho Veiculos"))
                .andExpect(jsonPath("$.cnpj").value("45572678000187"))
                .andExpect(jsonPath("$.members[0].id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$.members[0].name").value("Ruan Amaral"))
                .andExpect(jsonPath("$.members[0].email").value("ruan@gmail.com"))
                .andExpect(jsonPath("$.members[0].role").value(MemberRole.OWNER.name()))
                .andExpect(jsonPath("$.members[1].id").doesNotExist())
                .andExpect(jsonPath("$.creationDate").value("2024-04-23T23:01:40.619-03:00"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uriGet)));

        String content = super.getScenarioBody("shouldReturnCreatedWhenRegisterMemberInDealership");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(LOCATION, containsString(uriGet)));

        mockMvc.perform(get(uriGet))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"))
                .andExpect(jsonPath("$.tradeName").value("Ruanzinho Veiculos"))
                .andExpect(jsonPath("$.cnpj").value("45572678000187"))
                .andExpect(jsonPath("$.members[0].id").value("411e816f-a2b3-4cc2-a622-ef71a49706da"))
                .andExpect(jsonPath("$.members[0].name").value("Guilherme Matias"))
                .andExpect(jsonPath("$.members[0].email").value("guilhermematias@gmail.com"))
                .andExpect(jsonPath("$.members[0].role").value(MemberRole.ASSISTANT.name()))
                .andExpect(jsonPath("$.members[1].id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$.members[1].name").value("Ruan Amaral"))
                .andExpect(jsonPath("$.members[1].email").value("ruan@gmail.com"))
                .andExpect(jsonPath("$.members[1].role").value(MemberRole.OWNER.name()))
                .andExpect(jsonPath("$.creationDate").value("2024-04-23T23:01:40.619-03:00"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uriGet)));
    }
}
