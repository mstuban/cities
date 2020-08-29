package com.infinum.cities.service;

import com.infinum.cities.auth.util.TokenUtil;
import com.infinum.cities.exception.UserNotFoundException;
import com.infinum.cities.model.auth.AuthenticationRequest;
import org.springframework.security.authentication.*;
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

    public String authenticate(
        AuthenticationRequest authenticationRequest,
        String email,
        String password
    ) throws Exception {
        boolean userExists = true;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (InternalAuthenticationServiceException e) {
            if (e.getCause() instanceof UserNotFoundException) {
                userExists = false;
            }
        }

        UserDetails userDetails;
        if (userExists) {
            userDetails = userService.loadUserByUsername(authenticationRequest.getEmail());
        } else {
            userDetails = userService.save(email, passwordEncoder.encode(password));
        }

        return tokenUtil.generateToken(userDetails);
    }
}
