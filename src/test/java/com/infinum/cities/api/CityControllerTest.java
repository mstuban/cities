package com.infinum.cities.api;

import com.infinum.cities.exception.CityNotFoundException;
import com.infinum.cities.model.City;
import com.infinum.cities.service.CityService;
import com.infinum.cities.stub.CityStubFactory;
import com.infinum.cities.utils.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static java.util.Comparator.comparing;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CityControllerTest {

    private static final Long EXISTING_CITY_ID = 1L;
    private static final Long NON_EXISTING_CITY_ID = 999999L;

    @Mock
    private CityService service;

    private CityController controller;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new CityController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testRetrieveAllCitiesWhenCitiesExist() throws Exception {
        // prepare ...
        when(service.findAll()).thenReturn(CityStubFactory.mockCities);

        // act & assert ...
        mockMvc.perform(get("/api/city"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id").value("1"))
            .andExpect(jsonPath("$[0].name").value("New York"))
            .andExpect(jsonPath("$[0].population").value(2000000))
            .andExpect(jsonPath("$[0].description").value("New York"));
    }

    @Test
    public void testRetrieveAllCitiesWhenCitiesDoNotExist() throws Exception {
        // prepare ...
        when(service.findAll()).thenReturn(Collections.emptyList());

        // act & assert ...
        mockMvc.perform(get("/api/city"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testRetrieveCityByIdWhenItExists() throws Exception {
        // prepare ...
        when(service.findById(EXISTING_CITY_ID)).thenReturn(CityStubFactory.mockCities.get(0));

        // act & assert ...
        mockMvc.perform(get("/api/city/" + EXISTING_CITY_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.name").value("New York"))
            .andExpect(jsonPath("$.population").value(2000000))
            .andExpect(jsonPath("$.description").value("New York"));
    }

    @Test
    public void testRetrieveCityByIdWhenItDoesNotExist() throws Exception {
        // prepare ...
        when(service.findById(NON_EXISTING_CITY_ID)).thenThrow(
            new CityNotFoundException(
                "City " + NON_EXISTING_CITY_ID + " not found"
            )
        );

        // act & assert ...
        mockMvc.perform(get("/api/city/" + NON_EXISTING_CITY_ID))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testFindAllOrderByCreated() throws Exception {
        // prepare ...
        CityStubFactory.mockCities.sort(comparing(City::getCreated));

        when(service.findAllOrderByCreated()).thenReturn(CityStubFactory.mockCities);

        // act & assert ...
        mockMvc.perform(get("/api/city/oldest-first"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id").value("2"))
            .andExpect(jsonPath("$[0].name").value("Chicago"))
            .andExpect(jsonPath("$[0].population").value(1000000))
            .andExpect(jsonPath("$[0].description").value("Chicago"))
            .andExpect(jsonPath("$[1].id").value("3"))
            .andExpect(jsonPath("$[1].name").value("Denver"))
            .andExpect(jsonPath("$[1].population").value(500000))
            .andExpect(jsonPath("$[1].description").value("Denver"))
            .andExpect(jsonPath("$[2].id").value("1"))
            .andExpect(jsonPath("$[2].name").value("New York"))
            .andExpect(jsonPath("$[2].population").value(2000000))
            .andExpect(jsonPath("$[2].description").value("New York"));
    }

    @Test
    public void testFindAllOrderByMostFavorites() throws Exception {
        // prepare ...
        CityStubFactory.mockCities.sort(comparing(City::getNumberOfFavorites));

        when(service.findAllOrderByMostFavorites()).thenReturn(CityStubFactory.mockCities);

        // act & assert ...
        mockMvc.perform(get("/api/city/most-favorites"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id").value("1"))
            .andExpect(jsonPath("$[0].name").value("New York"))
            .andExpect(jsonPath("$[0].population").value(2000000))
            .andExpect(jsonPath("$[0].description").value("New York"))
            .andExpect(jsonPath("$[1].id").value("3"))
            .andExpect(jsonPath("$[1].name").value("Denver"))
            .andExpect(jsonPath("$[1].population").value(500000))
            .andExpect(jsonPath("$[1].description").value("Denver"))
            .andExpect(jsonPath("$[2].id").value("2"))
            .andExpect(jsonPath("$[2].name").value("Chicago"))
            .andExpect(jsonPath("$[2].population").value(1000000))
            .andExpect(jsonPath("$[2].description").value("Chicago"));
    }

    @Test
    public void testSave() throws Exception {
        // prepare ...
        when(service.save(CityStubFactory.mockCity)).thenReturn(CityStubFactory.persistedMockCity);

        // act & assert ...
        mockMvc.perform(
            post("/api/city")
                .content(
                    JsonUtil.toJson(CityStubFactory.mockCity)
                )
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.name").value("Los Angeles"))
            .andExpect(jsonPath("$.population").value(500000))
            .andExpect(jsonPath("$.description").value("Los Angeles"));
    }

    @Test
    public void testSaveWhenFieldsAreMissing() throws Exception {
        // prepare ...

        // act & assert ...
        mockMvc.perform(
            post("/api/city")
                .content(
                    JsonUtil.toJson(CityStubFactory.invalidMockCity)
                )
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest());
    }
}
