package com.mobiautobackend.api.rest.controllers;

import com.mobiautobackend.api.rest.models.request.MemberAuthRequestModel;
import com.mobiautobackend.api.rest.models.response.AuthenticationResponseModel;
import com.mobiautobackend.domain.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    public static final String AUTHENTICATION_RESOURCE_PATH = "/api/authenticate";

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(AUTHENTICATION_RESOURCE_PATH)
    public ResponseEntity<AuthenticationResponseModel> authenticate(@RequestBody @Valid MemberAuthRequestModel memberAuthRequestModel) {
        String token = authenticationService.authenticate(memberAuthRequestModel.getEmail(), memberAuthRequestModel.getPassword());
        return ResponseEntity.ok().body(new AuthenticationResponseModel(token));
    }
}
