package com.infinum.cities.config;

import com.infinum.cities.auth.jwt.JwtAuthenticationEntryPoint;
import com.infinum.cities.auth.jwt.JwtRequestFilter;
import com.infinum.cities.auth.util.TokenUtil;
import com.infinum.cities.repository.CityRepository;
import com.infinum.cities.repository.UserRepository;
import com.infinum.cities.service.CityService;
import com.infinum.cities.service.UserService;
import com.ninjasquad.springmockk.MockkBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SecurityTestConfiguration {

    @MockkBean
    private CityRepository cityRepository;

    @MockkBean
    private UserRepository userRepository;

    @Bean
    public TokenUtil tokenUtil() {
        return new TokenUtil();
    }

    @Bean
    public CityService cityService() {
        return new CityService(cityRepository);
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository, cityService());
    }

    @Bean
    public JwtRequestFilter requestFilter() {
        return new JwtRequestFilter(userService(), tokenUtil());
    }

    @Bean
    public JwtAuthenticationEntryPoint authEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }
}
