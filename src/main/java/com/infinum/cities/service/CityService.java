package com.infinum.cities.service;

import com.infinum.cities.exception.CityNotFoundException;
import com.infinum.cities.model.City;
import com.infinum.cities.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityService {

    private final CityRepository repository;

    public CityService(CityRepository repository) {
        this.repository = repository;
    }

    public City findById(Long id) {
        return repository.findById(id).orElseThrow(
            () -> new CityNotFoundException("City " + id + " not found")
        );
    }

    public Iterable<City> findAll() {
        return repository.findAll();
    }

    public Iterable<City> findAllOrderByCreated() {
        return repository.findAllByOrderByCreated();
    }

    public City save(City city) {
        return repository.save(city);
    }

    public Optional<City> findByName(String name) {
        return repository.findByName(name);
    }

    public Iterable<City> findAllOrderByMostFavorites() {
        return repository.findByOrderByNumberOfFavoritesAsc();
    }
}
