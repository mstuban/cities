package com.infinum.cities.service;

import com.infinum.cities.exception.CityNotFoundException;
import com.infinum.cities.exception.PatchOperationNotFoundException;
import com.infinum.cities.exception.UserNotFoundException;
import com.infinum.cities.model.City;
import com.infinum.cities.model.User;
import com.infinum.cities.model.dto.PatchOperation;
import com.infinum.cities.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    private final CityService cityService;

    public UserService(UserRepository repository, CityService cityService) {
        this.repository = repository;
        this.cityService = cityService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email).orElseThrow(
            () -> new UserNotFoundException("User " + email + " not found")
        );
    }

    public User save(String email, String password) throws UsernameNotFoundException {
        return repository.save(
            new User(
                email,
                password
            )
        );
    }

    @Transactional
    public User modifyFavoriteCities(PatchOperation op, String cityName, String userEmail) {
        City city = cityService.findByName(cityName).orElseThrow(
            () -> new CityNotFoundException("City" + cityName + " not found")
        );

        User currentUser = repository.findByEmail(userEmail).orElseThrow(
            () -> new UserNotFoundException("User " + userEmail + " not found")
        );

        switch (op) {
            case add:
                return currentUser.addFavoriteCity(city);
            case remove:
                return currentUser.removeFavoriteCity(city);
            default:
                throw new PatchOperationNotFoundException("Patch op " + op + " not supported");
        }
    }
}
