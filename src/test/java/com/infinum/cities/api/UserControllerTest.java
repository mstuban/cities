package com.infinum.cities.api;

import com.infinum.cities.auth.util.TokenUtil;
import com.infinum.cities.config.SecurityTestConfiguration;
import com.infinum.cities.exception.CannotAddCityException;
import com.infinum.cities.exception.CityAlreadyAddedException;
import com.infinum.cities.exception.CityNotFoundException;
import com.infinum.cities.model.User;
import com.infinum.cities.model.enums.PatchOperation;
import com.infinum.cities.repository.UserRepository;
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

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
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
    private UserRepository repository;

    @Mock
    private CityService cityService;

    @Mock
    private TokenUtil tokenUtil;

    @Mock
    private User user;

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
    @WithMockUser(username = "johnny.cash@gmail.com")
    public void testModifyFavoriteCitiesWhenAdd() throws Exception {
        // prepare ...
        UserStubFactory.mockUser.setFavoriteCities(Collections.emptySet());
        when(cityService.findByName("Denver")).thenReturn(java.util.Optional.of(CityStubFactory.mockCities.get(2)));
        when(service.modifyFavoriteCities(
            PatchOperation.add,
            "Denver",
            "johnny.cash@gmail.com"
            )
        ).thenReturn(UserStubFactory.mockUserWithAddedCity);

        // act & assert ...
        mockMvc.perform(
            patch("/api/user/favorite-cities/" + PatchOperation.add + "/Denver")
                .header("Authorization", "Bearer " + token)
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.email").value("johnny.cash@gmail.com"))
            .andExpect(jsonPath("$.favoriteCities", hasSize(1)))
            .andExpect(jsonPath("$.favoriteCities[0].name").value("Denver"));
    }

    @Test
    @WithMockUser(username = "johnny.cash@gmail.com")
    public void testModifyFavoriteCitiesWhenAddAndCityDoesNotExist() throws Exception {
        // prepare ...
        when(cityService.findByName("Folsom")).thenThrow(new CityNotFoundException("City Folsom not found"));
        when(service.modifyFavoriteCities(
            PatchOperation.add,
            "Folsom",
            "johnny.cash@gmail.com"
            )
        ).thenThrow(new CityNotFoundException("City Folsom not found"));

        // act & assert ...
        mockMvc.perform(
            patch("/api/user/favorite-cities/" + PatchOperation.add + "/Folsom")
                .header("Authorization", "Bearer " + token)
        ).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johnny.cash@gmail.com")
    public void testModifyFavoriteCitiesWhenAddAndCityIsAlreadyFavorite() throws Exception {
        // prepare ...
        when(cityService.findByName("Denver")).thenReturn(java.util.Optional.of(CityStubFactory.mockCities.get(2)));
        when(service.modifyFavoriteCities(
            any(),
            any(),
            any()
            )
        ).thenThrow(
            new CityAlreadyAddedException("User johnny.cash@gmail.com already added Denver as favorite")
        );

        // act & assert ...
        mockMvc.perform(
            patch("/api/user/favorite-cities/" + PatchOperation.add + "/Denver")
                .header("Authorization", "Bearer " + token)
        ).andExpect(
            status().isBadRequest()
        );
    }

    @Test
    @WithMockUser(username = "johnny.cash@gmail.com")
    public void testModifyFavoriteCitiesWhenRemove() throws Exception {
        // prepare ...
        UserStubFactory.mockUser.setFavoriteCities(Collections.emptySet());
        when(cityService.findByName("Denver")).thenReturn(java.util.Optional.of(CityStubFactory.mockCities.get(2)));
        when(service.modifyFavoriteCities(
            PatchOperation.remove,
            "Denver",
            "johnny.cash@gmail.com"
            )
        ).thenReturn(UserStubFactory.mockUser);

        // act & assert ...
        mockMvc.perform(
            patch("/api/user/favorite-cities/" + PatchOperation.remove + "/Denver")
                .header("Authorization", "Bearer " + token)
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.email").value("johnny.cash@gmail.com"))
            .andExpect(jsonPath("$.favoriteCities", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "johnny.cash@gmail.com")
    public void testModifyFavoriteCitiesWhenRemoveAndCityIsNotFavorite() throws Exception {
        // prepare ...
        when(repository.findByEmail("johnny.cash@gmail.com")).thenReturn(Optional.of(UserStubFactory.mockUser));
        when(service.modifyFavoriteCities(
            PatchOperation.remove,
            "Denver",
            "johnny.cash@gmail.com"
            )
        ).thenThrow(new CannotAddCityException("User johnny.cash@gmail.com does not have Denver as favorite"));

        // act & assert ...
        mockMvc.perform(
            patch("/api/user/favorite-cities/" + PatchOperation.remove + "/Denver")
                .header("Authorization", "Bearer " + token)
        ).andExpect(
            status().isBadRequest()
        );
    }

    @Test
    @WithMockUser(username = "johnny.cash@gmail.com")
    public void testModifyFavoriteCitiesWhenRemoveAndCityDoesNotExist() throws Exception {
        // prepare ...
        when(cityService.findByName("Folsom")).thenThrow(new CityNotFoundException("City Folsom not found"));
        when(service.modifyFavoriteCities(
            PatchOperation.remove,
            "Folsom",
            "johnny.cash@gmail.com"
            )
        ).thenThrow(new CityNotFoundException("City Folsom not found"));

        // act & assert ...
        mockMvc.perform(
            patch("/api/user/favorite-cities/" + PatchOperation.remove + "/Folsom")
                .header("Authorization", "Bearer " + token)
        ).andExpect(status().isNotFound());
    }
}
