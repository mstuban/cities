package com.infinum.cities.auth.constants;

public class SecurityConstants {
    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 900_000; // 15 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/login";
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
}
