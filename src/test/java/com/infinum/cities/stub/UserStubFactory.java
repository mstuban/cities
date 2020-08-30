package com.infinum.cities.stub;

import com.infinum.cities.model.User;

import java.util.Collections;
import java.util.HashSet;

public class UserStubFactory {

    public static User mockUser = new User(
        1L,
        "johnny.cash@gmail.com",
        "$2y$12$ULXznv.uPcB5ktw4zeJyU.q0YEw9rkXoj/KRVUw6UpkIK0R8YGEwK",
        new HashSet<>(Collections.singletonList(CityStubFactory.mockCity))
    );

    public static User mockUserWithAddedCity = new User(
        1L,
        "johnny.cash@gmail.com",
        "$2y$12$ULXznv.uPcB5ktw4zeJyU.q0YEw9rkXoj/KRVUw6UpkIK0R8YGEwK",
        new HashSet<>(Collections.singletonList(CityStubFactory.mockCity))
    );
}
