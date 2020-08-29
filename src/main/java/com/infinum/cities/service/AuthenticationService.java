package com.infinum.cities.service;

import com.infinum.cities.auth.util.TokenUtil;
import com.infinum.cities.exception.PatchOperationNotFoundException;
import com.infinum.cities.model.auth.AuthenticationRequest;
import com.infinum.cities.model.enums.AuthOperation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    public String authenticate(AuthenticationRequest authRequest, AuthOperation authOp) throws Exception {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(),
                    authRequest.getPassword())
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        UserDetails userDetails;
        switch (authOp) {
            case register:
                userDetails = registerUser(authRequest);
                break;
            case login:
                userDetails = loginUser(authRequest);
                break;
            default:
                throw new PatchOperationNotFoundException(
                    "Patch operation " + authOp + " not found"
                );
        }

        return tokenUtil.generateToken(userDetails);
    }

    private UserDetails registerUser(AuthenticationRequest authenticationRequest) {
        return userService.save(
            authenticationRequest.getEmail(),
            passwordEncoder.encode(authenticationRequest.getPassword())
        );
    }

    private UserDetails loginUser(AuthenticationRequest authenticationRequest) {
        return userService.loadUserByUsername(authenticationRequest.getEmail());
    }
}
