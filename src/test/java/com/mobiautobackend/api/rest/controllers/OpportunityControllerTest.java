package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.ApplicationTests;
import com.mobiautobackend.domain.enumeration.OpportunityStatus;
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
                .buildAndExpand("d172d900-55d6-45b1-aaf6-70d7d05928b5").toUriString();

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
                .andExpect(jsonPath("$.status").value(OpportunityStatus.NEW.name()))
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
                .toUriString().concat("?dealershipId=246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1&statuses=NEW,APPROVED");

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
                .andExpect(jsonPath("$._embedded.opportunities[0].status").value(OpportunityStatus.NEW.name()))
                .andExpect(jsonPath("$._embedded.opportunities[0].creationDate").value("2024-04-23T23:01:40.619-03:00"))
                .andExpect(jsonPath("$._embedded.opportunities[0]._links['self'].href").value(containsString(fromPath(OPPORTUNITY_RESOURCE_PATH)
                        .buildAndExpand("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1", "2e55b038-b7af-41b7-b1f8-3c5023f237ac").toUriString())))

                .andExpect(jsonPath("$._embedded.opportunities[1].id").value("caf7322e-8129-4a34-b285-33f3f8614d20"))
                .andExpect(jsonPath("$._embedded.opportunities[1].dealershipId").value("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"))
                .andExpect(jsonPath("$._embedded.opportunities[1].vehicleId").value("5c0dda90-b6e8-4f87-9189-b4cfc5be369d"))
                .andExpect(jsonPath("$._embedded.opportunities[1].status").value("APPROVED"))

                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    public void shouldReturnCreatedWhenPostOpportunity() throws Exception {
        final String uri = fromPath(OPPORTUNITY_RESOURCE_PATH).toUriString();

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
                .andExpect(jsonPath("$.status").value(OpportunityStatus.NEW.name()))
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnOkWhenAssignAnOpportunity() throws Exception {
        final String uri = fromPath(OPPORTUNITY_ASSIGN_PATH).buildAndExpand("3ba22468-db41-470c-adde-727aa66327d6").toUriString();
        final String uriSelf = fromPath(OPPORTUNITY_SELF_PATH).buildAndExpand("3ba22468-db41-470c-adde-727aa66327d6").toUriString();
        String content = super.getScenarioBody("shouldReturnCreatedWhenAssignAnOpportunityWithInProgressStatus");

        mockMvc.perform(get(uriSelf))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.dealershipId").value("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"))
                .andExpect(jsonPath("$.vehicleId").value("2e55b038-b7af-41b7-b1f8-3c5023f237ac"))
                .andExpect(jsonPath("$.memberId").doesNotExist())
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.customer.name").value("Martin Costa"))
                .andExpect(jsonPath("$.customer.email").value("martincosta@gmail.com"))
                .andExpect(jsonPath("$.customer.phone").value("18997845415"))
                .andExpect(jsonPath("$.status").value(OpportunityStatus.NEW.name()))
                .andExpect(jsonPath("$.assignDate").doesNotExist())
                .andExpect(jsonPath("$.conclusionDate").doesNotExist())
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uriSelf)));

         mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(LOCATION, containsString(uriSelf)));

        mockMvc.perform(get(uriSelf))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.dealershipId").value("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"))
                .andExpect(jsonPath("$.vehicleId").value("2e55b038-b7af-41b7-b1f8-3c5023f237ac"))
                .andExpect(jsonPath("$.memberId").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.customer.name").value("Martin Costa"))
                .andExpect(jsonPath("$.customer.email").value("martincosta@gmail.com"))
                .andExpect(jsonPath("$.customer.phone").value("18997845415"))
                .andExpect(jsonPath("$.status").value(OpportunityStatus.IN_PROGRESS.name()))
                .andExpect(jsonPath("$.assignDate").exists())
                .andExpect(jsonPath("$.conclusionDate").doesNotExist())
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uriSelf)));

        content = super.getScenarioBody("shouldReturnCreatedWhenAssignAnOpportunityWithApprovedStatus");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(LOCATION, containsString(uriSelf)));

        mockMvc.perform(get(uriSelf))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.dealershipId").value("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"))
                .andExpect(jsonPath("$.vehicleId").value("2e55b038-b7af-41b7-b1f8-3c5023f237ac"))
                .andExpect(jsonPath("$.memberId").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.customer.name").value("Martin Costa"))
                .andExpect(jsonPath("$.customer.email").value("martincosta@gmail.com"))
                .andExpect(jsonPath("$.customer.phone").value("18997845415"))
                .andExpect(jsonPath("$.status").value(OpportunityStatus.APPROVED.name()))
                .andExpect(jsonPath("$.assignDate").exists())
                .andExpect(jsonPath("$.conclusionDate").exists())
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uriSelf)));
    }

    @Test
    public void shouldReturnCreatedWhenPostOpportunityAndAssignAutomatic() throws Exception {
        final String uri = fromPath(OPPORTUNITY_RESOURCE_PATH).toUriString();

        //Both assistants with the same number of opportunities in progress
        String content = super.getScenarioBody("shouldReturnCreatedWhenPostOpportunityAndAssignAutomatic")
                .replace("{vehicleId}", "bcc1ca1d-7b4c-497f-9a3f-ec7b5a749deb");

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
                .andExpect(jsonPath("$.dealershipId").value("2721d31f-a83d-4f6d-b1ac-f8fd74ca19c2"))
                .andExpect(jsonPath("$.vehicleId").value("bcc1ca1d-7b4c-497f-9a3f-ec7b5a749deb"))
                .andExpect(jsonPath("$.memberId").value("f94c4e97-1933-46c7-b08d-a7f40830a69a"))
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.customer.name").value("Palin Sena"))
                .andExpect(jsonPath("$.customer.email").value("palinsena@gmail.com"))
                .andExpect(jsonPath("$.customer.phone").value("18997845420"))
                .andExpect(jsonPath("$.status").value(OpportunityStatus.IN_PROGRESS.name()))
                .andExpect(jsonPath("$.assignDate").exists())
                .andExpect(jsonPath("$.conclusionDate").doesNotExist())
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));

        //Some assistant with no opportunities in progress
        content = super.getScenarioBody("shouldReturnCreatedWhenPostOpportunityAndAssignAutomatic")
                .replace("{vehicleId}", "f4508e32-4e40-4c2c-ac7a-3cd28ebb6762");

        result = mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, containsString(uri)))
                .andReturn();

        mockMvc.perform(get(result.getResponse().getHeader(LOCATION)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.dealershipId").value("a66a9197-faf7-4eeb-a64f-706a5566ad5e"))
                .andExpect(jsonPath("$.vehicleId").value("f4508e32-4e40-4c2c-ac7a-3cd28ebb6762"))
                .andExpect(jsonPath("$.memberId").value("8a143c6f-c406-4d81-a413-f7b47a0bee06"))
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.customer.name").value("Palin Sena"))
                .andExpect(jsonPath("$.customer.email").value("palinsena@gmail.com"))
                .andExpect(jsonPath("$.customer.phone").value("18997845420"))
                .andExpect(jsonPath("$.status").value(OpportunityStatus.IN_PROGRESS.name()))
                .andExpect(jsonPath("$.assignDate").exists())
                .andExpect(jsonPath("$.conclusionDate").doesNotExist())
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));

        //No assistant with opportunities in progress
        content = super.getScenarioBody("shouldReturnCreatedWhenPostOpportunityAndAssignAutomatic")
                .replace("{vehicleId}", "4be2aebe-e2a2-4833-ab85-d29fd6c77835");

        result = mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, containsString(uri)))
                .andReturn();

        mockMvc.perform(get(result.getResponse().getHeader(LOCATION)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.dealershipId").value("b1fc5b09-75ea-46dd-9290-9c1760686c3a"))
                .andExpect(jsonPath("$.vehicleId").value("4be2aebe-e2a2-4833-ab85-d29fd6c77835"))
                .andExpect(jsonPath("$.memberId").exists())
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.customer.name").value("Palin Sena"))
                .andExpect(jsonPath("$.customer.email").value("palinsena@gmail.com"))
                .andExpect(jsonPath("$.customer.phone").value("18997845420"))
                .andExpect(jsonPath("$.status").value(OpportunityStatus.IN_PROGRESS.name()))
                .andExpect(jsonPath("$.assignDate").exists())
                .andExpect(jsonPath("$.conclusionDate").doesNotExist())
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }
}
