package com.infinum.cities.api;

import com.infinum.cities.model.auth.AuthenticationRequest;
import com.infinum.cities.model.auth.AuthenticationResponse;
import com.infinum.cities.service.AuthenticationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ApiOperation(value = "Authenticate new / existing with email and password and receive JWT token")
    @RequestMapping(value = "/api/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
        @Valid @RequestBody AuthenticationRequest authenticationRequest
    ) throws Exception {
        String token = authenticationService.authenticate(
            authenticationRequest,
            authenticationRequest.getEmail(),
            authenticationRequest.getPassword()
        );

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
