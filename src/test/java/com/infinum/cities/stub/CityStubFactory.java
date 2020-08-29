package com.infinum.cities.stub;

import com.infinum.cities.model.City;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CityStubFactory {

    public static List<City> mockCities = Arrays.asList(
        new City(
            1L,
            "New York",
            2000000,
            "New York",
            LocalDateTime.now().minusYears(2),
            100
        ),
        new City(
            2L,
            "Chicago",
            1000000,
            "Chicago",
            LocalDateTime.now().minusYears(10),
            300
        ),
        new City(
            3L,
            "Denver",
            500000,
            "Denver",
            LocalDateTime.now().minusYears(5),
            200
        )
    );

    public static City mockCity = new City(
        "Denver",
        500000,
        "Denver"
    );

    public static City invalidMockCity = new City();

    public static City persistedMockCity = new City(
        1L,
        "Los Angeles",
        500000,
        "Los Angeles",
        LocalDateTime.now(),
        0
    );
}
