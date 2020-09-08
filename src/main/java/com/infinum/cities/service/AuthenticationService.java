package com.infinum.cities.service;

import com.infinum.cities.auth.util.TokenUtil;
import com.infinum.cities.model.auth.AuthenticationRequest;
import com.infinum.cities.model.enums.AuthOperation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final TokenUtil tokenUtil;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            TokenUtil tokenUtil,
            UserService userService,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenUtil = tokenUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(AuthenticationRequest authRequest, AuthOperation authOp) {
        if (authOp == AuthOperation.register) {
            createUser(authRequest);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        return tokenUtil.generateToken((UserDetails) authentication.getPrincipal());
    }

    private UserDetails createUser(AuthenticationRequest authenticationRequest) {
        return userService.save(
                authenticationRequest.getEmail(),
                passwordEncoder.encode(authenticationRequest.getPassword())
        );
    }
}
