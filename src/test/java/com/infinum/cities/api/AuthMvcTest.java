package com.infinum.cities.api;

import com.infinum.cities.CitiesApplication;
import com.infinum.cities.auth.util.TokenUtil;
import com.infinum.cities.model.User;
import com.infinum.cities.repository.UserRepository;
import com.infinum.cities.service.UserService;
import com.infinum.cities.stub.AuthenticationRequestStubFactory;
import com.infinum.cities.stub.CityStubFactory;
import com.infinum.cities.utils.JsonUtil;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = CitiesApplication.class)
public class AuthMvcTest {

    @Autowired
    public WebApplicationContext wac;

    @Autowired
    public FilterChainProxy springSecurityFilterChain;

    public MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenUtil tokenUtil;

    private String token;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(this.wac)
            .addFilter(springSecurityFilterChain).build();

        if (userRepository.findByEmail(AuthenticationRequestStubFactory.authRequest.getEmail()).isEmpty()) {
            userService.save(
                AuthenticationRequestStubFactory.authRequest.getEmail(),
                AuthenticationRequestStubFactory.authRequest.getPassword()
            );
        }

        token = tokenUtil.generateToken(
            new User(
                AuthenticationRequestStubFactory.authRequest.getEmail(),
                AuthenticationRequestStubFactory.authRequest.getPassword()
            )
        );
    }

    @Test
    public void testSave() throws Exception {
        // act & assert ...
        mockMvc.perform(
            post("/api/city/save")
                .content(
                    JsonUtil.toJson(CityStubFactory.mockCity)
                )
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.id").value("11"))
            .andExpect(jsonPath("$.name").value("Denver"))
            .andExpect(jsonPath("$.description").value("Denver"));
    }

    @Test
    public void testSaveWhenTokenInvalid() {
        // act & assert ...
        assertThrows(MalformedJwtException.class, () ->
            mockMvc.perform(
                post("/api/city/save")
                    .content(
                        JsonUtil.toJson(CityStubFactory.mockCity)
                    )
                    .header("Authorization", "Bearer " + "INVALID_TOKEN")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isUnauthorized())
        );
    }

    @Test
    public void testSaveWhenTokenHasExpired() {
        // act & assert ...
        assertThrows(MalformedJwtException.class, () ->
            mockMvc.perform(
                post("/api/city/save")
                    .content(
                        JsonUtil.toJson(CityStubFactory.mockCity)
                    )
                    .header("Authorization", "Bearer " + "INVALID_TOKEN")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isUnauthorized())
        );
    }
}
