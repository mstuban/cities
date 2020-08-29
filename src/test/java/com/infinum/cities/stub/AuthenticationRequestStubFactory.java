package com.infinum.cities.stub;

import com.infinum.cities.model.auth.AuthenticationRequest;

public class AuthenticationRequestStubFactory {
    public static AuthenticationRequest authRequest = new AuthenticationRequest(
        "johnny.cash@gmail.com",
        "pass"
    );
}
