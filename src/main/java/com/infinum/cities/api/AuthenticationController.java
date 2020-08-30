package com.infinum.cities.api;

import com.infinum.cities.model.auth.AuthenticationRequest;
import com.infinum.cities.model.auth.AuthenticationResponse;
import com.infinum.cities.model.enums.AuthOperation;
import com.infinum.cities.service.AuthenticationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value =
        "Register new user with email and password and receive JWT token / " +
            "Login existing user with email and password and receive JWT token"
    )
    @PostMapping(value = "/{authOp}")
    public ResponseEntity<?> authenticate(
        @Valid @RequestBody AuthenticationRequest authRequest,
        @ApiParam(value = "register / login", required = true) @PathVariable AuthOperation authOp
    ) throws Exception {
        String token = authenticationService.authenticate(authRequest, authOp);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
