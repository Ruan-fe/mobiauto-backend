package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.ApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.mobiautobackend.api.rest.controllers.MemberController.MEMBER_RESOURCE_PATH;
import static com.mobiautobackend.api.rest.controllers.MemberController.MEMBER_SELF_PATH;
import static com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum.MEMBER_NOT_FOUND;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

public class MemberControllerTest extends ApplicationTests<MemberControllerTest> {

    @Test
    public void shouldReturnOkWhenGetMemberById() throws Exception {

        final String uri = fromPath(MEMBER_SELF_PATH).buildAndExpand("a5993416-4255-11ec-71d3-0242ac130004").toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("a5993416-4255-11ec-71d3-0242ac130004"))
                .andExpect(jsonPath("$.name").value("Ruan Amaral"))
                .andExpect(jsonPath("$.email").value("ruan@gmail.com"))
                .andExpect(jsonPath("$.role").value("OWNER"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.creationDate").value("2024-04-23T23:01:40.619-03:00"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnNotFoundWhenGetMemberByIdThatDoesNotExists() throws Exception {

        final String uri = fromPath(MEMBER_SELF_PATH).buildAndExpand("MEMBER_ID_DOES_NOT_EXISTS").toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(MEMBER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(MEMBER_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnCreatedWhenPostMember() throws Exception {
        final String uri = fromPath(MEMBER_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnCreatedWhenPostMember");

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
                .andExpect(jsonPath("$.name").value("Carlos Theodoro"))
                .andExpect(jsonPath("$.email").value("carlostheodoro@gmail.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostMemberWithNullName() throws Exception {
        final String uri = fromPath(MEMBER_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostMemberWithNullName");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("name must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostMemberWithNullEmail() throws Exception {
        final String uri = fromPath(MEMBER_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostMemberWithNullEmail");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("email must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostMemberWithInvalidEmail() throws Exception {
        final String uri = fromPath(MEMBER_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostMemberWithInvalidEmail");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("email must be a well-formed email address"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }
    @Test
    public void shouldReturnBadRequestWhenPostMemberWithNullPassword() throws Exception {
        final String uri = fromPath(MEMBER_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostMemberWithNullPassword");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("password must not be blank"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }
}
