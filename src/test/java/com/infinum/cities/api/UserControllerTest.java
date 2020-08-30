package com.infinum.cities.api;

import com.infinum.cities.auth.util.TokenUtil;
import com.infinum.cities.config.SecurityTestConfiguration;
import com.infinum.cities.model.User;
import com.infinum.cities.model.enums.PatchOperation;
import com.infinum.cities.service.CityService;
import com.infinum.cities.service.UserService;
import com.infinum.cities.stub.AuthenticationRequestStubFactory;
import com.infinum.cities.stub.CityStubFactory;
import com.infinum.cities.stub.UserStubFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UserController.class)
@ContextConfiguration
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@Import({SecurityTestConfiguration.class})
public class UserControllerTest {
    @Mock
    private UserService service;

    @Mock
    private CityService cityService;

    @Mock
    private TokenUtil tokenUtil;

    private UserController controller;

    private MockMvc mockMvc;

    private String token;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new UserController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        token = tokenUtil.generateToken(
            new User(
                AuthenticationRequestStubFactory.authRequest.getEmail(),
                AuthenticationRequestStubFactory.authRequest.getPassword()
            )
        );
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testModifyFavoriteCitiesWhenAdd() throws Exception {
        // prepare ...
        when(cityService.findByName("New York")).thenReturn(java.util.Optional.of(CityStubFactory.mockCities.get(2)));
        when(service.modifyFavoriteCities(
            PatchOperation.add,
            "Denver",
            "johnny.cash@gmail.com"
            )
        ).thenReturn(UserStubFactory.mockUser);

        // act & assert ...
        mockMvc.perform(
            patch("/api/user/favorite-cities/" + PatchOperation.add + "/Denver")
                .header("Authorization", "Bearer " + token)
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id").value("1"))
            .andExpect(jsonPath("$[0].name").value("New York"))
            .andExpect(jsonPath("$[0].population").value(2000000))
            .andExpect(jsonPath("$[0].description").value("New York"));
    }
}
