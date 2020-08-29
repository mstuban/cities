package com.infinum.cities.repository;

import com.infinum.cities.model.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<City, Long> {

    Iterable<City> findAllByOrderByCreated();

    Optional<City> findByName(String name);

    Collection<City> findByOrderByNumberOfFavoritesAsc();
}
