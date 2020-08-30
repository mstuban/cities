package com.infinum.cities.service;

import com.infinum.cities.model.City;
import com.infinum.cities.repository.CityRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.Optional;

@Service
public class CityService {

    private final CityRepository repository;

    public CityService(CityRepository repository) {
        this.repository = repository;
    }

    public Iterable<City> findAll() {
        return repository.findAll();
    }

    public Iterable<City> findAllOrderByCreated() {
        return repository.findAllByOrderByCreated();
    }

    public City save(City city) {
        if (repository.existsByName(city.getName())) {
            throw new EntityExistsException("City " + city.getName() + " already exists");
        }

        return repository.save(city);
    }

    public Optional<City> findByName(String name) {
        return repository.findByName(name);
    }

    public Iterable<City> findAllOrderByMostFavorites() {
        return repository.findByOrderByNumberOfFavoritesAsc();
    }
}
