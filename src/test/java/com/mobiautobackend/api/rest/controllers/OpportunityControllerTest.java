package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.ApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.mobiautobackend.api.rest.controllers.OpportunityController.*;
import static com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum.OPPORTUNITY_NOT_FOUND;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

public class OpportunityControllerTest extends ApplicationTests<OpportunityControllerTest> {

    @Test
    public void shouldReturnOkWhenGetOpportunityById() throws Exception {

        final String uri = fromPath(OPPORTUNITY_SELF_PATH)
                .buildAndExpand("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1", "2e55b038-b7af-41b7-b1f8-3c5023f237ac",
                        "d172d900-55d6-45b1-aaf6-70d7d05928b5").toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("d172d900-55d6-45b1-aaf6-70d7d05928b5"))
                .andExpect(jsonPath("$.dealershipId").value("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"))
                .andExpect(jsonPath("$.vehicleId").value("2e55b038-b7af-41b7-b1f8-3c5023f237ac"))
                .andExpect(jsonPath("$.memberId").doesNotExist())
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.customer.name").value("Vitor Hugo"))
                .andExpect(jsonPath("$.customer.email").value("vitorhugo@gmail.com"))
                .andExpect(jsonPath("$.customer.phone").value("18997845412"))
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.creationDate").value("2024-04-23T23:01:40.619-03:00"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnNotFoundWhenGetOpportunityByIdThatDoesNotExists() throws Exception {

        final String uri = fromPath(OPPORTUNITY_SELF_PATH)
                .buildAndExpand("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1", "2e55b038-b7af-41b7-b1f8-3c5023f237ac",
                        "NOT_EXISTS").toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(OPPORTUNITY_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(OPPORTUNITY_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnOkWhenGetOpportunityByFilters() throws Exception {

        String uri = fromPath(OPPORTUNITY_RESOURCE_PATH)
                .buildAndExpand("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1", "2e55b038-b7af-41b7-b1f8-3c5023f237ac")
                .toUriString().concat("?statuses=NEW,APPROVED");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.opportunities[0].id").value("d172d900-55d6-45b1-aaf6-70d7d05928b5"))
                .andExpect(jsonPath("$._embedded.opportunities[0].dealershipId").value("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"))
                .andExpect(jsonPath("$._embedded.opportunities[0].vehicleId").value("2e55b038-b7af-41b7-b1f8-3c5023f237ac"))
                .andExpect(jsonPath("$._embedded.opportunities[0].memberId").doesNotExist())
                .andExpect(jsonPath("$._embedded.opportunities[0].customer").exists())
                .andExpect(jsonPath("$._embedded.opportunities[0].customer.name").value("Vitor Hugo"))
                .andExpect(jsonPath("$._embedded.opportunities[0].customer.email").value("vitorhugo@gmail.com"))
                .andExpect(jsonPath("$._embedded.opportunities[0].customer.phone").value("18997845412"))
                .andExpect(jsonPath("$._embedded.opportunities[0].status").value("NEW"))
                .andExpect(jsonPath("$._embedded.opportunities[0].creationDate").value("2024-04-23T23:01:40.619-03:00"))
                .andExpect(jsonPath("$._embedded.opportunities[0]._links['self'].href").value(containsString(fromPath(OPPORTUNITY_RESOURCE_PATH)
                        .buildAndExpand("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1", "2e55b038-b7af-41b7-b1f8-3c5023f237ac").toUriString())))
                .andExpect(jsonPath("$._embedded.opportunities[1].id").doesNotExist())
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));

        uri = fromPath(OPPORTUNITY_PATH)
                .buildAndExpand("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1").toUriString().concat("?statuses=APPROVED");

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.opportunities[0].id").value("caf7322e-8129-4a34-b285-33f3f8614d20"))
                .andExpect(jsonPath("$._embedded.opportunities[0].dealershipId").value("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"))
                .andExpect(jsonPath("$._embedded.opportunities[0].vehicleId").value("5c0dda90-b6e8-4f87-9189-b4cfc5be369d"))
                .andExpect(jsonPath("$._embedded.opportunities[0].memberId").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$._embedded.opportunities[0].customer").exists())
                .andExpect(jsonPath("$._embedded.opportunities[0].customer.name").value("Diego Silva"))
                .andExpect(jsonPath("$._embedded.opportunities[0].customer.email").value("diegosilva@gmail.com"))
                .andExpect(jsonPath("$._embedded.opportunities[0].customer.phone").value("18997845414"))
                .andExpect(jsonPath("$._embedded.opportunities[0].status").value("APPROVED"))
                .andExpect(jsonPath("$._embedded.opportunities[0].reason").value("Venda concluída com sucesso"))
                .andExpect(jsonPath("$._embedded.opportunities[0].creationDate").value("2024-04-23T23:01:40.619-03:00"))
                .andExpect(jsonPath("$._embedded.opportunities[0]._links['self'].href").value(containsString(fromPath(OPPORTUNITY_RESOURCE_PATH)
                        .buildAndExpand("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1", "5c0dda90-b6e8-4f87-9189-b4cfc5be369d").toUriString())))
                .andExpect(jsonPath("$._embedded.opportunities[1].id").doesNotExist())
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnCreatedWhenPostOpportunity() throws Exception {
        final String uri = fromPath(OPPORTUNITY_RESOURCE_PATH)
                .buildAndExpand("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1", "2e55b038-b7af-41b7-b1f8-3c5023f237ac").toUriString();

        String content = super.getScenarioBody("shouldReturnCreatedWhenPostOpportunity");

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
                .andExpect(jsonPath("$.dealershipId").value("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"))
                .andExpect(jsonPath("$.vehicleId").value("2e55b038-b7af-41b7-b1f8-3c5023f237ac"))
                .andExpect(jsonPath("$.memberId").doesNotExist())
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.customer.name").value("Palin Sena"))
                .andExpect(jsonPath("$.customer.email").value("palinsena@gmail.com"))
                .andExpect(jsonPath("$.customer.phone").value("18997845420"))
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }
}
