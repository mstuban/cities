package com.infinum.cities.api;

import com.infinum.cities.auth.util.TokenUtil;
import com.infinum.cities.exception.UserNotFoundException;
import com.infinum.cities.model.User;
import com.infinum.cities.model.enums.AuthOperation;
import com.infinum.cities.service.AuthenticationService;
import com.infinum.cities.stub.AuthenticationRequestStubFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import javax.persistence.EntityExistsException;

import static com.infinum.cities.utils.JsonUtil.toJson;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService service;

    @Mock
    private TokenUtil tokenUtil;

    private AuthenticationController controller;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new AuthenticationController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testRegister() throws Exception {
        // prepare ...
        String token = tokenUtil.generateToken(
            new User(
                AuthenticationRequestStubFactory.authRequest.getEmail(),
                AuthenticationRequestStubFactory.authRequest.getPassword()
            )
        );
        when(
            service.authenticate(
                AuthenticationRequestStubFactory.authRequest,
                AuthOperation.register
            )
        )
            .thenReturn(token);

        // act & assert ...
        mockMvc.perform(
            post("/api/auth/register/")
                .content(toJson(AuthenticationRequestStubFactory.authRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.token", equalTo(token)));
    }

    @Test
    public void testRegisterWhenUserAlreadyExists() throws Exception {
        // prepare ...
        when(
            service.authenticate(
                AuthenticationRequestStubFactory.authRequest,
                AuthOperation.register
            )
        )
            .thenThrow(
                new EntityExistsException(
                    "User " + AuthenticationRequestStubFactory.authRequest.getEmail() + " already exists"
                )
            );

        // act & assert ...
        assertThrows(NestedServletException.class, () -> mockMvc.perform(
            post("/api/auth/register")
                .content(toJson(AuthenticationRequestStubFactory.authRequest))
                .contentType(MediaType.APPLICATION_JSON)
            )
        );
    }

    @Test
    public void testLoginWhenUserExists() throws Exception {
        // prepare ...
        String token = tokenUtil.generateToken(
            new User(
                AuthenticationRequestStubFactory.authRequest.getEmail(),
                AuthenticationRequestStubFactory.authRequest.getPassword()
            )
        );
        when(
            service.authenticate(
                AuthenticationRequestStubFactory.authRequest,
                AuthOperation.login
            )
        )
            .thenReturn(token);

        // act & assert ...
        mockMvc.perform(
            post("/api/auth/login")
                .content(toJson(AuthenticationRequestStubFactory.authRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.token", equalTo(token)));
    }

    @Test
    public void testLoginWhenUserDoesNotExist() throws Exception {
        // prepare ...
        when(
            service.authenticate(
                AuthenticationRequestStubFactory.authRequest,
                AuthOperation.login
            )
        )
            .thenThrow(
                new UserNotFoundException(
                    "User " + AuthenticationRequestStubFactory.authRequest.getEmail() + " not found"
                )
            );

        // act & assert ...
        mockMvc.perform(
            post("/api/auth/login")
                .content(toJson(AuthenticationRequestStubFactory.authRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }
}
