package com.infinum.cities.service;

import com.infinum.cities.auth.util.TokenUtil;
import com.infinum.cities.model.auth.AuthenticationRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final TokenUtil tokenUtil;

    private final UserService userService;

    public AuthenticationService(
        AuthenticationManager authenticationManager,
        TokenUtil tokenUtil,
        UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenUtil = tokenUtil;
        this.userService = userService;
    }

    public String authenticate(
        AuthenticationRequest authenticationRequest,
        String email,
        String password
    ) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getEmail());

        return tokenUtil.generateToken(userDetails);
    }
}
