package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.ApplicationTests;
import com.mobiautobackend.domain.enumeration.VehicleBrandType;
import com.mobiautobackend.domain.enumeration.VehicleColor;
import com.mobiautobackend.domain.enumeration.VehicleFuelType;
import com.mobiautobackend.domain.enumeration.VehicleTransmissionType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.mobiautobackend.api.rest.controllers.VehicleController.VEHICLE_RESOURCE_PATH;
import static com.mobiautobackend.api.rest.controllers.VehicleController.VEHICLE_SELF_PATH;
import static com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum.DEALERSHIP_NOT_FOUND;
import static com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum.VEHICLE_NOT_FOUND;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

public class VehicleControllerTest extends ApplicationTests<VehicleControllerTest> {

    @Test
    public void shouldReturnOkWhenGetVehicleById() throws Exception {
        final String uri = fromPath(VEHICLE_SELF_PATH).buildAndExpand("2e55b038-b7af-41b7-b1f8-3c5023f237ac").toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2e55b038-b7af-41b7-b1f8-3c5023f237ac"))
                .andExpect(jsonPath("$.dealershipId").value("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"))
                .andExpect(jsonPath("$.licensePlate").value("FOI-4674"))
                .andExpect(jsonPath("$.brandType").value(VehicleBrandType.VOLKSWAGEN.name()))
                .andExpect(jsonPath("$.model").value("AMAROK"))
                .andExpect(jsonPath("$.manufacturingYear").value("2022"))
                .andExpect(jsonPath("$.modelYear").value("2022"))
                .andExpect(jsonPath("$.version").value("Highline 3.0 CD 4x4"))
                .andExpect(jsonPath("$.transmissionType").value(VehicleTransmissionType.AUTOMATIC.name()))
                .andExpect(jsonPath("$.fuelType").value(VehicleFuelType.DIESEL.name()))
                .andExpect(jsonPath("$.doors").value("4"))
                .andExpect(jsonPath("$.color").value(VehicleColor.BLACK.name()))
                .andExpect(jsonPath("$.creationDate").value("2024-04-23T23:01:40.619-03:00"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnOkWhenGetVehicleByIdThatDoesNotExists() throws Exception {
        final String uri = fromPath(VEHICLE_SELF_PATH).buildAndExpand("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1",
                "NOT_EXISTS").toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(VEHICLE_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(VEHICLE_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnOkWhenGetVehicleByIdAndDealershipIdDoesNotExists() throws Exception {
        final String uri = fromPath(VEHICLE_SELF_PATH).buildAndExpand("NOT_EXISTS",
                "2e55b038-b7af-41b7-b1f8-3c5023f237ac").toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(VEHICLE_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(VEHICLE_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnCreatedWhenPostVehicle() throws Exception {
        final String uri = fromPath(VEHICLE_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnCreatedWhenPostVehicle");

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
                .andExpect(jsonPath("$.licensePlate").value("ARE-1245"))
                .andExpect(jsonPath("$.brandType").value(VehicleBrandType.VOLKSWAGEN.name()))
                .andExpect(jsonPath("$.model").value("Gol"))
                .andExpect(jsonPath("$.manufacturingYear").value("2016"))
                .andExpect(jsonPath("$.modelYear").value("2017"))
                .andExpect(jsonPath("$.version").value("1.6 MSI Highline"))
                .andExpect(jsonPath("$.transmissionType").value(VehicleTransmissionType.MANUAL.name()))
                .andExpect(jsonPath("$.fuelType").value(VehicleFuelType.GASOLINE.name()))
                .andExpect(jsonPath("$.doors").value("4"))
                .andExpect(jsonPath("$.color").value(VehicleColor.WHITE.name()))
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostVehicleAndNotExistsDealershipId() throws Exception {
        final String uri = fromPath(VEHICLE_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostVehicleAndNotExistsDealershipId");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(DEALERSHIP_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errors.messages[0].message").value(DEALERSHIP_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostVehicleWithNullBrandType() throws Exception {
        final String uri = fromPath(VEHICLE_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostVehicleWithNullBrandType");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("brandType must not be null"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostVehicleWithNullFuelType() throws Exception {
        final String uri = fromPath(VEHICLE_RESOURCE_PATH).toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostVehicleWithNullFuelType");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message").value("fuelType must not be null"))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }

    @Test
    public void shouldReturnBadRequestWhenPostVehicleWithInvalidFuelType() throws Exception {
        final String uri = fromPath(VEHICLE_RESOURCE_PATH)
                .buildAndExpand("246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1").toUriString();

        String content = super.getScenarioBody("shouldReturnBadRequestWhenPostVehicleWithInvalidFuelType");

        mockMvc.perform(post(uri)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.messages").exists())
                .andExpect(jsonPath("$.errors.messages[0].code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.messages[0].message")
                        .value(containsString("not one of the values accepted for Enum class: [ELECTRICITY, MULTIFLEX, NATURAL_GAS, FLEX, DIESEL, HYBRID, GASOLINE]")))
                .andExpect(jsonPath("$._links['self'].href").value(containsString(uri)));
    }
}
