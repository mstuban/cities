package com.infinum.cities.api;

import com.infinum.cities.model.auth.AuthenticationRequest;
import com.infinum.cities.model.auth.AuthenticationResponse;
import com.infinum.cities.model.enums.AuthOperation;
import com.infinum.cities.service.AuthenticationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ApiOperation(value = "Register new user with email and password and receive JWT token")
    @PostMapping(value = "/register")
    public ResponseEntity<?> register(
        @Valid @RequestBody AuthenticationRequest authRequest
    ) throws Exception {
        String token = authenticationService.authenticate(authRequest, AuthOperation.register);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @ApiOperation(value = "Login existing user with with email and password and receive JWT token")
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(
        @Valid @RequestBody AuthenticationRequest authRequest
    ) throws Exception {
        String token = authenticationService.authenticate(authRequest, AuthOperation.login);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
